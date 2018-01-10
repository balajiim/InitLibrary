package com.venue.emkitproximity.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.gimbal.android.Attributes;
import com.gimbal.android.Beacon.BatteryLevel;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Place;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.radiusnetworks.proximity.KitConfig;
import com.radiusnetworks.proximity.ProximityKitBeacon;
import com.radiusnetworks.proximity.ProximityKitBeaconRegion;
import com.radiusnetworks.proximity.ProximityKitGeofenceNotifier;
import com.radiusnetworks.proximity.ProximityKitGeofenceRegion;
import com.radiusnetworks.proximity.ProximityKitManager;
import com.radiusnetworks.proximity.ProximityKitMonitorNotifier;
import com.radiusnetworks.proximity.ProximityKitRangeNotifier;
import com.radiusnetworks.proximity.ProximityKitSyncNotifier;
import com.radiusnetworks.proximity.geofence.GooglePlayServicesException;
import com.venue.emkitproximity.asynctask.BeaconListAsyncTask;
import com.venue.emkitproximity.asynctask.LocationAsyncTask;
import com.venue.emkitproximity.holder.BeaconInitDetails;
import com.venue.emkitproximity.holder.EmkitGimbalBeaconSighting;
import com.venue.emkitproximity.holder.EmkitGimbalVisit;
import com.venue.emkitproximity.holder.GimbalPlaceEvent;
import com.venue.emkitproximity.notifier.EmkitReportUserLocationNotifier;
import com.venue.emkitproximity.notifier.GimbalInternalNotifier;
import com.venue.emkitproximity.notifier.LocationNotifier;
import com.venue.emkitproximity.utils.EmkitPermissionHelper;
import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitGPSTracker;
import com.venue.initv2.holder.FindMyFriend;
import com.venue.initv2.holder.Geofence;
import com.venue.initv2.holder.IniteMkitDetails;
import com.venue.initv2.holder.ProximityServices;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class ProximityController extends PlaceEventListener implements
        LocationNotifier, ProximityKitMonitorNotifier,
        ProximityKitRangeNotifier, ProximityKitSyncNotifier,
        ProximityKitGeofenceNotifier, EmkitReportUserLocationNotifier {

    public static LinkedList<GimbalPlaceEvent> al = null;

    private static final String TAG = ProximityController.class.getName();

    private static Context mContext;
    private static ProximityController proximityInstance = null;
    private static final int MAX_NUM_EVENTS = 50;
    //Blocked for SDK
    public static PlaceManager placeManager;
    public static CommunicationListener communicationListener;
    public static BeaconManager beaconManager;
    private static boolean isRadiusRunning;
    // Radius Integration <START>

    // Singleton storage for an instance of the manager
    private static ProximityKitManager pkManager = null;
    // Object to use as a thread-safe lock
    private static final Object pkManagerLock = new Object();
    // Flag for tracking if the app was started in the background.
    private boolean haveDetectedBeaconsSinceBoot = false;

    // Radius Integration <END>

    //Proximity Flags
    public static String PROXIMITY_GIMBAL_GEO = "0";
    public static String PROXIMITY_GIMBAL_BLE = "0";
    public static String PROXIMITY_SIGNAL360_AUDIO = "0";
    public static String PROXIMITY_SIGNAL360_BLE = "0";
    public static String PROXIMITY_SIGNAL360_GEO = "0";
    public static String PROXIMITY_RADIUS_BLE = "0";
    public static String PROXIMITY_RADIUS_GEO = "0";
    public static String PROXIMITY_LISTNR_AUDIO = "0";
    public static String PROXIMITY_FOOTMARKS = "0";

    public ProximityController(Context context) {
        mContext = context;
    }

    public static synchronized ProximityController getInstance(Context context) {
        mContext = context;
        if (proximityInstance == null) {
            proximityInstance = new ProximityController(context);
            checkPermissions();
            prepareProximityFlags();
        }
        return proximityInstance;
    }

    private static void checkPermissions() {
        if (!EmkitPermissionHelper.getInstance(mContext).verifyLocationPermission()) {
            EmkitPermissionHelper.getInstance(mContext).startPermissionActivity("init", 124);
        }
    }

    private static void prepareProximityFlags() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String emkit_init_response = pref.getString("emkit_init_response", null);
        if (emkit_init_response != null) {
            Gson gson = new Gson();
            try {
                IniteMkitDetails initDetails = gson.fromJson(emkit_init_response, IniteMkitDetails.class);
                ProximityServices services = initDetails.getProximityServices();
                if (services != null) {
                    PROXIMITY_GIMBAL_BLE = services.getGimbalBle();
                    PROXIMITY_GIMBAL_GEO = services.getGimbalGeo();
                    PROXIMITY_RADIUS_BLE = services.getRadiusBle();
                    PROXIMITY_RADIUS_GEO = services.getRadiusGeo();
                    PROXIMITY_SIGNAL360_AUDIO = services.getSignal360Audio();
                    PROXIMITY_SIGNAL360_BLE = services.getSignal360Ble();
                    PROXIMITY_SIGNAL360_GEO = services.getSignal360Geo();
                    PROXIMITY_LISTNR_AUDIO = services.getListenrAudio();
                    PROXIMITY_FOOTMARKS = services.getFootmarksBle();

                }
            } catch (Exception e) {
            }
        }
    }

    protected void initGimbal(Application application) {
        al = new LinkedList<GimbalPlaceEvent>();
        // Activity activity = (Activity) context2;
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        // String senderId = pref.getString("sender_id", "0");
        String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
        if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
                && !gimbalAPIKey.equals("0")) {
            Gimbal.setApiKey(application, gimbalAPIKey);

            if (PROXIMITY_GIMBAL_GEO != null && PROXIMITY_GIMBAL_GEO.equals("1")) {
                placeManager = PlaceManager.getInstance();
                placeManager.addListener(this);
                placeManager.startMonitoring();
            }

            Logger.i("Gimbal", "Gimbal.getApplicationInstanceIdentifier()::"
                    + Gimbal.getApplicationInstanceIdentifier());
            Editor edit = pref.edit();
            edit.putString("gimbal_user_id",
                    Gimbal.getApplicationInstanceIdentifier());
            edit.commit();

            String gimbal_flag = pref.getString("gimbal_flag", null);
            if (gimbal_flag != null && gimbal_flag.equals("ON")) {
                if (PROXIMITY_GIMBAL_BLE != null && PROXIMITY_GIMBAL_BLE.equals("1")) {
                    beaconManager = new BeaconManager();
                    beaconManager.addListener(beaconListener);
                    beaconManager.startListening();
                }
            }
        }

    }

    public void assureProximityMonitoring() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String status = pref.getString("optin_oked", "-1");
        Logger.i("ProximityController", status);
        String optinstop = pref.getString("gimbal_stop", "0");
        //Blocked for SDK
        /*String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
        if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
				&& !gimbalAPIKey.equals("0")) {
			Gimbal.setApiKey((Application) mContext.getApplicationContext(),
					gimbalAPIKey);// To start the main thread of gimbal
			PlaceManager.getInstance().addListener(proximityInstance);
			if (status != null && (status.equals("1") || status.equals("-1"))) {
				if (optinstop != null && optinstop.equals("0"))
					PlaceManager.getInstance().startMonitoring();
			} else {
				PlaceManager.getInstance().stopMonitoring();
			}
		}
		String sonicStatus = pref.getString("sonic_stop", "1");
		if (sonicStatus != null && sonicStatus.equals("0")) {
			startSonicMonitoring(mContext);
			if (!pref.getBoolean("audioscan", true))
				Sonic.get().setForceAudioOff(true);
			else
				Sonic.get().setForceAudioOff(false);
		}*/

        String beacon_services_flag = pref.getString(
                "beacon_services_flag", null);
        //Blocked for SDK
        if (beacon_services_flag != null
                && beacon_services_flag.equals("ON")) {

            String gimbal_flag = pref.getString("gimbal_flag", null);
            if (gimbal_flag != null && gimbal_flag.equals("ON")) {
                String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
                if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
                        && !gimbalAPIKey.equals("0")) {
                    Gimbal.setApiKey((Application) mContext.getApplicationContext(),
                            gimbalAPIKey);// To start the main thread of gimbal
                    PlaceManager.getInstance().addListener(proximityInstance);
                    if (status != null && (status.equals("1") || status.equals("-1"))) {
                        if (optinstop != null && optinstop.equals("0"))
                            PlaceManager.getInstance().startMonitoring();
                    } else {
                        PlaceManager.getInstance().stopMonitoring();
                    }
                }
                initGimbal((Application) mContext
                        .getApplicationContext());
            }
            String sonic_flag = pref.getString("sonic_flag", null);
            if (sonic_flag != null && sonic_flag.equals("ON")) {
                //startSonicMonitoring(EmkitInstance.setContext());
            }
            String radius_flag = pref.getString("radius_flag", null);
            if (radius_flag != null && radius_flag.equals("ON")) {
                startRadiusMonitoring(mContext);
            }

        }
        //startRadiusMonitoring(mContext);
    }

    //Blocked for SDK
    /*public void startSonicMonitoring(Context context) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
		String location = pref.getString("parent_location_status", "1");
		if (location != null && location.equals("1")) {
			String sonicAPIKey = pref.getString("sonicAPIKey", "0");
			if (sonicAPIKey != null && sonicAPIKey.length() > 0
					&& !sonicAPIKey.equals("0")) {
				Logger.i("ProximityController", "Starting Sonic with key " + sonicAPIKey);
				Sonic.get()
						.initialize(context, (SonicClient) this, sonicAPIKey);
				Sonic.get().start();
				if (!pref.getBoolean("audioscan", true))
					Sonic.get().setForceAudioOff(true);
				else
					Sonic.get().setForceAudioOff(false);
			}
		}
	}*/

    //Blocked for SDK
    /*public void stopSonicMonitoring() {
        Logger.i("ProximityController", "Stopping Sonic");
		Sonic.get().shutdown();
	}*/

    //Blocked for SDK
    /*public void enableSonicAudioRecording() {
        Logger.i("ProximityController", "from enableSonicAudioRecording::");
		SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
		if (!pref.getBoolean("audioscan", true))
			Sonic.get().setForceAudioOff(true);
		else
			Sonic.get().setForceAudioOff(false);
		Editor editor = pref.edit();
		editor.putString("sonic_stop", "0");
		editor.commit();
	}*/

    //Blocked for SDK
    /*public void disableSonicAudioRecording() {
        Logger.i("ProximityController", "from disableSonicAudioRecording::");

		SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
		if (!pref.getBoolean("audioscan", true))
			Sonic.get().setForceAudioOff(true);
		else
			Sonic.get().setForceAudioOff(false);
		Editor editor = pref.edit();
		editor.putString("sonic_stop", "1");
		editor.commit();
	}*/

    //Blocked for SDK
    private BeaconEventListener beaconListener = new BeaconEventListener() {
        @Override
        public void onBeaconSighting(BeaconSighting arg0) {
            // TODO Auto-generated method stub

            super.onBeaconSighting(arg0);
            processGimbalBeaconSighting(arg0);
        }

    };

    private void processGimbalBeaconSighting(BeaconSighting arg0) {
        Logger.i(TAG + "-proximity sighted for gimbal", arg0.getBeacon()
                .getIdentifier());
        SharedPreferences pref = mContext.getSharedPreferences(
                "emkit_info", 0);
        if (pref.getString("locationsettings", "1").equals("0"))
            return;

        final int rssi = arg0.getRSSI();
        Logger.i(TAG + "-proximity sighted", arg0.getBeacon()
                .getIdentifier());

        final BeaconInitDetails beaconInit = new BeaconInitDetails();
        beaconInit
                .setBattery_level("" + getBatteryValue(arg0.getBeacon().getBatteryLevel()));
        beaconInit.setBeacon_name(arg0.getBeacon().getName());
        beaconInit.setBeacon_rssi("" + rssi);
        beaconInit.setBeacon_state("");
        beaconInit.setBeacon_temp("" + arg0.getBeacon().getTemperature());
        beaconInit.setFirst_sighted("");
        beaconInit.setLast_sighted("" + System.currentTimeMillis());
        beaconInit.setLast_sighted_sent_to_server("");
        beaconInit.setPrevious_rssi("");
        beaconInit.setId(arg0.getBeacon().getIdentifier());
        SharedPreferences beacon_pref = mContext.getSharedPreferences(
                "beacon_info", 0);
        Editor edit = beacon_pref.edit();
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "battery_level", "" + getBatteryValue(arg0.getBeacon().getBatteryLevel()));
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "beacon_name", arg0.getBeacon().getName());
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "beacon_rssi", "" + rssi);
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "beacon_sate", " ");
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "beacon_temp", "" + arg0.getBeacon().getTemperature());
        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "last_sighted", "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + arg0.getBeacon().getIdentifier()
                + "first_sighted", null) == null)
            edit.putString("" + arg0.getBeacon().getIdentifier()
                    + "first_sighted", "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + arg0.getBeacon().getIdentifier()
                + "last_sighted_sent_to_server", null) == null) {
            edit.putString("" + arg0.getBeacon().getIdentifier()
                    + "last_sighted_sent_to_server", "" + 0);
            beaconInit.setLast_sighted_sent_to_server("" + 0);
        } else {

        }

        edit.putString("" + arg0.getBeacon().getIdentifier()
                + "previous_rssi", " ");
        edit.putString("" + arg0.getBeacon().getIdentifier() + "beacon_id",
                arg0.getBeacon().getIdentifier());
        edit.commit();
        GimbalSightings gs = new GimbalSightings();
        gs.setContext(mContext);
        gs.checkIfBeaconPresent(arg0.getBeacon().getIdentifier(), rssi,
                beaconInit);
    }

    private int getBatteryValue(BatteryLevel bl) {
        int result = 1;
        if (bl != null) {
            if (bl.equals(BatteryLevel.HIGH))
                result = 4;
            else if (bl.equals(BatteryLevel.MEDIUM_HIGH))
                result = 3;
            else if (bl.equals(BatteryLevel.MEDIUM_LOW))
                result = 2;
            else if (bl.equals(BatteryLevel.LOW))
                result = 1;
        }
        return result;
    }

    private void processCustomGimbalBeaconSighting(EmkitGimbalBeaconSighting arg0) {
        Logger.i(TAG + "-proximity sighted for gimbal", arg0.getBeaconName());
        SharedPreferences pref = mContext.getSharedPreferences(
                "emkit_info", 0);
        if (pref.getString("locationsettings", "1").equals("0"))
            return;

        final int rssi = Integer.parseInt(arg0.getBeaconRssi());
        Logger.i(TAG + "-proximity sighted", arg0.getBeaconName());

        final BeaconInitDetails beaconInit = new BeaconInitDetails();
        beaconInit
                .setBattery_level("" + arg0.getBatteryLevel());
        beaconInit.setBeacon_name(arg0.getBeaconName());
        beaconInit.setBeacon_rssi("" + rssi);
        beaconInit.setBeacon_state("");
        beaconInit.setBeacon_temp("" + arg0.getBeaconTemp());
        beaconInit.setFirst_sighted("");
        beaconInit.setLast_sighted("" + System.currentTimeMillis());
        beaconInit.setLast_sighted_sent_to_server("");
        beaconInit.setPrevious_rssi("");
        beaconInit.setId(arg0.getBeaconId());
        SharedPreferences beacon_pref = mContext.getSharedPreferences(
                "beacon_info", 0);
        Editor edit = beacon_pref.edit();
        edit.putString("" + arg0.getBeaconId()
                + "battery_level", "" + arg0.getBatteryLevel());
        edit.putString("" + arg0.getBeaconId()
                + "beacon_name", arg0.getBeaconName());
        edit.putString("" + arg0.getBeaconId()
                + "beacon_rssi", "" + rssi);
        edit.putString("" + arg0.getBeaconId()
                + "beacon_sate", " ");
        edit.putString("" + arg0.getBeaconId()
                + "beacon_temp", "" + arg0.getBeaconTemp());
        edit.putString("" + arg0.getBeaconId()
                + "last_sighted", "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + arg0.getBeaconId()
                + "first_sighted", null) == null)
            edit.putString("" + arg0.getBeaconId()
                    + "first_sighted", "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + arg0.getBeaconId()
                + "last_sighted_sent_to_server", null) == null) {
            edit.putString("" + arg0.getBeaconId()
                    + "last_sighted_sent_to_server", "" + 0);
            beaconInit.setLast_sighted_sent_to_server("" + 0);
        } else {

        }

        edit.putString("" + arg0.getBeaconId()
                + "previous_rssi", " ");
        edit.putString("" + arg0.getBeaconId() + "beacon_id",
                arg0.getBeaconId());
        edit.commit();
        GimbalSightings gs = new GimbalSightings();
        gs.setContext(mContext);
        gs.checkIfBeaconPresent(arg0.getBeaconId(), rssi,
                beaconInit);
    }

    private void processGimbalEntryEvent(Visit visit) {
        boolean flag = false;
        Place place = visit.getPlace();
        Logger.i("ProximityController", "on Entry " + place.getName());
        Attributes attr = place.getAttributes();
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(place.getName());
        event.setType("CAME");
        registerGimbalPlaceEvent(event);
        event.setType("AT");
        registerGimbalPlaceEvent(event);
        flag = true;
        try {
            if (flag) {
                List<Geofence> friendGeofences = getFriendGeofences();
                if (friendGeofences != null) {
                    for (Geofence emkitFriendGeofence : friendGeofences) {
                        Logger.i(TAG, "saved geofence " + emkitFriendGeofence.getName());
                        Logger.i(TAG, "entered geofence " + place.getName());
                        if (emkitFriendGeofence.getName().equals(place.getName())) {
                            Logger.i(TAG, "geofence reporting location");
                            //Report user location for find my friend
                            //EmkitInstance.getInstance(EmkitInstance.setContext()).reportUserLocation(EmkitInstance.setContext(), ProximityController.this, ""+arg0.getLatitude(), ""+arg0.getLongitude());
                            EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                            gps.startUsingGPS();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processGimbalExitEvent(Visit visit) {
        Place place = visit.getPlace();
        Logger.i("ProximityController", "on Exit " + place.getName());
        Attributes attr = place.getAttributes();
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(place.getName());
        event.setType("LEFT");
        registerGimbalPlaceEvent(event);
        try {
            //to check if all the friends geofences are exited, then stop gps
            //Open out the below after checking the friends geofences
            SharedPreferences sightpref = mContext.getSharedPreferences("sightpref", 0);
            //check if the entries in this pref matching with friend geofences are having only LEFT value. In that case, stop the gps service
            boolean presentFlag = false;
            List<Geofence> friendGeofences = getFriendGeofences();
            if (friendGeofences != null) {
                for (Geofence emkitFriendGeofence : friendGeofences) {
                    String didExit = sightpref.getString(emkitFriendGeofence.getName(), null);
                    if (didExit != null) {
                        if (didExit.equalsIgnoreCase("Departed")) {
                            presentFlag = false;
                        } else {
                            presentFlag = true;
                            break;
                        }
                    }
                }
            }
            if (!presentFlag) {
                EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                gps.stopUsingGPS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processCustomGimbalEntryEvent(EmkitGimbalVisit visit) {
        boolean flag = false;
        //Place place = visit.getName();
        Logger.i("ProximityController", "on Entry " + visit.getName());
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(visit.getName());
        event.setType("CAME");
        registerGimbalPlaceEvent(event);
        event.setType("AT");
        registerGimbalPlaceEvent(event);
        flag = true;
        try {
            if (flag) {
                List<Geofence> friendGeofences = getFriendGeofences();
                if (friendGeofences != null) {
                    for (Geofence emkitFriendGeofence : friendGeofences) {
                        Logger.i(TAG, "saved geofence " + emkitFriendGeofence.getName());
                        Logger.i(TAG, "entered geofence " + visit.getName());
                        if (emkitFriendGeofence.getName().equals(visit.getName())) {
                            Logger.i(TAG, "geofence reporting location");
                            //Report user location for find my friend
                            //EmkitInstance.getInstance(EmkitInstance.setContext()).reportUserLocation(EmkitInstance.setContext(), ProximityController.this, ""+arg0.getLatitude(), ""+arg0.getLongitude());
                            EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                            gps.startUsingGPS();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processCustomGimbalExitEvent(EmkitGimbalVisit visit) {
        Logger.i("ProximityController", "on Entry " + visit.getName());
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(visit.getName());
        event.setType("LEFT");
        registerGimbalPlaceEvent(event);
        try {
            //to check if all the friends geofences are exited, then stop gps
            //Open out the below after checking the friends geofences
            SharedPreferences sightpref = mContext.getSharedPreferences("sightpref", 0);
            //check if the entries in this pref matching with friend geofences are having only LEFT value. In that case, stop the gps service
            boolean presentFlag = false;
            List<Geofence> friendGeofences = getFriendGeofences();
            for (Geofence emkitFriendGeofence : friendGeofences) {
                String didExit = sightpref.getString(emkitFriendGeofence.getName(), null);
                if (didExit != null) {
                    if (didExit.equalsIgnoreCase("Departed")) {
                        presentFlag = false;
                    } else {
                        presentFlag = true;
                        break;
                    }
                }
            }
            if (!presentFlag) {
                EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                gps.stopUsingGPS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Blocked for SDK
    @Override
    public void onVisitStart(Visit visit) {
        processGimbalEntryEvent(visit);
    }

    @Override
    public void onVisitEnd(Visit visit) {
        processGimbalExitEvent(visit);
    }

    public int getEventSize() {
        Logger.i("ProximityController", "" + al.size());
        return al.size();
    }

    public void didBeginVisit(HashMap<String, String> hashMap) {
        EmkitGimbalVisit visit = new EmkitGimbalVisit();
        //add the items from hashmap to Visit object
        Set s = hashMap.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().equalsIgnoreCase("place_name"))
                visit.setName(pairs.getValue().toString());
        }
        processCustomGimbalEntryEvent(visit);
    }

    public void didEndVisit(HashMap<String, String> hashMap) {
        EmkitGimbalVisit visit = new EmkitGimbalVisit();
        //add the items from hashmap to Visit object
        Set s = hashMap.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().equalsIgnoreCase("place_name"))
                visit.setName(pairs.getValue().toString());
        }
        processCustomGimbalExitEvent(visit);
    }

    public void didBeaconSighting(HashMap<String, String> hashMap) {
        EmkitGimbalBeaconSighting arg0 = new EmkitGimbalBeaconSighting();
        //add the items from hashmap to BeaconSighting object
        Set s = hashMap.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().equalsIgnoreCase("beacon_name"))
                arg0.setBeaconName(pairs.getValue().toString());
            if (pairs.getKey().toString().equalsIgnoreCase("battery_level"))
                arg0.setBatteryLevel(pairs.getValue().toString());
            if (pairs.getKey().toString().equalsIgnoreCase("beacon_rssi"))
                arg0.setBeaconRssi(pairs.getValue().toString());
            if (pairs.getKey().toString().equalsIgnoreCase("beacon_temp"))
                arg0.setBeaconTemp(pairs.getValue().toString());
            if (pairs.getKey().toString().equalsIgnoreCase("beacon_id"))
                arg0.setBeaconId(pairs.getValue().toString());
        }
        //processCustomGimbalBeaconSighting(arg0);
        int rssi = (arg0.getBeaconRssi() != null) ? Integer.parseInt(arg0.getBeaconRssi()) : 0;
        int batteryLevel = (arg0.getBatteryLevel() != null) ? Integer.parseInt(arg0.getBatteryLevel()) : 0;
        buildBeaconDataforRadius(arg0.getBeaconId(), rssi, batteryLevel);
    }

    public void didBeginVisit(Visit visit) {
        processGimbalEntryEvent(visit);
    }

    public void didEndVisit(Visit visit) {
        processGimbalExitEvent(visit);
    }

    public void didBeaconSighting(BeaconSighting arg0) {
        processGimbalBeaconSighting(arg0);
    }

    public void registerGimbalPlaceEvent(GimbalPlaceEvent event) {

        while (getEventSize() >= MAX_NUM_EVENTS) {
            al.removeLast();
        }
        al.add(event);

        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String alert = pref.getString("alert_no", "0");
        String status = pref.getString("optin_oked", "-1");
        String custom_optin = pref.getString("custom_optin", null);
        if (custom_optin == null || custom_optin.equals("ON"))// check to
            // proceed
            // without
            // showing optin
            // dialog
            status = "1";
        /*
         * if (status != null && status.equals("-1")) alert = "0";
		 */
		/*
		 * if(alert != null && alert.equals("0")) {
		 */
        /*if (status != null && status.equals("-1")) {
            Intent intent = new Intent(mContext, OptInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }*/
		/*
		 * InternalLogEventNotifier notify = (InternalLogEventNotifier)
		 * context.getApplicationContext(); notify.showOptIn();
		 */
        // alert_no = 1;

		/*
		 * Editor editor = pref.edit(); editor.putString("alert_no", "1");
		 * editor.commit(); } else
		 */
        {
            // SharedPreferences pref =
            // context.getSharedPreferences("emkit_info", 0);
            // String status = pref.getString("optin_oked", "-1");
            if (status != null && status.equals("1")) {
                while (!al.isEmpty()) {
                    try {
                        GimbalPlaceEvent gEvent = al
                                .getLast();
                        Logger.i("ProximityController",
                                "gimbal place:: " + gEvent.getName());
                        Editor editor1 = pref.edit();
                        editor1.putString("placename", gEvent.getName());
                        editor1.putString("eventtype", gEvent.getType());
                        editor1.commit();
                        GimbalSightings gs = new GimbalSightings();
                        gs.setContext(mContext);
                        gs.contentEvent();
                        // notify.sendEvent(context.getApplicationContext(),
                        // hm);
                        al.removeLast();
                    } catch (Exception e) {
                        Logger.i("ProximityController", "exception while iterating");
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public void didDetermineStateForGeofence(int arg0,
                                             ProximityKitGeofenceRegion arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void didEnterGeofence(ProximityKitGeofenceRegion arg0) {
        // TODO Auto-generated method stub
        boolean flag = false;
        String value = arg0.getName();
        Logger.i("Proximity ", "radius geofence is " + value);
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(value);
        event.setType("CAME");
        registerGimbalPlaceEvent(event);
        event.setType("AT");
        registerGimbalPlaceEvent(event);
        flag = true;
        try {
            if (flag) {
                List<Geofence> friendGeofences = getFriendGeofences();
                if (friendGeofences != null) {
                    for (Geofence emkitFriendGeofence : friendGeofences) {
                        Logger.i(TAG, "saved geofence " + emkitFriendGeofence.getName());
                        Logger.i(TAG, "entered geofence " + value);
                        if (emkitFriendGeofence.getName().equals(value)) {
                            Logger.i(TAG, "geofence reporting location");
                            //Report user location for find my friend
                            //EmkitInstance.getInstance(EmkitInstance.setContext()).reportUserLocation(EmkitInstance.setContext(), ProximityController.this, ""+arg0.getLatitude(), ""+arg0.getLongitude());
                            EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                            gps.startUsingGPS();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*if (arg0 != null && arg0.getAttributes() != null) {
			for (Map.Entry<String,String> entry : arg0.getAttributes().entrySet()) {
				  String key = entry.getKey();
				  String value = entry.getValue();
				  Logger.i("Proximity radius geofence attribute ", key +" is "+value);
				  GimbalPlaceEvent event = new GimbalPlaceEvent();
				  event.setName(value);
				  event.setType("CAME");
				  registerGimbalPlaceEvent(event);
				  event.setType("AT");
				  registerGimbalPlaceEvent(event);
				  flag = true;
				  if (flag) {
					  ArrayList<EmkitFriendGeofence> friendGeofences = getFriendGeofences();
					  for (EmkitFriendGeofence emkitFriendGeofence : friendGeofences) {
						  if (emkitFriendGeofence.getName().equals(value)) {
							  //Report user location for find my friend
							  System.out.println("Matching the friend geofence. Report the location");
						  }
					  }
				  }
			}
		}*/

    }

    @Override
    public void didExitGeofence(ProximityKitGeofenceRegion arg0) {
        // TODO Auto-generated method stub
        //Logger.i("Proximity radius geofence ", ""+arg0.getAttributes());
        String value = arg0.getName();
        Logger.i("Proximity ", "radius geofence name is " + value);
        GimbalPlaceEvent event = new GimbalPlaceEvent();
        event.setName(value);
        event.setType("LEFT");
        registerGimbalPlaceEvent(event);

        try {
            //to check if all the friends geofences are exited, then stop gps
            //Open out the below after checking the friends geofences
            SharedPreferences sightpref = mContext.getSharedPreferences("sightpref", 0);
            //check if the entries in this pref matching with friend geofences are having only LEFT value. In that case, stop the gps service
            boolean presentFlag = false;
            List<Geofence> friendGeofences = getFriendGeofences();
            for (Geofence emkitFriendGeofence : friendGeofences) {
                String didExit = sightpref.getString(emkitFriendGeofence.getName(), null);
                if (didExit != null) {
                    if (didExit.equalsIgnoreCase("Departed")) {
                        presentFlag = false;
                    } else {
                        presentFlag = true;
                        break;
                    }
                }
            }
            if (!presentFlag) {
                EmkitGPSTracker gps = EmkitGPSTracker.getInstance(mContext.getApplicationContext());
                gps.stopUsingGPS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void didFailSync(Exception arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void didSync() {
        // TODO Auto-generated method stub

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<ProximityKitBeacon> arg0,
                                        ProximityKitBeaconRegion arg1) {
        // TODO Auto-generated method stub
        if (arg0.size() == 0) {
            return;
        }

        for (ProximityKitBeacon beacon : arg0) {
            Logger.i("Radius Utility", "I have a beacon with data: " + beacon
                    + " attributes=" + beacon.getAttributes());

            // We've wrapped up further behavior in some internal helper methods
            // Check their docs for details on additional things which you can
            // do we beacon data
            Logger.i(
                    "Radius Utility",
                    "didRangeBeaconsInRegion " + beacon.getId1() + "-"
                            + beacon.getId2() + "-" + beacon.getId3());
            String beaconId = beacon.getId1() + "-" + beacon.getId2() + "-"
                    + beacon.getId3();
            int batteryLevel = extractBatteryLevel(beacon);
            Logger.i("Radius Utility", " batteryLevel " + batteryLevel);
            buildBeaconDataforRadius(beaconId.toUpperCase(), beacon.getRssi(), batteryLevel);
        }
    }

    private static final int ALT_BEACON_TYPE_CODE = 0xbeac;
    private static final int ALT_BEACON_BATTERY_LEVEL_INDEX = 0;

    private static int extractBatteryLevel(ProximityKitBeacon beacon) {
        Logger.i("Radius Utility", " beacontypecode " + beacon.getBeaconTypeCode());
        if (beacon.getBeaconTypeCode() != ALT_BEACON_TYPE_CODE) {
            return 0;
        }
        for (Long item : beacon.getDataFields()) {
            Logger.i("Radius Utility", " datafields " + item.longValue());
        }
        Long batteryLevel = beacon.getDataFields().get(ALT_BEACON_BATTERY_LEVEL_INDEX);
        Logger.i("Radius Utility", " batteryLevel after " + batteryLevel);
        return (batteryLevel == null) ? null : batteryLevel.intValue();
    }


    @Override
    public void didDetermineStateForRegion(int arg0,
                                           ProximityKitBeaconRegion arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void didEnterRegion(ProximityKitBeaconRegion arg0) {
        // TODO Auto-generated method stub
        Logger.i("ProximityController", "region " + arg0.getName());
        Logger.i("ProximityController", "region " + arg0.getUniqueId());
        Logger.i("ProximityController", "region " + arg0.getId1());
        Logger.i("ProximityController", "region " + arg0.getId2());
        Logger.i("ProximityController", "region " + arg0.getId3());
		/*int notificationType = (int) System
				.currentTimeMillis();
		PendingIntent pendingIntent = PendingIntent
				.getActivity(mContext, notificationType,
						new Intent(), 0);
		 SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
	     int notificationResource = pref.getInt("EmkitNotificationResource", 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext)
				.setContentTitle("Entered geofence")
				.setContentText(arg0.getName())
				.setAutoCancel(true)
				//.setLargeIcon(icon)
				.setSmallIcon(notificationResource)
				.setContentIntent(pendingIntent)
				.setWhen(System.currentTimeMillis())
				// .setTicker("Card Notification")
				 .setStyle(new NotificationCompat.BigTextStyle()
                   .bigText(arg0.getUniqueId()))
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setVibrate(new long[] { 1000 });
		// Logger.d(TAG,"7");
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Logger.d(TAG,"8");
		notificationManager.notify(notificationType,
				mBuilder.build());*/
    }

    @Override
    public void didExitRegion(ProximityKitBeaconRegion arg0) {
        // TODO Auto-generated method stub

    }

    //Blocked for SDK
	/*@Override
	public void cacheOfflineContent(Sonic arg0, List<SonicContent> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletedContentWithIdentifier(Integer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didCompleteRegistration(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean didHearCode(Sonic arg0, SonicCodeHeard codeHeard) {
		// TODO Auto-generated method stub

		if (codeHeard instanceof SonicAudioCodeHeard
				&& ((SonicAudioCodeHeard) codeHeard).getCustomPayload() != -1) {
			Logger.i("ProximityController", "Received Custom Payload with code "
					+ codeHeard.getBeaconCode() + " and custom payload "
					+ ((SonicAudioCodeHeard) codeHeard).getCustomPayload());

		} else {
			Logger.i("ProximityController", String.format("Did hear code %s",
					codeHeard.getSignalType()));

			if (codeHeard.getSignalType().equalsIgnoreCase("BLUETOOTH"))
				buildBeaconDataforSonic("B" + codeHeard.getBeaconCode(), "AT",
						codeHeard.getRssi());
			else
				buildBeaconDataforSonic("A" + codeHeard.getBeaconCode(), "AT",
						-50);
		}

		return false;
	}

	@Override
	public Boolean didHearCode(Sonic arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void didReceiveActivations(Sonic arg0, List<SonicActivation> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didStatusChange(SonicSdkStatus arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void geoFenceEntered(SonicLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void geoFenceExited(SonicLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void geoFencesUpdated(List<SonicLocation> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getTagsForCode(SonicCodeHeard arg0) {
		// TODO Auto-generated method stub
		return null;
	}*/

    @Override
    public void onLocationAppSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationAppError() {
        // TODO Auto-generated method stub

    }

    private void startRadiusMonitoring(Context context) {
        //System.out.println("Manimaran radius is going to start");
        mContext = context;
        if (!isRadiusRunning) {
            try {
                synchronized (pkManagerLock) {
                    if (pkManager == null) {
                        pkManager = ProximityKitManager.getInstance(context.getApplicationContext(),
                                loadConfig());
                    }
                }
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }

            // For debugging
            //pkManager.debugOn();
	
			/*com.radiusnetworks.proximity.beacon.BeaconManager beaconManager = pkManager
					.getBeaconManager();*/
            // Alt-Beacon format
			/*beaconManager
					.getBeaconParsers()
					.add(new BeaconParser()
							.setBeaconLayout("m:2-5=c0decafe,i:6-13,i:14-17,p:18-18,d:19-22,d:23-26"));*/
            // Eddystone format
	
			/*beaconManager.getBeaconParsers().add(
					new BeaconParser()
							.setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));*/

            //Setting scan periods
            //beaconManager.setBackgroundBetweenScanPeriod(30000);
            if (PROXIMITY_RADIUS_GEO != null && PROXIMITY_RADIUS_GEO.equals("1")) {
                if (servicesConnected()) {
                    // if (true) {
                    // As a safety mechanism, `enableGeofences()` throws a checked
                    // exception in case the
                    // app does not properly handle Google Play support.
                    try {
                        pkManager.enableGeofences();

                        pkManager.setProximityKitGeofenceNotifier(this);
                    } catch (GooglePlayServicesException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (PROXIMITY_RADIUS_BLE != null && PROXIMITY_RADIUS_BLE.equals("1")) {
                pkManager.setProximityKitSyncNotifier(this);
                pkManager.setProximityKitMonitorNotifier(this);
                pkManager.setProximityKitRangeNotifier(this);
            }
            startRadiusManager();
            isRadiusRunning = true;
        } else {
            //Radius is already running
        }
        //startRadiusManager();
    }

    private void stopRadiusMonitoring() {
        isRadiusRunning = false;
        stopRadiusManager();
    }

    private void startRadiusManager() {
        pkManager.start();
    }

    private void stopRadiusManager() {
        pkManager.stop();
    }

    private KitConfig loadConfig() {
        Properties properties = new Properties();
        try {
            properties.load(mContext.getAssets()
                    .open("ProximityKit.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load properties file!",
                    e);
        }
        return new KitConfig(properties);
    }

    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);

        if (ConnectionResult.SUCCESS == resultCode) {
            Logger.i("Radius Utility", "Google Play services available");
            return true;
        }

        // Taking the easy way out: log it. Then let Google Play generate the
        // appropriate action
        Logger.i("Radius Utility",
                GooglePlayServicesUtil.getErrorString(resultCode));
        PendingIntent nextAction = GooglePlayServicesUtil
                .getErrorPendingIntent(resultCode, mContext,
                        0);

        // Make sure we have something to do
        if (nextAction == null) {
            Logger.i("Radius Utility",
                    "Unable to determine action to handle Google Play Services error.");
        }

        // This isn't a crash worthy event
        try {
            nextAction.send(mContext, 0, new Intent());
        } catch (PendingIntent.CanceledException e) {
            Logger.i("Radius Utility", "Intent was canceled after we sent it.");
        } catch (NullPointerException npe) {
            // Likely on a mod without Google Play but log the exception to be
            // safe
            Logger.i("Radius Utility",
                    "Error occurred when trying to retrieve to Google Play Services.");
            npe.printStackTrace();
            displayFallbackGooglePlayDialog(resultCode);
        }
        return false;
    }

    private void displayFallbackGooglePlayDialog(final int resultCode) {
        final Handler handler = new Handler();
        final long oneSecond = 1000;

        Runnable runnable = new Runnable() {
            public void run() {
                if (mContext == null) {
                    handler.postDelayed(this, oneSecond);
                    return;
                }
                displayGooglePlayErrorDialog(resultCode);
            }
        };
        handler.post(runnable);
    }

    private void displayGooglePlayErrorDialog(int resultCode) {
        if (mContext == null) {
            return;
        }

        try {
            GooglePlayServicesUtil.getErrorDialog(resultCode,
                    (Activity) mContext, 0).show();
        } catch (Exception e) {
            // last resort
            new AlertDialog.Builder(mContext)
                    .setTitle("Missing Google Play Services")
                    .setMessage(
                            "Please visit the Google Play Store and install Google Play Services.")
                    .show();
        }
    }

    //Blocked for SDK
	/*public void buildBeaconDataforSonic(String beaconName, String eventType,
			int beacon_rssi) {
		SharedPreferences pref = EmkitInstance.setContext()
				.getSharedPreferences("emkit_info", 0);
		if (pref.getString("locationsettings", "1").equals("0"))
			return;

		if (!pref.getBoolean("audioscan", true))
			Sonic.get().setForceAudioOff(true);
		else
			Sonic.get().setForceAudioOff(false);
		final int rssi = beacon_rssi;
		Logger.i(TAG + "-proximity sighted", beaconName);

		final BeaconInitDetails beaconInit = new BeaconInitDetails();
		beaconInit.setBattery_level("" + 80);
		beaconInit.setBeacon_name(beaconName);
		beaconInit.setBeacon_rssi("" + rssi);
		beaconInit.setBeacon_state("");
		beaconInit.setBeacon_temp("" + 1000);
		beaconInit.setFirst_sighted("");
		beaconInit.setLast_sighted("" + System.currentTimeMillis());
		beaconInit.setLast_sighted_sent_to_server("");
		beaconInit.setPrevious_rssi("");
		beaconInit.setId(beaconName);
		SharedPreferences beacon_pref = EmkitInstance.setContext()
				.getSharedPreferences("beacon_info", 0);
		Editor edit = beacon_pref.edit();
		edit.putString("" + beaconName + "battery_level", "" + 80);
		edit.putString("" + beaconName + "beacon_name", beaconName);
		edit.putString("" + beaconName + "beacon_rssi", "" + rssi);
		edit.putString("" + beaconName + "beacon_sate", " ");
		edit.putString("" + beaconName + "beacon_temp", "" + 1000);
		edit.putString("" + beaconName + "last_sighted",
				"" + System.currentTimeMillis());
		if (beacon_pref.getString("" + beaconName + "first_sighted", null) == null)
			edit.putString("" + beaconName + "first_sighted",
					"" + System.currentTimeMillis());
		if (beacon_pref.getString("" + beaconName
				+ "last_sighted_sent_to_server", null) == null) {
			edit.putString("" + beaconName + "last_sighted_sent_to_server",
					"" + 0);
			beaconInit.setLast_sighted_sent_to_server("" + 0);
		} else {

		}

		edit.putString("" + beaconName + "previous_rssi", " ");
		edit.putString("" + beaconName + "beacon_id", beaconName);
		edit.commit();
		GimbalSightings gs = new GimbalSightings();
		gs.setContext(EmkitInstance.setContext());
		gs.checkIfBeaconPresent(beaconName, rssi, beaconInit);
	}*/

    private void buildBeaconDataforRadius(String beaconName, int beacon_rssi, int batteryLevel) {
        beaconName = "6B3C7521-582B-4CF6-9B63-8A93DA314C35-100-9909";
        SharedPreferences pref = mContext
                .getSharedPreferences("emkit_info", 0);
        if (pref.getString("locationsettings", "1").equals("0"))
            return;
        Logger.i("Utility", "Santhana clean beacon sighted::" + beaconName);
		/*if (beaconName.equalsIgnoreCase(EmkitInstance.beacon_name)) {
			Logger.i("Utility", "Santhana clean happening::"+beaconName);
			return;
		}*/
		/*
		 * if (!pref.getBoolean("locationsettings", false)) return;
		 */
        SharedPreferences beacon_pref = mContext
                .getSharedPreferences("beacon_info", 0);
        Editor edit = beacon_pref.edit();
        long first_millis = Long.parseLong(beacon_pref.getString("" + beaconName + "first_sighted", "0"));
        long first_seconds = TimeUnit.MILLISECONDS.toSeconds(System
                .currentTimeMillis() - first_millis);
        Logger.i("Utility", "Manimaran beacon diff " + first_seconds);
        if (first_seconds >= 300) {
            edit.putString("" + beaconName + "first_sighted",
                    null);
        }

        final int rssi = beacon_rssi;
        Logger.i(TAG + "-proximity sighted", beaconName);

        final BeaconInitDetails beaconInit = new BeaconInitDetails();
        beaconInit.setBattery_level("" + batteryLevel);
        beaconInit.setBeacon_name(beaconName);
        beaconInit.setBeacon_rssi("" + rssi);
        beaconInit.setBeacon_state("");
        beaconInit.setBeacon_temp("" + 1000);
        beaconInit.setFirst_sighted("");
        beaconInit.setLast_sighted("" + System.currentTimeMillis());
        beaconInit.setLast_sighted_sent_to_server("");
        beaconInit.setPrevious_rssi("");
        beaconInit.setId(beaconName);


        edit.putString("" + beaconName + "sub_type", "RadiusBeacon");
        edit.putString("" + beaconName + "battery_level", "" + batteryLevel);
        edit.putString("" + beaconName + "beacon_name", beaconName);
        edit.putString("" + beaconName + "beacon_rssi", "" + rssi);
        edit.putString("" + beaconName + "beacon_sate", " ");
        edit.putString("" + beaconName + "beacon_temp", "" + 1000);
        edit.putString("" + beaconName + "last_sighted",
                "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + beaconName + "first_sighted", null) == null)
            edit.putString("" + beaconName + "first_sighted",
                    "" + System.currentTimeMillis());
        if (beacon_pref.getString("" + beaconName
                + "last_sighted_sent_to_server", null) == null) {
            edit.putString("" + beaconName + "last_sighted_sent_to_server",
                    "" + 0);
            beaconInit.setLast_sighted_sent_to_server("" + 0);
        } else {
			/*
			 * int new_last =
			 * (Integer.parseInt(beacon_pref.getString(""+beaconName
			 * +"last_sighted_sent_to_server", null))+1);
			 * edit.putString(""+beaconName+"last_sighted_sent_to_server", "" +
			 * new_last); beaconInit.setLast_sighted_sent_to_server("" +
			 * new_last);
			 */
        }
        // edit.putString(""+arg0.getBeacon().getIdentifier()+"last_sighted_sent_to_server",
        // " ");
        edit.putString("" + beaconName + "previous_rssi", " ");
        edit.putString("" + beaconName + "beacon_id", beaconName);
        edit.commit();
        GimbalSightings gs = new GimbalSightings();
        gs.setContext(mContext);
        gs.checkIfBeaconPresent(beaconName, rssi, beaconInit);
    }

    public void clearProximityCache(GimbalInternalNotifier notify) {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
        //Blocked for SDK
		/*if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
				&& !gimbalAPIKey.equals("0")) {
			Gimbal.setApiKey((Application) mContext.getApplicationContext(),
					gimbalAPIKey);
		}
		Gimbal.resetApplicationInstanceIdentifier();
		PlaceManager.getInstance().stopMonitoring();
		PlaceManager.getInstance().removeListener(proximityInstance);
		CommunicationManager.getInstance()
				.removeListener(communicationListener);*/
        //Blocked for SDK
		/*stopBeaconManager();
		stopSonicMonitoring();*/
        Editor editor = pref.edit();
        editor.putString("alert_no", "0");
        editor.putString("optin_oked", "0");
        editor.commit();
        // to clear all the shared preferences
        SharedPreferences pref1 = mContext.getSharedPreferences("sightpref", 0);
        Editor edit1 = pref1.edit();
        edit1.clear();
        edit1.commit();
        pref1 = mContext.getSharedPreferences("sight_beacons", 0);
        edit1 = pref1.edit();
        edit1.clear();
        edit1.commit();
        pref1 = mContext.getSharedPreferences("beacon_names", 0);
        edit1 = pref1.edit();
        edit1.clear();
        edit1.commit();
        pref1 = mContext.getSharedPreferences("beacon_info", 0);
        edit1 = pref1.edit();
        edit1.clear();
        edit1.commit();

        notify.onClearingInternalSuccess();
    }

    //Blocked for SDK
	/*public void stopBeaconManager() {
		if (beaconManager != null) {
			beaconManager.stopListening();
		}
	}*/

    //Blocked for SDK
	/*public void stopBeaconListening() {
		// if (beaconManager.isMonitoring())
		{
			stopBeaconManager();
			Sonic.get().stop();
			startSonicMonitoring(mContext);
		}
	}*/

    public void startProximity() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String beacon_services_flag = pref.getString(
                "beacon_services_flag", null);
        if (beacon_services_flag != null
                && beacon_services_flag.equals("ON")) {
            initiateProximity();
        }
    }

    public void startProximityMonitoring() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
        //Blocked for SDK
        if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
                && !gimbalAPIKey.equals("0")) {
            Gimbal.setApiKey((Application) mContext.getApplicationContext(),
                    gimbalAPIKey);
        }
        PlaceManager.getInstance().addListener(proximityInstance);
        PlaceManager.getInstance().startMonitoring();
		/*beaconManager.addListener(beaconListener);
		beaconManager.startListening();
		startSonicMonitoring(mContext);*/
        // SharedPreferences pref = context.getSharedPreferences("emkit_info",
        // 0);
        Editor editor = pref.edit();
        editor.putString("gimbal_stop", "0");
        editor.commit();
        proximityOptInAccepted();
        LocationAsyncTask taskIniteMkit = new LocationAsyncTask(mContext);
        taskIniteMkit.execute("1");
    }

    public void stopProximityMonitoring() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String gimbalAPIKey = pref.getString("gimbalAPIKey", "0");
        //Blocked for SDK
        if (gimbalAPIKey != null && gimbalAPIKey.length() > 0
                && !gimbalAPIKey.equals("0")) {
            Gimbal.setApiKey((Application) mContext.getApplicationContext(),
                    gimbalAPIKey);
        }
        PlaceManager.getInstance().stopMonitoring();
		/*stopBeaconManager();
		stopSonicMonitoring();*/
        stopRadiusMonitoring();
        // SharedPreferences pref = context.getSharedPreferences("emkit_info",
        // 0);
        Editor editor = pref.edit();
        editor.putString("gimbal_stop", "1");
        editor.commit();
        LocationAsyncTask taskIniteMkit = new LocationAsyncTask(mContext);
        taskIniteMkit.execute("0");
    }

    public void proximityOptInAccepted() {

        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("optin_oked", "1");
        editor.commit();

        while (!al.isEmpty()) {
            try {
                GimbalPlaceEvent event = al.getLast();
                Logger.i("ProximityController", "gimbal place:: " + event.getName());
                Editor editor1 = pref.edit();
                editor1.putString("placename", event.getName());
                editor1.putString("eventtype", event.getType());
                editor1.commit();
                GimbalSightings gs = new GimbalSightings();
                gs.setContext(mContext);
                gs.contentEvent();
                al.removeLast();
            } catch (Exception e) {
                Logger.i("ProximityController", "exception while iterating");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // SharedPreferences pref = context.getSharedPreferences("emkit_info",
        // 0);

    }

    public void proximityOptInCancelled() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("optin_cld", "1");
        editor.putString("optin_oked", "0");
        editor.putString("gimbal_stop", "1");
        editor.commit();

        //Blocked for SDK
        PlaceManager.getInstance().stopMonitoring();

        LocationAsyncTask taskIniteMkit = new LocationAsyncTask(mContext);
        taskIniteMkit.execute("0");
    }

    public void initiateProximity() {
        prepareProximityFlags();

        BeaconListAsyncTask beaconList = new BeaconListAsyncTask();
        beaconList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final SharedPreferences pref = mContext
                        .getSharedPreferences("emkit_info", 0);
                String beacon_services_flag = pref.getString(
                        "beacon_services_flag", null);
                //Blocked for SDK
                if (beacon_services_flag != null
                        && beacon_services_flag.equals("ON")) {
                    String gimbal_flag = pref.getString("gimbal_flag", null);
                    if (gimbal_flag != null && gimbal_flag.equals("ON")) {
                        initGimbal((Application) mContext
                                .getApplicationContext());
                    }
                    String sonic_flag = pref.getString("sonic_flag", null);
                    if (sonic_flag != null && sonic_flag.equals("ON")) {
                        //startSonicMonitoring(EmkitInstance.setContext());
                    }
                    String radius_flag = pref.getString("radius_flag", null);
                    if (radius_flag != null && radius_flag.equals("ON")) {
                        startRadiusMonitoring(mContext);
                    }

                }

                //startRadiusMonitoring(EmkitInstance.setContext());
            }

        }, 1000);
    }

    public List<Geofence> getFriendGeofences() {
        SharedPreferences pref = mContext
                .getSharedPreferences("emkit_info", 0);
        String initResponse = pref.getString("emkit_init_response", "");
        if (initResponse.length() > 0) {
            Gson gson = new Gson();
            IniteMkitDetails initObj = gson.fromJson(initResponse, IniteMkitDetails.class);
            FindMyFriend findMyFriend = initObj.getFindMyFriend();
            if (findMyFriend != null)
                return findMyFriend.getGeofences();
        }
        return null;
    }

    @Override
    public void onEmkitReportUserLocationSuccess(String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEmkitReportUserLocationFailure() {
        // TODO Auto-generated method stub

    }

    public void clearBeacon() {
        // TODO Auto-generated method stub

        SharedPreferences sighted_pref1 = mContext.getSharedPreferences(
                "sight_beacons", 0);
        Map<String, ?> allEntries = sighted_pref1.getAll();
        HashSet<String> hs_val = new HashSet<String>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            String beacon_name = entry.getKey();

            SharedPreferences sighted_pref = mContext.getSharedPreferences(
                    "beacon_info", 0);
            Long lastsightedtime = Long.parseLong(sighted_pref.getString(
                    beacon_name + "last_sighted", "0"));

            Logger.i("Utility", "lastsightedtime::" + lastsightedtime);

            Long currenttime = System.currentTimeMillis();
            Long difference = (currenttime - lastsightedtime);
            Logger.i("Utility", "Santhana clean difference::" + difference);
            if (difference > 300000) {
                BeaconHandler beacon = new BeaconHandler();
                beacon.setValues(mContext, "", "", "", "", "", "", "", "", "",
                        "");
                beacon.callDeparture(beacon_name);
                SharedPreferences pref = mContext.getSharedPreferences(
                        "sight_beacons", 0);
                Editor edit = pref.edit();
                edit.remove(beacon_name);
                edit.commit();
                // To remove from sighted list for mydevices section
                SharedPreferences pref1 = mContext.getSharedPreferences(
                        "sightpref", 0);
                Editor edit1 = pref1.edit();
                edit1.remove(beacon_name);
                edit1.commit();
                //SharedPreferences pref2 = context.getSharedPreferences("beacon_info", 0);
                Editor edit2 = sighted_pref.edit();
                //edit2.remove(beacon_name+"beacon_id");
                //edit2.remove(beacon_name+"dwell_time");
				/*edit2.remove(beacon_name+"min_rssi");
				edit2.remove(beacon_name+"max_rssi");*/
                //edit2.remove(beacon_name+"relay_time_interval");
                //edit2.remove(beacon_name+"max_sighting_interval");
                edit2.remove(beacon_name + "first_sighted");
                edit2.remove(beacon_name + "last_sighted_sent_to_server");
                edit2.commit();
            }
        }
        //Geofence safe depart checking <START>
        SharedPreferences pref_geo = mContext.getSharedPreferences(
                "sightpref", 0);
        Editor geo_edit = pref_geo.edit();
        Map<String, ?> geoEntries = pref_geo.getAll();

        for (Map.Entry<String, ?> entry : geoEntries.entrySet()) {
            String geo_name = entry.getKey();
            Logger.i("Utility", "geo_fence entry saved::" + geo_name);
            if (geo_name.indexOf("_sighted_time") != -1) {
                Long geo_last_sighted = Long.parseLong(pref_geo.getString(
                        geo_name, "0"));

                Logger.i("Utility", "geo_last_sighted::" + geo_last_sighted);

                Long currenttime = System.currentTimeMillis();
                Long geo_difference = (currenttime - geo_last_sighted);
                Logger.i("Utility", "geo_difference::" + geo_difference);
                if (!geo_last_sighted.equals("0") && geo_difference > 86400000) {//24 hours - 86400000
                    String temp_geo_name = geo_name.substring(0, geo_name.indexOf("_sighted_time"));
                    Logger.i("Utility", "geo_departing softly::" + temp_geo_name);
                    geo_edit.putString(temp_geo_name, "Departed");
                    geo_edit.putString(geo_name, "0");
                    geo_edit.commit();
                    boolean presentFlag = false;
                    List<Geofence> friendGeofences = ProximityController.getInstance(mContext).getFriendGeofences();
                    for (Geofence emkitFriendGeofence : friendGeofences) {
                        Logger.i("Utility", "geo_iterating friend geofence::" + emkitFriendGeofence.getName());
                        String didExit = pref_geo.getString(emkitFriendGeofence.getName(), null);
                        Logger.i("Utility", "geo_present friend geofence::" + didExit);
                        if (didExit != null) {
                            if (didExit.equalsIgnoreCase("Sighted")) {
                                presentFlag = true;
                                break;
                            } else {
                                presentFlag = false;
                            }
                        }
                    }
                    if (!presentFlag)
                        EmkitGPSTracker.getInstance(mContext).stopUsingGPS();
                }
            }
        }

        //Geofence safe depart checking <END>
        checkGimbalMonitoring();
    }

    public void checkGimbalMonitoring() {
        SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String beacon_services_flag = pref.getString(
                "beacon_services_flag", null);
        if (beacon_services_flag != null
                && beacon_services_flag.equals("ON")) {
            ProximityController.getInstance(mContext).assureProximityMonitoring();
        }
    }

    public void stopBeaconListening() {
        // TODO Auto-generated method stub
        //Blocked for SDK
		/*ProximityController.getInstance(context).stopBeaconListening();*/
    }

}
