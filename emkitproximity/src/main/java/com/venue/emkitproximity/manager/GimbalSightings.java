package com.venue.emkitproximity.manager;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.gimbal.android.PlaceManager;
import com.venue.emkitproximity.asynctask.SightingsAsyncTask;
import com.venue.emkitproximity.holder.BeaconInitDetails;
import com.venue.emkitproximity.holder.CardGimbalDetails;
import com.venue.emkitproximity.holder.IniteMkitDetails;
import com.venue.emkitproximity.json.JSONParser;
import com.venue.emkitproximity.notifier.SightingsNotifier;
import com.venue.emkitproximity.utils.Logger;
import com.venue.emkitproximity.utils.StoreDetails;
import com.venue.initv2.AnotherCardActivity;
import com.venue.initv2.EMCardActivity;
import com.venue.initv2.EmkitInitialization;
import com.venue.initv2.EmkitInstance;
import com.venue.initv2.GATrackerEmkit;
import com.venue.initv2.ScreenReceiver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GimbalSightings extends Service implements SightingsNotifier {
    private Context context;
    public static final String PLACE_EVENT_DESCRIPTION_KEY = "PLACE_EVENT_DESCRIPTION_KEY";
    SharedPreferences pref;
    private BeaconHandler beaconHandler;
    public static PlaceManager placeManager;

    private static final String TAG = "GimbalSightings";

    private EmkitInitialization emkitInitialization;

    public GimbalSightings() {
        beaconHandler = new BeaconHandler();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Logger.d(TAG,"onCreate");
        context = GimbalSightings.this;
        //emkitInitialization=EmkitInstance.getInstance(context);
    }

    public void contentEvent() {
        // Logger.d(TAG,"from ContentEvent");

        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        String placeid = pref.getString("placename", null);
        String event_type = pref.getString("eventtype", null);
        //Log.i(TAG, "trigger_event::" + event_type);
        String trigger_event = "";
        if (event_type.equalsIgnoreCase("AT")) {
            trigger_event = "Sighted";
        } else if (event_type.equalsIgnoreCase("CAME")) {
            trigger_event = "Arrived";
        } else if (event_type.equalsIgnoreCase("LEFT")) {
            trigger_event = "Departed";
        }
        Logger.i(TAG, "trigger_event::" + trigger_event);
        Logger.i(TAG, "placeid::" + placeid);
        String beaconId = placeid;
        // Logger.d(TAG,"beacon id"+beaconId);

        // Log.i(TAG, "beaconId " + beaconId);
        GATrackerEmkit.init(context.getApplicationContext());
        GATrackerEmkit.set("SIGHTINGS", "Gimbal_Sightings", true);
        GATrackerEmkit.set("SIGHTINGS", "Gimbal_Sightings");

        GATrackerEmkit.logEvent(trigger_event, event_type, beaconId, null);
        GATrackerEmkit.set("SIGHTINGS", null);
        int cursorCount = -1;


        SharedPreferences s_pref = context.getSharedPreferences("beacon_names", 0);
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
        Boolean tag = false;
        if (cur_beacon == null) {
            tag = false;
        } else {
            tag = true;
        }
        Logger.i(TAG, "place matching::" + tag);
        // Logger.d(TAG,"after cursorcount");


        if (tag) {
            SharedPreferences sightpref = context.getSharedPreferences("sightpref", 0);
            Editor editor = sightpref.edit();
            editor.putString(beaconId, trigger_event);
            editor.putString(beaconId + "_sighted_time", "" + System.currentTimeMillis());
            editor.commit();
            // Logger.d(TAG,"inside cursor count");
            /*
             * notificationTitle = contentDescriptor.getTitle();
			 * notificationDescription =
			 * contentDescriptor.getContentDescription(); campaignId =
			 * contentDescriptor.getCampaignId();
			 */
            StoreDetails sd = new StoreDetails();
            IniteMkitDetails emkitDetails = sd.getIniteMkitDetails(context);
            JSONParser.setContext(context);// Santhana - fix for the crash, on
            // reentering a geofence
            Logger.d(TAG, " " + emkitDetails.getSightingURL());
            Logger.d(TAG, " " + emkitDetails.getEmp2UserId());
            /*SightingsAsyncTask taskIniteMkit = new SightingsAsyncTask(this);
            // Logger.d(TAG,"Manimaran "+beaconId);
			taskIniteMkit.execute(emkitDetails.getSightingURL(),
					emkitDetails.getEmp2UserId(), beaconId, trigger_event);*/
            //New change for fastness - Santhana
            if (Build.VERSION.SDK_INT >= 11)
                new SightingsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emkitDetails.getSightingURL(),
                        emkitDetails.getEmp2UserId(), beaconId, trigger_event);
            else
                new SightingsAsyncTask(this).execute(emkitDetails.getSightingURL(),
                        emkitDetails.getEmp2UserId(), beaconId, trigger_event);
            // Logger.d(TAG,"Sightings API call AsyncTask has been started");
        } else {
            // Logger.d(TAG,"No beacon id found in saved beacon receiver list");
        }

        // }

        // }

    }

    public void startVisitManager() {
        // Logger.i(TAG, "startVisitManager " );
        //Open this out for Proximity - Santhana
        /*VisitManager visitManager = ProximityFactory.getInstance()
				.createVisitManager();
		visitManager.setVisitListener(this);
		visitManager.start();
		BeaconHandler beacon = new BeaconHandler();
		beacon.setValues(context, "", "", "", "", "", "", "", "", "", "");
		beacon.runTimer();*/
    }

    public void clearProximityData() {
		/*BeaconReceiverListDB.cleanup = true;
		BeaconReceiverListDB dbase = new BeaconReceiverListDB(context);
		try {
			dbase.clearSightedRecords();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbase.close();
			BeaconReceiverListDB.cleanup = false;
		}*/
        SharedPreferences sighted_pref = context.getSharedPreferences("sight_beacons", 0);
        Editor edit = sighted_pref.edit();
        edit.clear();
        edit.commit();
        SharedPreferences pref = context.getSharedPreferences("emkit_info",
                Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putString("emp2UserId", null);
        edit.commit();
    }

    public void checkIfBeaconPresent(String beaconId, int beacon_rssi,
                                     BeaconInitDetails beaconData) {
        Logger.d(TAG, "beacon id" + beaconId);
        // Log.i(TAG, "beaconId checking " + beaconId);

        int cursorCount = -1;

        // Logger.d(TAG,"after cursorcount");
        //BeaconReceiverListDB dbase = new BeaconReceiverListDB(context);


        SharedPreferences s_pref = context.getSharedPreferences("beacon_names", 0);
        SharedPreferences pref = context.getSharedPreferences("beacon_info", 0);
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
        Boolean tag = false;
        if (cur_beacon == null) {
            tag = false;
        } else {
            tag = true;
        }


        //Cursor cur = dbase.getBeaconRecordById(beaconId);
        if (tag) {
		/*	cursorCount = cur.getCount();
			cur.moveToFirst();*/
            // Logger.d(TAG,"db getBeaconRecordById - cur.getCount() : " +
            // cursorCopunt);
            //	if (cursorCount > 0) {
            String min_rssi = pref.getString("" + beaconId + "min_rssi", null);
            String max_rssi = pref.getString("" + beaconId + "max_rssi", null);
            if (min_rssi == null || max_rssi == null)
                return;
            Logger.i(beaconId + "-$$$min_rssi ", min_rssi);
            Logger.i(beaconId + "-$$$max_rssi ", max_rssi);
            String relay_interval = pref.getString("" + beaconId + "relay_time_interval", null);
            String max_sighting = pref.getString("" + beaconId + "max_sighting_interval", null);
            // Logger.i(beaconId+"-$$$relay_interval ", relay_interval);
            // Logger.i(beaconId+"-$$$max_sighting ", max_sighting);
            if (Integer.parseInt(min_rssi) <= beacon_rssi
                    && beacon_rssi <= Integer.parseInt(max_rssi)) {
                // insertIntoSightingsList(beaconId, beaconInit);
                beaconHandler.setValues(context, beaconId,
                        beaconData.getBeacon_temp(),
                        beaconData.getBattery_level(),
                        "" + System.currentTimeMillis(),
                        beaconData.getLast_sighted(),
                        beaconData.getLast_sighted_sent_to_server(),
                        beaconData.getPrevious_rssi(),
                        beaconData.getBeacon_rssi(),
                        beaconData.getBeacon_state(),
                        beaconData.getBeacon_name());
                beaconHandler.insertIntoDB();
                // GATrackerEmkit.init(context.getApplicationContext());
					/*GATrackerEmkit.set(Fields.SCREEN_NAME, "Beacon_Sightings",
							true);
					GATrackerEmkit.set(Fields.SCREEN_NAME, "Beacon_Sightings");
					GATrackerEmkit.logEvent(beaconData.getBeacon_state(),
							beaconData.getBeacon_name(), beaconId, null);
					GATrackerEmkit.set(Fields.SCREEN_NAME, null);*/
            } else {
                // Logger.i(TAG, "");
                // notify.onBeaconPresenceSuccessRssiNotMatching(beaconId,
                // true, beaconInit);
                // Logger.d(TAG,"inside cursor count");
                // Logger.d(TAG,"Sightings API call AsyncTask has been started");
            }
			/*} else {
				// Logger.i(TAG, "");
				// Logger.d(TAG,"No beacon id found in saved beacon receiver list");
				// notify.onBeaconPresenceFailure(beaconId, true, beaconInit);
			}*/
        }
	/*	cur.close();

		dbase.close();*/

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

    @Override
    public void onSightingsSuccess(CardGimbalDetails cardDetails) {
		
		/*
		 * cardDetails.setEmkit_URL("actionid=131&card_id=32");
		 * cardDetails.setMessage("Welcome to Champ Sports");
		 * cardDetails.setTitle( "Notification");
		 */
        Logger.d(TAG, "from onSightings Success ");
        if (cardDetails != null) {

            if (cardDetails.getEmkit_URL() != null) {
                Logger.i(TAG, "emkit_URL " + cardDetails.getEmkit_URL());
                Logger.d(TAG, "onSightingsSuccess - Gimbal card triggered"
                        + cardDetails);

                String title = cardDetails.getTitle();
                // Logger.d(TAG,"notification :: title - " + title);

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
						/*try {
							InternalLogEventNotifier notify;
							notify = (InternalLogEventNotifier) context;
							SharedPreferences pref = context
									.getSharedPreferences("emkit_info", 0);

							notify.sendReportEvent("CardReceived", emkit_url
									.substring(emkit_url.indexOf("card_id=")
											+ "card_id=".length(),
											emkit_url.lastIndexOf("&")), "",
									campaignId);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
                        String[] params = emkit_url.split("&");
                        for (String param : params) {
                            String name = param.split("=")[0];
                            String value = param.split("=")[1];
                            if (name.equals("campaign_id")) {
                                campaignId = value;
                            }
                        }
                        int startIdx = emkit_url.indexOf("card_id=");

                        if (startIdx != -1) {

                            emkit_url = emkit_url.substring(startIdx);

                        }
                    }

                    Intent notificationIntent = new Intent(context,
                            EMCardActivity.class);
                    // Logger.d(TAG,"1");
                    Bundle bundle = new Bundle();
                    // Logger.d(TAG,"2");
                    Logger.d(TAG, "notification : final emkit_URL - "
                            + emkit_url);
                    bundle.putString("emkit_url", main_emkit_url);
                    bundle.putString("campaignid", campaignId);
                    bundle.putString("campaign_id", campaign_id);
                    bundle.putString("instantpush_id", instantpush_id);
                    // Logger.d(TAG,"3");
                    notificationIntent.putExtras(bundle);
                    Logger.d(TAG, "before displaying logic");
                    if (!EmkitInstance.getInstance(context).isApplicationInBackground()) {
                        Logger.d(TAG, "app not in bg");
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
                        Logger.d(TAG, "app is in bg");
                        {

                            // Checking notification settings disabled/enabled by user.

                            if (EmkitInstance.getInstance(context).getNotificationStatus().equalsIgnoreCase("1")) {
                                Logger.d(TAG, "Generating notification from gimbal sightings");
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
                                        .setSmallIcon(notificationResource)
                                        //.setLargeIcon(icon)
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
                            } else {

                                Logger.d(TAG, emkitInitialization == null ? "Emkitinitialization null" :
                                        emkitInitialization.getNotificationStatus().equalsIgnoreCase("1")
                                                ? "Notification status true" : "Notification status fals");
                            }

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
        // Logger.d(TAG,"from onSightingsError method");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        // Logger.d(TAG,"from onBind method");
        return null;
    }

}
