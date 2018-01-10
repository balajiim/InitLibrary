/**
 * Copyright Venuetize 2017
 * <EmkitInitialization>
 * created by Venuetize
 */
package com.venue.initv2;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.venue.initv2.utility.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class EmkitInitialization implements ActivityLifecycleCallbacks {
    private String[] deepLinkArray;

    private static final String TAG = "EmkitInitialization";

    private Context mContext;
    public static Context tempContext;
    private ProgressDialog mProgressDialog = null;
    private boolean closeFlag = false;
    public static Context baseContext;

    private static final String EMKIT_USERID = "emp2UserId";

    public static String SENDER_ID;

    public static boolean appPaused;

    public static boolean appResumed;

    public static int isStopped;

    public static int isResumed;

    private static final String SERVER_URL_CONFIG_KEY = "server_url";

    private CloseNotifier closeNotify;


    protected EmkitInitialization(Context context) {
        tempContext = context;
        isStopped = 0;
        isResumed = 0;
        appPaused = false;
        appResumed = false;

        GATrackerEmkit.init(context.getApplicationContext());
        closeFlag = false;
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
        new FontFetchAsync().execute();
        deepLinkArray = context.getResources().getStringArray(R.array.emkit_deeplinks);

        //prepareGCMRegistration();
    }

    private void prepareGCMRegistration(Context con) {
        //To perform this operation if registration id is not there
        /*if (getDeviceId() != null && getDeviceId().trim().length() > 0) {
            try {
                if (tempContext instanceof Activity)
                    registerWithGCM((Activity) tempContext, tempContext.getResources().getString(R.string.emkit_vendor_gcm_senderid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        Logger.i(TAG, "starting gcm registration");
        tempContext = con;
        //if (tempContext instanceof Activity)
        //registerWithGCM(tempContext, tempContext.getResources().getString(R.string.emkit_vendor_gcm_senderid));
        //Firebase impl
        startEmkitWithValues();
        //EmVendorGigya.getInstance(tempContext).initializeGigya();
    }

    public void startEmkitWithValues() {
        registereMkitAPIKey(tempContext, tempContext.getResources().getString(R.string.emkitAPIKey));
        registerBeaconServicesFlag(tempContext, tempContext.getResources().getString(R.string.emkit_beacon_services_flag));
        registerGimbalFlag(tempContext, tempContext.getResources().getString(R.string.emkit_gimbal_flag));
        registerSonicFlag(tempContext, tempContext.getResources().getString(R.string.emkit_sonic_flag));
        registerRadiusFlag(tempContext, tempContext.getResources().getString(R.string.emkit_radius_flag));
        registerCustomOptInFlag(tempContext, tempContext.getResources().getString(R.string.emkit_custom_optin_flag));
        registerGimbalAPIKey(tempContext, tempContext.getResources().getString(R.string.emkit_gimbalAPIKey));
        registerSonicAPIKey(tempContext, tempContext.getResources().getString(R.string.emkit_sonicAPIKey));
        registerExternalRefId(tempContext, tempContext.getResources().getString(R.string.emkit_external_refid));
        initializeLogInstance(tempContext.getApplicationContext());
        firstLaunch(tempContext);
    }

    public void setNotificationIcon(int notificationResource) {
        Logger.i(TAG, "setNotification icon" + notificationResource);
        SharedPreferences emkitpref = tempContext.getSharedPreferences("emkit_info", 0);
        SharedPreferences.Editor emkiteditor = emkitpref.edit();
        emkiteditor.putInt("EmkitNotificationResource", notificationResource);
        emkiteditor.commit();
    }

    public void firstLaunch(Context context) {
        //Any other init to be done
    }

    public void deviceId(String deviceid) {
        SharedPreferences pref = tempContext.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("device_id", deviceid);
        editor.commit();
        // SaveSharedPrefrences(context, "device_id", deviceid);
    }

    public String getDeviceId() {
        SharedPreferences pref = tempContext.getSharedPreferences("emkit_info", 0);
        return pref.getString("device_id", null);
    }

    public void registereMkitAPIKey(Context context, String eMkitAPIKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("eMkitAPIKey", eMkitAPIKey);
        editor.commit();
    }

    public void registerExternalRefId(Context context, String nflKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("external_ref_id", nflKey);
        editor.commit();
    }

    public void registerBeaconServicesFlag(Context context, String beaconFlag) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("beacon_services_flag", beaconFlag);
        editor.commit();
    }

    public void registerGimbalFlag(Context context, String nflKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("gimbal_flag", nflKey);
        editor.commit();
    }

    public void registerSonicFlag(Context context, String sonicFlag) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("sonic_flag", sonicFlag);
        editor.commit();
    }

    public void registerRadiusFlag(Context context, String radiusFlag) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("radius_flag", radiusFlag);
        editor.commit();
    }

    public void registerCustomOptInFlag(Context context, String nflKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("custom_optin", nflKey);
        editor.commit();
    }

    public void registerGimbalAPIKey(Context context, String gimbalAPIKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("gimbalAPIKey", gimbalAPIKey);
        editor.commit();
    }

    public void registerSonicAPIKey(Context context, String sonicAPIKey) {
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("sonicAPIKey", sonicAPIKey);
        editor.commit();
    }

    public void registerWithGCM(Context activity, String gcm_senderid) {
        // GCM INTEGRATION START
        SENDER_ID = gcm_senderid;
        EmkitGCMClientManager pushClientManager = new EmkitGCMClientManager(activity, SENDER_ID);
        pushClientManager.registerIfNeeded(new EmkitGCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Logger.d("Registration id", registrationId);
                //send this registrationId to your server
                startEmkitWithValues();
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                startEmkitWithValues();
            }
        });
        // GCM INTEGRATION END
    }


    public void checkGimbalPresence() {
        //implement the gimbal presence logic from dex
    }

    public void initializeLogInstance(Context con) {
        // TODO Auto-generated method stub

        GATrackerEmkit.init(con);
    }

    public void startLogInstance() {
    }

    public void resumeLogInstance() {
        // TODO Auto-generated method stub
        GATrackerEmkit.logEvent("eMKitResume", "eMKitResume", "resume_app",
                null);
    }

    public void stopLogInstance() {
        // TODO Auto-generated method stub
        GATrackerEmkit.logEvent("eMKitStop", "eMKitStop", "stop_app", null);
    }

    public void terminateLogInstance() {
        // TODO Auto-generated method stub
        GATrackerEmkit.logEvent("eMKitTerminate", "eMKitTerminate",
                "terminate_app", null);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Logger.i(
                TAG, "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // TODO Auto-generated method stub
        Logger.i(
                TAG, "onActivityStarted");
        startLogInstance();
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // TODO Auto-generated method stub
        Logger.i(
                TAG, "onActivityDestroyed");
        terminateLogInstance();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // TODO Auto-generated method stub

        Logger.i(
                TAG, "onActivityPaused");
        //isPaused++;
        appPaused = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // TODO Auto-generated method stub
        Logger.i(
                TAG, "onActivityResumed");
        isResumed++;
        appPaused = false;
        initializeLogInstance(tempContext.getApplicationContext());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // TODO Auto-generated method stub
        Logger.i(
                TAG, "onActivityStopped");
        stopLogInstance();
        isStopped++;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    public boolean isApplicationInForeground() {
        //System.out.println("Manimaran isResumed "+isResumed);
        //System.out.println("Manimaran isStopped "+isStopped);
        return isResumed > isStopped;
    }

    public boolean isApplicationInBackground() {
        return appPaused;
    }


    private String[] listAssetFiles(String path) {

        String[] list;
        try {

            list = tempContext.getAssets().list(path);

        } catch (IOException e) {
            return null;
        }

        return list;
    }

    class FontFetchAsync extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {
            loadFonts();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Hashtable<String, Object> fontHash = new Hashtable<String, Object>();

                String[] fontArray = listAssetFiles("fonts/emkitfonts");

                Logger.i(TAG, "Total Fonts " + fontArray.length);
                for (int i = 0; i < fontArray.length; i++) {
                    String file = fontArray[i];// To assign the font file
                    Logger.i(TAG, "Font asset " + file);
                    String fontType = null;
                    Typeface fontVal = null;
                    try {
                        fontType = "fonts/emkitfonts/" + file.substring(0, file.lastIndexOf("."));

                        if (file != null && file.trim().length() > 0
                                && !fontHash.containsKey(fontType)) {

                            fontVal = Typeface.createFromAsset(
                                    tempContext.getAssets(), "fonts/emkitfonts/" + file);
                            fontHash.put(fontType, fontVal);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    file = null;
                    fontType = null;
                    fontVal = null;
                }

                EmkitInstance.setHashTable(fontHash);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public void cleanUpEmkit(Context context) {

        Logger.i(TAG, "cleanUpEmkit");
        cleanCardList(context);
        clearEmkitData(context);
        //cleaning thirdparty items
        EmkitGPSTracker.getInstance(context).stopUsingGPS();
        // stopProximity();
        // clearProximityRecords();
    }

    public void clearEmkitData(Context ctx) {
        //Logic to clear the emp2userid
        Logger.i(TAG, "cleanUpEmkit4");
        SharedPreferences pre = ctx.getSharedPreferences("emkit_info",
                Context.MODE_PRIVATE);
        Editor edit = pre.edit();
        edit.putString(EMKIT_USERID, null);
        edit.commit();
        Logger.i(TAG, "cleanUpEmkit5 1");
        SharedPreferences pref = ctx.getSharedPreferences("emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("carparking_latitude", "" + 0.0);
        editor.putString("carparking_longitude", "" + 0.0);
        editor.putString("nearmeData", "");
        editor.putBoolean("isMyCarLocationAvailable", false);
        editor.commit();
        SharedPreferences pref4 = ctx.getSharedPreferences("emkit_info", 0);
        Map<String, ?> keys = pref4.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Logger.d("the all shared mapkey values", entry.getKey());
            if (entry.getKey().startsWith("mapupdatedtime")) {
                Editor removeeditor = pref4.edit();
                removeeditor.remove(entry.getKey());
                removeeditor.apply();
                Logger.i(TAG, "the filter key is " + entry.getKey());

            }
        }
        File[] files = ctx.getFilesDir().listFiles();

        for (File file : files) {
            Logger.i(TAG, "the files are deleting::" + file);
            Logger.i(TAG, "cleanUpEmkit7");
            file.delete();
        }
    }

    public void cleanCardList(Context context) {

        /*Logger.i(TAG, "emkitLogout contextis::" + context);
        PackageManager m = context.getPackageManager();
        String path = context.getPackageName();
        String cardpath = "";
        String fileName = context.getResources().getString(
                R.string.my_cards_file_name);

        try {

            PackageInfo p = m.getPackageInfo(path, 0);
            path = p.applicationInfo.dataDir;
            // Logger.d(TAG,"path " + path);
            cardpath = path + "/" + fileName + ".txt";

        } catch (NameNotFoundException e) {
            // Logger.d(TAG,"Error Package name not found: " + e);
        }
        File file = new File(cardpath);
        if (file.delete()) {
            // Logger.d(TAG,file.getName() + " is deleted!");
        } else {
            // Logger.d(TAG,"Delete operation is failed.");
        }
        try {
            CleanCardNotifier ccn;
            ccn = (CleanCardNotifier) context;
            ccn.cleanCardSuccess();
        } catch (Exception e) {
        }*/
    }

    private static String SHAHash;
    public static Typeface MontserratBd;
    public static Typeface MontserratRg;
    public static Typeface RobotoBd;
    public static Typeface RobotoRg;
    public static Typeface htfbolditalic;
    public static Typeface htfbookcondenced;
    public static Typeface htfMedium;
    public static Typeface htfMediumCondenced;
    public static Typeface UniversLTStdBoldCn;
    public static Typeface UniversLTStdCn;

    public static void loadFonts() {
        Logger.e("", "loadFonts");
        MontserratBd = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/Montserrat-Bold");
        MontserratRg = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/Montserrat-Regular");
        RobotoBd = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/Roboto-Bold");
        RobotoRg = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/Roboto-Regular");
        htfbolditalic = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/GothamHTF-BoldItalic");
        htfbookcondenced = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/GothamHTF-BookCondensed");
        htfMedium = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/GothamHTF-Medium");
        htfMediumCondenced = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/emkitfonts/GothamHTF-MediumCondensed");
        UniversLTStdBoldCn = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/AvenirLTStd-Black");
        UniversLTStdCn = (Typeface) EmkitInstance.getFontHash().get(
                "fonts/AvenirLTStd-Medium");


    }

    public void launchDeeplink(Context ctx, String deepLink) {
        //System.out.println("From LaunchDeepLink :: ");
        try {
            if (deepLink != null && deepLink.indexOf("http") != -1) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(deepLink));
                ctx.startActivity(i);
            } else {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setData(Uri.parse(deepLink));
                ctx.startActivity(i);
            }
        } catch (ActivityNotFoundException e) {
        }
    }

    public void launchVideo(Context ctx, String url) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse(url), "video/*");
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public void launchAudio(Context ctx, String url) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse(url), "audio/*");
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }


    public void launchWebView(Context ctx, String url) {
        try {
            openWebView(ctx, url);
        } catch (ActivityNotFoundException e) {
        }
    }

    public void openWebView(Context context, String url) {
        Intent i = new Intent(mContext, EmWebViewActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("web_url", url);
        i.putExtras(bundle);
        context.startActivity(i);
    }

    public void emkitLogEvent(Context context,
                              String campaign_id, String instantpush_id) {
        //postjson will be in the format of EmkitLogEvent class
        /*EmkitLogEvent event = new EmkitLogEvent();
        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        String emp2UserId = pref.getString("emp2UserId", null);
        Logger.i(TAG, "log event the instantpush_id is::" + instantpush_id);
        Logger.i(TAG, "log event the campaign_id is::" + campaign_id);
        event.setEmp2_user_id(emp2UserId);
        if (instantpush_id != null) {
            event.setEventType(context.getResources().getString(R.string.emkit_log_eventType_instant));
            event.setEventValue(instantpush_id);
        }
        if (campaign_id != null) {
            event.setEventType(context.getResources().getString(R.string.emkit_log_eventType_proximity));
            event.setEventValue(campaign_id);
        }
        event.setEventCategory(context.getResources().getString(R.string.emkit_log_eventCategory));


        Gson gson = new Gson();
        String eventJson = gson.toJson(event);
        Logger.i(TAG, "log event the json is::" + eventJson);

        GetLogEventTask().setValues(notifer, context);
        GetLogEventTask().executeTask(eventJson);*/

    }

    public void setFlag(CloseNotifier notify, boolean flag) {
        closeFlag = flag;
        closeNotify = notify;
    }

    public void displayCard(Context context, String cardid) {

        //implement the display card api call
    }

    public String getNotificationStatus() {
        return "1";
    }

}
