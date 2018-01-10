package com.venue.emkitmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.venue.emkitmanager.utils.Logger;
import com.venue.emkitproximity.activity.EmkitNotificationActivity;

/**
 * Created by santhana on 12/23/17.
 */

public class EmkitNotificationMaster {

    public static EmkitNotificationMaster emkitNotificationMaster = null;
    public static Context mContext = null;
    public static NotificationManager notificationManager;
    public static final String CHANNEL_ID = "com.venuetize.emkit.demo";
    public static final String CHANNEL_NAME = "Message channel";

    public EmkitNotificationMaster() {

    }

    public static EmkitNotificationMaster getInstance(Context context) {
        mContext = context;
        if (emkitNotificationMaster == null)
            emkitNotificationMaster = new EmkitNotificationMaster();
        return emkitNotificationMaster;
    }

    public boolean iseMKitPayload(Bundle data) {
        boolean result = false;
        try {
            if (data != null) {
                String emkit_url = data.getString("emkit_URL");
                if (emkit_url != null) {
                    result = true;
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void processeMkitPayload(Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String emkit_url = data.getString("emkit_URL");
        String link = data.getString("link");
        String type = data.getString("type");
        String instantpush_id = data.getString("instantpush_id");
        String campaign_id = data.getString("campaign_id");

        Logger.i("got title ", title);
        Logger.i("got message ", message);
        Logger.i("got emkit_URL ", emkit_url);
        Logger.i("got link ", link);
        Logger.i("got type ", type);
        Logger.i("got instantpush_id ", instantpush_id);
        Logger.i("got campaign_id ", campaign_id);
        String finalurl = "";
        String linkType = "";

        if (type == null || type.equalsIgnoreCase("0")) {
            if (emkit_url == null || emkit_url.equalsIgnoreCase("null") || emkit_url.trim().length() == 0)
                return;
        }

        /*if(link == null  || link.trim().length() == 0 || link.equalsIgnoreCase("0"))
        {
            if (emkit_url == null || emkit_url.equalsIgnoreCase("null") || emkit_url.trim().length() == 0)
                return;
        }*/

        if (title != null && message != null) {

            if (emkit_url != null && !emkit_url.endsWith("&") && emkit_url.trim().length() > 0) {
                emkit_url = emkit_url + "&";

                int startIdx = emkit_url.indexOf("card_id=");

                if (startIdx != -1) {

                    emkit_url = emkit_url.substring(startIdx);

                }
                //finalurl = emkit_url;
            } else {
                emkit_url = "";
            }

            if (type != null && (type.equalsIgnoreCase("link") || type.equalsIgnoreCase("deeplink") || type.equalsIgnoreCase("video") || type.equalsIgnoreCase("audio"))) {
                finalurl = link;
                linkType = type;
            }

            Intent notificationIntent = new Intent(mContext.getApplicationContext(), EmkitNotificationActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle_intent = new Bundle();
            bundle_intent.putString("finalurl", finalurl);
            bundle_intent.putString("linktype", linkType);
            bundle_intent.putString("instantpush_id", instantpush_id);
            bundle_intent.putString("campaign_id", campaign_id);
            if (emkit_url.length() > 0)
                bundle_intent.putString("emkit_url", emkit_url);
            notificationIntent.putExtras(bundle_intent);
            PendingIntent pIntent = PendingIntent.getActivity(mContext.getApplicationContext(), (int) System.currentTimeMillis(), notificationIntent, 0);


            int notificationType = (int) System.currentTimeMillis();

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), notificationType, notificationIntent, 0);
            SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
            int notificationResource = pref.getInt("EmkitNotificationResource", 0);
            initChannels(mContext);
            NotificationCompat.Builder mBuilder = null;
            //if (Build.VERSION.SDK_INT < 26) {
            mBuilder = new NotificationCompat.Builder(mContext);
            /*} else {
                mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
            }*/
            mBuilder.setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(notificationResource)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//			.setTicker("Card Notification")
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1000});
            try {
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationType, mBuilder.build());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        if (notificationManager == null) {
            getNotificationService();
        }
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void getNotificationService() {
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
