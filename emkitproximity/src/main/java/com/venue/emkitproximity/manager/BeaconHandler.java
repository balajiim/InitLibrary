package com.venue.emkitproximity.manager;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.venue.emkitproximity.asynctask.SightingsAsyncTask;
import com.venue.emkitproximity.holder.BeaconSightedDetails;
import com.venue.emkitproximity.holder.CardGimbalDetails;
import com.venue.emkitproximity.holder.IniteMkitDetails;
import com.venue.emkitproximity.json.JSONParser;
import com.venue.emkitproximity.notifier.SightingsNotifier;
import com.venue.emkitproximity.utils.Logger;
import com.venue.emkitproximity.utils.StoreDetails;
import com.venue.initv2.AnotherCardActivity;
import com.venue.initv2.EMCardActivity;
import com.venue.initv2.EmkitInstance;
import com.venue.initv2.GATrackerEmkit;
import com.venue.initv2.ScreenReceiver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BeaconHandler implements SightingsNotifier {

    private static final String TAG = "BeaconHandler";
    private Context context;
    private BeaconSightedDetails beaconDetails;
    private String beaconId;
    private String beacon_temp;
    private String battery_level;
    private String first_sighted;
    private String last_sighted;
    private String last_sighted_sent_to_server;
    private String previous_rssi;
    private String beacon_rssi;
    private String beacon_state;
    private String beacon_name;
    private Timer timer = null;
    private TimerTask doAsynchronousTask = null;

    public BeaconHandler() {
        // runTimer();
    }

    public void setValues(Context context, String beaconId, String beacon_temp,
                          String battery_level, String first_sighted, String last_sighted,
                          String last_sighted_sent_to_server, String previous_rssi,
                          String beacon_rssi, String beacon_state, String beacon_name) {
        this.context = context;
        this.beaconId = beaconId;
        this.beacon_temp = beacon_temp;
        this.battery_level = battery_level;
        this.first_sighted = first_sighted;
        this.last_sighted = last_sighted;
        this.last_sighted_sent_to_server = last_sighted_sent_to_server;
        this.previous_rssi = previous_rssi;
        this.beacon_rssi = beacon_rssi;
        this.beacon_state = beacon_state;
        this.beacon_name = beacon_name;
    }

    public void callSighted(String beaconId, boolean saveFlag) {
        if (context == null)
            context = EmkitInstance.getContext();
        StoreDetails sd = new StoreDetails();
        IniteMkitDetails emkitDetails = sd.getIniteMkitDetails(context);
        JSONParser.setContext(context);
        if (saveFlag) {
            SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
            Editor editor = sightpref.edit();
            editor.putString(beaconId, "Sighted");
            editor.commit();
        }
        //New change for fastness - Santhana
        /*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(this);
        taskIniteMkit.execute(emkitDetails.getSightingURL(),
				emkitDetails.getEmp2UserId(), beaconId, "Sighted");*/
        if (Build.VERSION.SDK_INT >= 11)
            new SightingsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Sighted");
        else
            new SightingsAsyncTask(this).execute(emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Sighted");
    }

    public void insertIntoDB() {
        // Logger.i(TAG, "going to insert");

        //BeaconReceiverListDB dbase = new BeaconReceiverListDB(context);
        SharedPreferences s_pref = context.getSharedPreferences("beacon_names", 0);
        SharedPreferences sighted_pref = context.getSharedPreferences("sight_beacons", 0);
        Map<String, ?> allEntries = sighted_pref.getAll();
        HashSet<String> hs_val = new HashSet<String>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            hs_val.add(entry.getKey());
        }
        HashMap<String, String> s_hmap = new HashMap<String, String>();
        Iterator<String> iterator1 = hs_val.iterator();

        // check values
        while (iterator1.hasNext()) {
            String beacon_name = iterator1.next();
            s_hmap.put("" + beacon_name + "", beacon_name);
        }

        Set<String> set = s_pref.getStringSet("beaconnames", null);
        if (set == null)
            return;
        Iterator<String> iterator = set.iterator();
        HashMap<String, String> hmap = new HashMap<String, String>();

        while (iterator.hasNext()) {
            String beacon_name = iterator.next();
            hmap.put("" + beacon_name + "", beacon_name);
        }
        String cur_beacon = hmap.get(beaconId);
        String sight_beacon = s_hmap.get(beaconId);
        Boolean tag = false;
        Boolean s_tag = false;
        if (cur_beacon == null) {
            tag = false;
        } else {
            tag = true;
        }
        if (sight_beacon == null) {
            s_tag = false;
        } else {
            s_tag = true;
        }

        //Cursor cur = dbase.getBeaconRecordById(beaconId);
        //	Cursor curSighted = dbase.getSightedRecordById(beaconId);
        BeaconSightedDetails beaconSightedHolder = new BeaconSightedDetails();
        SharedPreferences pref = context.getSharedPreferences("beacon_info", 0);
        try {
            if (tag) {
                Logger.i(TAG, "Santhana going to insert 1111 ");
                //cur.moveToFirst();
                beaconSightedHolder.setId(pref.getString("" + beaconId + "beacon_id", null));
                beaconSightedHolder.setDwell_time(pref.getString("" + beaconId + "dwell_time", null));
                beaconSightedHolder.setMin_rssi(pref.getString("" + beaconId + "min_rssi", null));
                beaconSightedHolder.setMax_rssi(pref.getString("" + beaconId + "max_rssi", null));
                beaconSightedHolder
                        .setRelay_time_interval(pref.getString("" + beaconId + "relay_time_interval", null));
                beaconSightedHolder
                        .setMax_sighting_interval(pref.getString("" + beaconId + "max_sighting_interval", null));
                beaconSightedHolder.setBeacon_temp(beacon_temp);
                beaconSightedHolder.setBattery_level(battery_level);
                beaconSightedHolder.setFirst_sighted(pref.getString("" + beaconId + "first_sighted", null));
                beaconSightedHolder.setLast_sighted(last_sighted);
                beaconSightedHolder
                        .setLast_sighted_sent_to_server(pref.getString("" + beaconId + "last_sighted_sent_to_server", null));
                beaconSightedHolder.setPrevious_rssi(previous_rssi);
                beaconSightedHolder.setBeacon_rssi(beacon_rssi);
                beaconSightedHolder.setBeacon_state(beacon_state);
                beaconSightedHolder.setBeacon_name(beacon_name);

            }
            if (s_tag) {
                Logger.i(TAG, "Santhana Already in sighted list");
                //curSighted.moveToFirst();
                // Logger.i(
                // TAG,
                // "Already in sighted list "
                // + curSighted.getString(curSighted
                // .getColumnIndex(BeaconReceiverListDB.COL_BEACON_ID)));
                beaconSightedHolder.setBeacon_state("Sighted");

                beaconSightedHolder
                        .setFirst_sighted(beaconSightedHolder.getFirst_sighted());
                beaconSightedHolder
                        .setLast_sighted_sent_to_server(beaconSightedHolder.getLast_sighted_sent_to_server());

                long relay_time = Long
                        .parseLong(beaconSightedHolder.getRelay_time_interval());

                long max_sighting_time = Long
                        .parseLong(beaconSightedHolder.getMax_sighting_interval());

                String first_sighted = beaconSightedHolder.getFirst_sighted();
                //Logger.i(TAG, "Santhana first_sighted " + first_sighted);
                if (first_sighted != null && first_sighted.trim().equals(""))
                    first_sighted = "0";
                if (first_sighted == null)
                    first_sighted = "0";
                long first_millis = Long
                        .parseLong(first_sighted);

                String last_sent_time = beaconSightedHolder.getLast_sighted_sent_to_server();
                if (last_sent_time.equals(""))
                    last_sent_time = "0";
                long last_millis = Long.parseLong(last_sent_time);
                long first_seconds = TimeUnit.MILLISECONDS.toSeconds(System
                        .currentTimeMillis() - first_millis);
                Logger.i(TAG, "Santhana last_millis " + last_millis);
                Logger.i(TAG, "Santhana current_millis " + System
                        .currentTimeMillis());
                long last_seconds = TimeUnit.MILLISECONDS.toSeconds(System
                        .currentTimeMillis() - last_millis);
                /*
				 * if (last_millis == 0) last_seconds = 0;
				 */
                // Logger.i(TAG, "SERVERCALL last_seconds " + last_seconds);
                // Logger.i(TAG, "SERVERCALL relay_time " + relay_time);
                Logger.i(TAG, "Santhana beaconId " + beaconId);
                Logger.i(TAG, "Santhana first_seconds " + first_seconds);
                Logger.i(TAG, "Santhana max_sighting_time " + max_sighting_time);
                Logger.i(TAG, "Santhana last_seconds " + last_seconds);
                Logger.i(TAG, "Santhana relay_time " + relay_time);
                // if (first_seconds > 300) {
                if (first_seconds < max_sighting_time) {
                    if (last_seconds > relay_time) {
                        // Logger.i(TAG, "SERVERCALL S "+beaconId);
						/*beaconSightedHolder.setLast_sighted_sent_to_server(""
								+ System.currentTimeMillis());*/
                        //dbase.updateBeaconSightedDetails(beaconSightedHolder);

                        SharedPreferences sight_pref = context.getSharedPreferences("sight_beacons", 0);
                        Editor edit = sight_pref.edit();
                        edit.putString("" + beaconId + "", beaconId);
                        edit.commit();

                        SharedPreferences beacon_pref = context.getSharedPreferences("beacon_info", 0);
                        edit = beacon_pref.edit();
                        edit.putString(beaconId + "last_sighted_sent_to_server", "" + System.currentTimeMillis());
                        edit.commit();
                        Logger.i(TAG, "Santhana sighting server call for beaconid " + beaconId);
                        // Logger.i(
                        // TAG,
                        // "Updated in sighted list "
                        // + curSighted.getString(curSighted
                        // .getColumnIndex(BeaconReceiverListDB.COL_BEACON_ID)));
                        // if (beaconId.equalsIgnoreCase("MXVX-WM4PU")) {
                        StoreDetails sd = new StoreDetails();
                        IniteMkitDetails emkitDetails = sd
                                .getIniteMkitDetails(context);
                        JSONParser.setContext(context);
                        SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
                        Editor editor = sightpref.edit();
                        editor.putString(beaconId, "Sighted");
                        editor.commit();
						/*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(
								this);
						taskIniteMkit.execute(emkitDetails.getSightingURL(),
								emkitDetails.getEmp2UserId(), beaconId,
								"Sighted", beacon_name);*/
                        //New change for fastness - Santhana
                        if (Build.VERSION.SDK_INT >= 11)
                            new SightingsAsyncTask(
                                    this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                                    emkitDetails.getEmp2UserId(), beaconId,
                                    "Sighted", beacon_name);
                        else
                            new SightingsAsyncTask(
                                    this).execute(emkitDetails.getSightingURL(),
                                    emkitDetails.getEmp2UserId(), beaconId,
                                    "Sighted", beacon_name);
						/*GATrackerEmkit.set(Fields.SCREEN_NAME, "Beacon_Sightings",
								true);
						GATrackerEmkit.set(Fields.SCREEN_NAME, "Beacon_Sightings");
						GATrackerEmkit.logEvent(beaconSightedHolder.getBeacon_state(),
								beaconSightedHolder.getBeacon_name(), beaconId, null);
						GATrackerEmkit.set(Fields.SCREEN_NAME, null);*/
                        // }
                    } else {
                        SharedPreferences sight_pref = context.getSharedPreferences("sight_beacons", 0);
                        Editor edit = sight_pref.edit();
                        edit.putString("" + beaconId + "", beaconId);
                        edit.commit();
                        //dbase.updateBeaconSightedDetails(beaconSightedHolder);
                    }
                } else {
					/*SharedPreferences beacon_pref = context.getSharedPreferences("beacon_info", 0);
					Editor edit = beacon_pref.edit();
		    		edit.putString(beaconId+"first_sighted", null);
		    		edit.putString(beaconId+"last_sighted", null);
		    		edit.commit();*/
                }
				/*
				 * } else {
				 * beaconSightedHolder.setLast_sighted_sent_to_server("" +
				 * System.currentTimeMillis());
				 * beaconSightedHolder.setFirst_sighted("" +
				 * System.currentTimeMillis());
				 * dbase.updateBeaconSightedDetails(beaconSightedHolder);
				 * //Logger.i( // TAG, // "Updated in sighted list " // +
				 * curSighted.getString(curSighted //
				 * .getColumnIndex(BeaconReceiverListDB.COL_BEACON_ID))); // if
				 * (beaconId.equalsIgnoreCase("MXVX-WM4PU")) { StoreDetails sd =
				 * new StoreDetails(); IniteMkitDetails emkitDetails = sd
				 * .getIniteMkitDetails(context);
				 * JSONParser.setContext(context); SightingsAsyncTask
				 * taskIniteMkit = new SightingsAsyncTask( this);
				 * taskIniteMkit.execute(emkitDetails.getSightingURL(),
				 * emkitDetails.getEmp2UserId(), beaconId, "Sighted",
				 * beacon_name); }
				 */
            } else {
                Logger.i(TAG, "Santhana SERVERCALL A/S " + beaconSightedHolder
                        .getLast_sighted());
                beaconSightedHolder.setBeacon_state("Arrived");
				/*beaconSightedHolder.setFirst_sighted(beaconSightedHolder
						.getLast_sighted());*/
				/*beaconSightedHolder.setLast_sighted_sent_to_server(""
						+ System.currentTimeMillis());*/
				
		/*		SharedPreferences sight_pref=context.getSharedPreferences("sight_info", 0);
				Editor edit=sight_pref.edit();
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.putString(""+beaconSightedHolder.getId()+"beacon_id",beaconSightedHolder.getId());
				edit.commit();*/


                //dbase.insertBeaconSightedDetails(beaconSightedHolder);
                // Logger.i(
                // TAG,
                // "Inserted in sighted list "
                // + cur.getString(cur
                // .getColumnIndex(BeaconReceiverListDB.COL_BEACON_ID)));
                // if (beaconId.equalsIgnoreCase("MXVX-WM4PU")) {
                StoreDetails sd = new StoreDetails();
                IniteMkitDetails emkitDetails = sd.getIniteMkitDetails(context);
                JSONParser.setContext(context);
                SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(this);
                taskIniteMkit.execute(emkitDetails.getSightingURL(),
                        emkitDetails.getEmp2UserId(), beaconId, "Arrived");

                beaconSightedHolder.setBeacon_state("Sighted");
				/*beaconSightedHolder.setFirst_sighted(beaconSightedHolder
						.getLast_sighted());*/
                beaconSightedHolder.setLast_sighted_sent_to_server(""
                        + System.currentTimeMillis());
                SharedPreferences beacon_pref = context.getSharedPreferences("beacon_info", 0);
                Editor edit1 = beacon_pref.edit();
                edit1.putString(beaconId + "last_sighted_sent_to_server", "" + System.currentTimeMillis());
                edit1.commit();
                SharedPreferences sight_pref = context.getSharedPreferences("sight_beacons", 0);
                Editor edit = sight_pref.edit();
                edit.putString("" + beaconId + "", beaconId);
                edit.commit();
                //dbase.insertBeaconSightedDetails(beaconSightedHolder);
                taskIniteMkit = new SightingsAsyncTask(this);
                taskIniteMkit.execute(emkitDetails.getSightingURL(),
                        emkitDetails.getEmp2UserId(), beaconId, "Sighted",
                        beacon_name);
                // }
            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
			/*if (cur != null)
				cur.close();*/
			/*if (curSighted != null)
				curSighted.close();
			if (dbase != null)
				dbase.close();*/
        }
    }

    @Override
    public void onSightingsSuccess(CardGimbalDetails cardDetails) {
        // TODO Auto-generated method stub
        // Logger.i(TAG, "sighting success");
        if (cardDetails != null) {

            if (cardDetails.getEmkit_URL() != null) {

                // Logger.d(TAG,"onSightingsSuccess - Gimbal card triggered");

                String title = cardDetails.getTitle();
                Logger.d(TAG, "notification :: title - " + title);

                String message = cardDetails.getMessage();
                Logger.d(TAG, "notification :: message - " + message);

                String emkit_url = cardDetails.getEmkit_URL();
                // Logger.d(TAG,"notification :: emkit_URL - " +
                // emkit_url);

                String campaign_id = cardDetails.getCampaign_id();
                String instantpush_id = cardDetails.getInstantpush_id();

                String campaignId = cardDetails.getCampaignId();

                if (title != null && message != null && emkit_url != null) {
                    String main_emkit_url = "";
                    if (!emkit_url.endsWith("&")) {
                        emkit_url = emkit_url + "&";
                        main_emkit_url = emkit_url;
                        try {
							/*InternalLogEventNotifier notify;
							notify = (InternalLogEventNotifier) context;
							SharedPreferences pref = context
									.getSharedPreferences("emkit_info", 0);

							notify.sendReportEvent("CardReceived", emkit_url
									.substring(emkit_url.indexOf("card_id=")
											+ "card_id=".length(),
											emkit_url.lastIndexOf("&")), "",
									campaignId);*/
                            // GATrackerEmkit.set(Fields.SCREEN_NAME,
                            // "CardReceived");
                            // GATrackerEmkit.init(context.getApplicationContext());
                            GATrackerEmkit.set("SIGHTINGS",
                                    "CardReceived", true);
                            GATrackerEmkit.set("SIGHTINGS",
                                    "CardReceived");
                            GATrackerEmkit.logEvent("CardReceived", emkit_url
                                            .substring(emkit_url.indexOf("card_id=")
                                                            + "card_id=".length(),
                                                    emkit_url.lastIndexOf("&")),
                                    campaignId, null);
                            GATrackerEmkit.set("SIGHTINGS", null);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        int startIdx = emkit_url.indexOf("card_id=");

                        if (startIdx != -1) {

                            emkit_url = emkit_url.substring(startIdx);

                        }
                    }
                    if (context == null)
                        context = EmkitInstance.getContext();
                    Intent notificationIntent = new Intent(context,
                            EMCardActivity.class);
                    // Logger.d(TAG,"1");
                    Bundle bundle = new Bundle();
                    // Logger.d(TAG,"2");
                    // Logger.d(TAG,"notification : final emkit_URL - " +
                    // emkit_url);
                    bundle.putString("emkit_url", main_emkit_url);
                    bundle.putString("campaign_id", campaign_id);
                    bundle.putString("instantpush_id", instantpush_id);
                    // Logger.d(TAG,"3");
                    notificationIntent.putExtras(bundle);
                    if (!EmkitInstance.getInstance(context).isApplicationInBackground()) {
                        if (!ScreenReceiver.isScreenOn) {

                            Intent viewIntent = new Intent(context,
                                    AnotherCardActivity.class);
                            Bundle bundle1 = new Bundle();
                            Logger.d(TAG, "2");
                            Logger.d(TAG, "notification : final emkit_URL - "
                                    + emkit_url);
                            bundle1.putString("title", title);
                            bundle1.putString("message", message);
                            bundle1.putString("emkit_url", emkit_url);
                            bundle1.putString("campaignid", campaignId);
                            bundle1.putString("campaign_id", campaign_id);
                            bundle1.putString("instantpush_id", instantpush_id);
                            viewIntent.putExtras(bundle1);
                            viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(viewIntent);
                        } else {
                            notificationIntent
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(notificationIntent);
                        }
                    } else {

                        {

                            Logger.d(TAG, "4");
                            int notificationType = (int) System
                                    .currentTimeMillis();
                            // Logger.d(TAG,"5");
                            notificationIntent
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent pendingIntent = PendingIntent
                                    .getActivity(context, notificationType,
                                            notificationIntent, 0);
                            // Logger.d(TAG,"6");
                            Bitmap icon = null;
							/*int app_icon=0;
							try {
								PackageManager pm = context.getPackageManager();
								Drawable ico =context. getApplicationInfo().loadIcon(context.getPackageManager()); 
								icon=((BitmapDrawable)ico).getBitmap();
								app_icon=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/

                            SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
                            int notificationResource = pref.getInt("EmkitNotificationResource", 0);


                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                    context)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    .setAutoCancel(true)
                                    //.setLargeIcon(icon)
                                    .setSmallIcon(notificationResource)
                                    .setContentIntent(pendingIntent)
                                    .setWhen(System.currentTimeMillis())
                                    // .setTicker("Card Notification")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(message))
                                    .setSound(
                                            RingtoneManager
                                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setVibrate(new long[]{1000});
                            // Logger.d(TAG,"7");
                            NotificationManager notificationManager = (NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                            // Logger.d(TAG,"8");
                            notificationManager.notify(notificationType,
                                    mBuilder.build());
                            // Logger.d(TAG,"9");

                        }

                    }
                }

				/*
				 * Notification notification = new Notification(R.drawable.icon,
				 * notificationTitle, System.currentTimeMillis()); //
				 * notification.flags |= Notification.FLAG_AUTO_CANCEL;
				 * notification.ledARGB = 0xff31a2dd; notification.ledOnMS =
				 * 500; notification.ledOffMS = 200; notification.flags |=
				 * Notification.FLAG_SHOW_LIGHTS; notification.defaults |=
				 * Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
				 * 
				 * PendingIntent pendingIntent = createPendingIntent(beaconId);
				 * 
				 * notification.setLatestEventInfo(this, notificationTitle,
				 * notificationDescription, pendingIntent);
				 * 
				 * ((NotificationManager)
				 * getSystemService(Context.NOTIFICATION_SERVICE
				 * )).notify(notificationId, notification);
				 * 
				 * notificationId += 1;
				 */
            } else {
                // Logger.d(TAG,"from else condition");
                // Logger.d(TAG,"onSightingsSuccess - Sighted");
            }

        } else {
            // Logger.d(TAG,"onSightingsSuccess - not Sighted and no card triggered. Some error in response");
        }
    }

    @Override
    public void onSightingsError() {
        // TODO Auto-generated method stub
        Logger.i(TAG, "sighting failure");
    }

    public static boolean isApplicationSentToBackground(final Context context) {
        if (!ScreenReceiver.isScreenOn) {
            return true;
        }
        android.app.ActivityManager am = (android.app.ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void callSighted(String beaconId) {
        StoreDetails sd = new StoreDetails();
        IniteMkitDetails emkitDetails = sd.getIniteMkitDetails(context);
        JSONParser.setContext(context);
        SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
        Editor editor = sightpref.edit();
        editor.putString(beaconId, "Sighted");
        editor.commit();
		/*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(this);
		taskIniteMkit.execute(emkitDetails.getSightingURL(),
				emkitDetails.getEmp2UserId(), beaconId, "Sighted");*/
        //New change for fastness - Santhana
        if (Build.VERSION.SDK_INT >= 11)
            new SightingsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Sighted");
        else
            new SightingsAsyncTask(this).execute(emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Sighted");
    }

    public void callDeparture(String beaconId) {
        //	BeaconReceiverListDB.cleanup = true;
        StoreDetails sd = new StoreDetails();
        IniteMkitDetails emkitDetails = sd.getIniteMkitDetails(context);
        JSONParser.setContext(context);
        SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
        Editor editor = sightpref.edit();
        editor.putString(beaconId, "Departed");
        editor.commit();
		/*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(this);
		taskIniteMkit.execute(emkitDetails.getSightingURL(),
				emkitDetails.getEmp2UserId(), beaconId, "Departed");*/
        //New change for fastness - Santhana
        if (Build.VERSION.SDK_INT >= 11)
            new SightingsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Departed");
        else
            new SightingsAsyncTask(this).execute(emkitDetails.getSightingURL(),
                    emkitDetails.getEmp2UserId(), beaconId, "Departed");
		/*BeaconReceiverListDB dbase = new BeaconReceiverListDB(context);
		dbase.deleteSightedRecordById(beaconId);
		BeaconReceiverListDB.cleanup = false;
		dbase.close();*/
    }

    public void cleanUpBeaconSightingsList() {
        // Logger.i(TAG, "cleanup first");
        try {
            //BeaconReceiverListDB dbase = new BeaconReceiverListDB(context);
            //Cursor curSighted = dbase.getAll_SightedEntries();
            SharedPreferences sighted_pref = context.getSharedPreferences("sight_beacons", 0);
            Map<String, ?> allEntries = sighted_pref.getAll();
            HashSet<String> hs_val = new HashSet<String>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                // Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                hs_val.add(entry.getKey());
            }
            HashMap<String, String> s_hmap = new HashMap<String, String>();
            Iterator<String> iterator1 = hs_val.iterator();

            // check values
            while (iterator1.hasNext()) {
                String beacon_name = iterator1.next();
                // s_hmap.put(""+beacon_name+"",beacon_name );
                SharedPreferences pref = context.getSharedPreferences("beacon_info", 0);

                // Logger.i(TAG, "inside beacon cleanup loop");
                String beaconId = iterator1.next();
                long last_sighted = Long.parseLong(pref.getString("" + beaconId + "last_sighted", null));
                long last_seconds = TimeUnit.MILLISECONDS
                        .toSeconds(System.currentTimeMillis()
                                - last_sighted);
                // Logger.i(TAG, "cleanup seconds "+last_seconds);

                if (last_seconds > 300) {
                    // Logger.i(TAG, "cleanup Sent departure call");
                    StoreDetails sd = new StoreDetails();
                    IniteMkitDetails emkitDetails = sd
                            .getIniteMkitDetails(context);
                    JSONParser.setContext(context);
                    SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
                    Editor editor = sightpref.edit();
                    editor.putString(beaconId, "Sighted");
                    editor.commit();
						/*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(
								this);
						taskIniteMkit.execute(
								emkitDetails.getSightingURL(),
								emkitDetails.getEmp2UserId(), beaconId,
								"Sighted");*/
                    //New change for fastness - Santhana
                    if (Build.VERSION.SDK_INT >= 11)
                        new SightingsAsyncTask(
                                this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                                emkitDetails.getEmp2UserId(), beaconId,
                                "Sighted");
                    else
                        new SightingsAsyncTask(
                                this).execute(emkitDetails.getSightingURL(),
                                emkitDetails.getEmp2UserId(), beaconId,
                                "Sighted");
                    // To update the beacon record - without fail
                    // dbase.deleteSightedRecordById(beaconId);
                }


            }
            //SharedPreferences sighted_pref=context.getSharedPreferences("sight_beacons", 0);
	
		      
		/*	try {
				// Logger.i(TAG,
				// "inside beacon cleanup count "+curSighted.getCount());
				if (curSighted != null) {
					// Logger.i(TAG, "inside beacon cleanup");
					// curSighted.moveToFirst();
					while (curSighted.moveToNext()) {}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (curSighted != null)
					curSighted.close();
				if (dbase != null)
					dbase.close();
			}*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //BeaconReceiverListDB.cleanup = false;
    }

    public void runTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (doAsynchronousTask != null) {
            doAsynchronousTask.cancel();
            doAsynchronousTask = null;
        }
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {

            @SuppressWarnings("unchecked")
            public void run() {
                try {
                    // Logger.i(TAG, "background timer running");
                    // BeaconReceiverListDB.cleanup = true;
                    // cleanUpBeaconSightingsList();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        };
        timer.schedule(doAsynchronousTask, 5 * 60 * 1000, 5 * 60 * 1000);
    }
}
