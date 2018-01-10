package com.venue.initv2.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.venue.initv2.holder.IniteMkitDetails;

/**
 * Created by santhana on 12/4/17.
 */

public class InitV2Utility {
    private final String TAG = InitV2Utility.class.getName();
    private static InitV2Utility initInstance = null;
    private static Context mContext;
    private SharedPreferences pref = null;


    private static final String USER_INFO = "user-info";

    private static final String EMKIT_USERID = "emp2UserId";
    private static final String URL_CARDDATA = "cardDataURL";
    private static final String URL_SIGHTING = "sightingURL";
    private static final String URL_BEACON = "beaconsURL";
    private static final String EMKIT_VERSION = "eMkitVersionPage";
    private static final String FB_USERID = "fb-userid";
    private static final String FIRST_NOTI = "first_notification";
    private static final String EXTCARDURL ="extCardDataURL";
    private static final String LOCATION_TRACKING = "locationTracking";

    private static final String KEY_INITEMKIT_INFO = "emkit_info";
    private static final String KEY_USER_INFO = "user-info-key";

    private static final String NEW_CARD_DATA_STRUCTURE = "newCardDataStructure";

    public static InitV2Utility getInstance(Context context) {
        mContext = context;
        if (initInstance == null)
            initInstance = new InitV2Utility();
        return initInstance;
    }

    public InitV2Utility() {
        pref = mContext.getSharedPreferences("emkit_info", 0);
    }

    public void savePreference(String key, String data) {
        if (pref == null)
            pref = mContext.getSharedPreferences("emkit_info", 0);

        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, data);
        edit.commit();
    }

    public boolean saveIniteMkitDetails(IniteMkitDetails details) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(KEY_INITEMKIT_INFO, Context.MODE_PRIVATE).edit();

        editor.putString(EMKIT_USERID, details.getEmp2UserId());
        editor.putString(URL_CARDDATA, details.getCardDataURL());
        editor.putString(URL_SIGHTING, details.getSightingURL());
        editor.putString(URL_BEACON, details.getBeaconsURL());
        editor.putString(EMKIT_VERSION, details.getEMkitVersionPage());
        editor.putString(EXTCARDURL, details.getExtCardDataURL());
        editor.putString("emkit_init_response", new Gson().toJson(details));
        return editor.commit();
    }
}
