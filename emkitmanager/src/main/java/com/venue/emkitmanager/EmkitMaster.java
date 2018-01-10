package com.venue.emkitmanager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.venue.cardsmanager.EmkitCardsMaster;
import com.venue.emkitproximity.ProximityMaster;
import com.venue.emkitproximity.utils.EmkitPermissionHelper;
import com.venue.emvenue.EmvenueMaster;
import com.venue.emvenue.holder.GetAppUserID;
import com.venue.initv2.EmkitInitMaster;
import com.venue.initv2.EmkitInstance;
import com.venue.initv2.holder.EmkitInitNotifier;
import com.venue.loyaltymanager.EmkitLoyaltyMaster;
import com.venue.mapsmanager.EmkitMapsMaster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by santhana on 12/20/17.
 */

public class EmkitMaster implements GetAppUserID, EmkitInitNotifier {

    public static EmkitMaster emkitInitMaster = null;
    public static Context mContext = null;

    public EmkitMaster() {

    }

    public static EmkitMaster getInstance(Context context) {
        mContext = context;
        if (emkitInitMaster == null)
            emkitInitMaster = new EmkitMaster();
        //checkPermissions();
        return emkitInitMaster;
    }

    public void init(HashMap<String, Object> parameters) {
        String deviceToken = null;
        int notificationResource = -1;
        if (parameters != null && parameters.size() > 0) {
            Set s = parameters.entrySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                //System.out.println("keys are ::"+pairs.getKey());
                if (pairs.getKey().toString().equalsIgnoreCase("deviceToken")) {
                    deviceToken = (String) pairs.getValue();

                }
                if (pairs.getKey().toString().equalsIgnoreCase("notificationIcon")) {
                    notificationResource = (int) pairs.getValue();
                    EmkitInstance.getInstance(mContext).setNotificationIcon(notificationResource);
                }
            }
            EmkitInstance.getInstance(mContext).deviceId(deviceToken);
            EmkitInstance.getInstance(mContext).startEmkitWithValues();
        }
        EmkitInitMaster.getInstance(mContext).initEmkit(deviceToken, this);
        if (mContext.getResources().getBoolean(R.bool.emkit_proximity_config))
            ProximityMaster.getInstance(mContext).init();
        if (mContext.getResources().getBoolean(R.bool.emkit_cards_config))
            EmkitCardsMaster.getInstance(mContext).init();
        if (mContext.getResources().getBoolean(R.bool.emkit_maps_config))
            EmkitMapsMaster.getInstance(mContext).init();
        if (mContext.getResources().getBoolean(R.bool.emkit_loyalty_config))
            EmkitLoyaltyMaster.getInstance(mContext).init();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAppUser(Context context, String deviceKey) {
        mContext = context;
        Log.i(TAG, "getAppUser");
        EmvenueMaster.getInstance(mContext).getAppUser(deviceKey, EmkitMaster.this);
    }

    @Override
    public void getAppUserIDSuccess(String userToken) {
        Log.i(TAG, "getAppUserIDSuccess");
        EmkitInitMaster.getInstance(mContext).updateUserWithEmkit(userToken);

    }

    @Override
    public void getAppUserIDFailure() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void emkitInitSuccess() {
        getAppUser(mContext, EmkitInstance.getInstance(mContext).getDeviceId());
    }

    @Override
    public void emkitInitFailure() {

    }

    private static void checkPermissions() {
        if (!EmkitPermissionHelper.getInstance(mContext).verifySDCardPermission()) {
            EmkitPermissionHelper.getInstance(mContext).startPermissionActivity("init", 2);
        }
    }
}
