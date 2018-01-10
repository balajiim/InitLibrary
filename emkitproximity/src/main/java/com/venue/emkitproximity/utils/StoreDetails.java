
package com.venue.emkitproximity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.venue.emkitproximity.holder.IniteMkitDetails;

public class StoreDetails {

    private static final String USER_INFO = "user-info";

    private static final String EMKIT_USERID = "emp2UserId";
    private static final String URL_CARDDATA = "cardDataURL";
    private static final String URL_SIGHTING = "sightingURL";
    private static final String URL_BEACON = "beaconsURL";
    private static final String EMKIT_VERSION = "eMkitVersionPage";
    private static final String FB_USERID = "fb-userid";
    private static final String FIRST_NOTI = "first_notification";
    private static final String EXTCARDURL = "extCardDataURL";
    private static final String LOCATION_TRACKING = "locationTracking";

    private static final String KEY_INITEMKIT_INFO = "emkit_info";
    private static final String KEY_USER_INFO = "user-info-key";

    private static final String NEW_CARD_DATA_STRUCTURE = "newCardDataStructure";

    public IniteMkitDetails getIniteMkitDetails(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE);

        final String emp2UserId = sharedPreferences.getString(EMKIT_USERID, null);
        final String cardDataURL = sharedPreferences.getString(URL_CARDDATA, null);
        final String sightingURL = sharedPreferences.getString(URL_SIGHTING, null);
        final String beaconsURL = sharedPreferences.getString(URL_BEACON, null);
        final String eMkitVersionPage = sharedPreferences.getString(EMKIT_VERSION, null);
        final String extCardDataURL = sharedPreferences.getString(EXTCARDURL, null);

        IniteMkitDetails initeMkitDetails = null;

        if (emp2UserId != null) {
            initeMkitDetails = new IniteMkitDetails();
            initeMkitDetails.setEmp2UserId(emp2UserId);
            initeMkitDetails.setCardDataURL(cardDataURL);
            initeMkitDetails.setSightingURL(sightingURL);
            initeMkitDetails.setBeaconsURL(beaconsURL);
            initeMkitDetails.seteMkitVersionPage(eMkitVersionPage);
            initeMkitDetails.setExtCardDataURL(extCardDataURL);
        }

        return initeMkitDetails;
    }

    public static String getNewCardDataStructureValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE);
        String newCardDataStructure = sharedPreferences.getString(NEW_CARD_DATA_STRUCTURE, null);

        return newCardDataStructure;
    }

    public static boolean saveIniteMkitDetails(Context context, IniteMkitDetails details) {
        Editor editor = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();

        editor.putString(EMKIT_USERID, details.getEmp2UserId());
        editor.putString(URL_CARDDATA, details.getCardDataURL());
        editor.putString(URL_SIGHTING, details.getSightingURL());
        editor.putString(URL_BEACON, details.getBeaconsURL());
        editor.putString(EMKIT_VERSION, details.geteMkitVersionPage());
        editor.putString(EXTCARDURL, details.getExtCardDataURL());
        return editor.commit();
    }

    public static void clearIniteMkitInfo(Context context) {
        Editor editor = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static boolean saveUserInfo(Context context, String userInfo) {
        Editor editor = context.getSharedPreferences(KEY_USER_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(USER_INFO, userInfo);
        return editor.commit();
    }

    public static String getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_USER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_INFO, "");
    }

    public static void clearUserInfo(Context context) {
        Editor editor = context.getSharedPreferences(KEY_USER_INFO, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static boolean saveLocationTrackingStatus(Context context, boolean status) {
        return saveNotificationStatus(context, LOCATION_TRACKING, status);
    }

    public static boolean getLocationTrackingStatus(Context context) {
        return getNotificationStatus(context, LOCATION_TRACKING);
    }

    private static boolean saveNotificationStatus(Context context, String type, boolean survey) {
        Editor editor = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();
        editor.putBoolean(type, survey);
        return editor.commit();
    }

    private static boolean getNotificationStatus(Context context, String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(type, true);
    }

    public static String getFBUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FB_USERID, "");
    }

    public static boolean setFBUserId(Context context, String userId) {
        Editor editor = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(FB_USERID, userId);
        return editor.commit();
    }

    public static String getFirstNoti(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FIRST_NOTI, null);
    }

    public static boolean setFirstNoti(Context context, String userId) {
        Editor editor = context.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(FIRST_NOTI, userId);
        return editor.commit();
    }

    public boolean saveLogInResExternal(Context context, String saveLogInRes) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getLogInResExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean saveEnteredUserNameExternal(Context context, String username) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getEnteredUserNameExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean saveEnteredPasswordExternal(Context context, String password) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getEnteredPasswordExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean saveUserNameExternal(Context context, String username) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getUserNameExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean savePasswordExternal(Context context, String password) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getPasswordExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean saveUseridExternal(Context context, String userid) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getUseridExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean saveLoyaltyCount(Context context, int count) {
        // TODO Auto-generated method stub
        return false;
    }

    public int getLoyaltyCount(Context context) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean saveUSerPortalIdExternal(Context context, String portalid) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getUserPortalIdExternal(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

}