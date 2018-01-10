
/**
 * Copyright Venuetize 2017
 * <EmProximity>
 * created by Venuetize
 */


package com.venue.emkitproximity;

import android.content.Context;

import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Visit;
import com.venue.emkitproximity.manager.ProximityController;
import com.venue.emkitproximity.utils.Logger;

import java.util.HashMap;

/**
 * Created by Santhanam on 2/27/2017.
 */

public class EmProximity {

    private static EmProximity eMProximityInstance = null;
    private static Context mContext;

    public EmProximity(Context context) {
        mContext = context;
    }

    public static synchronized EmProximity getInstance(Context context) {
        mContext = context;
        if (eMProximityInstance == null) {
            eMProximityInstance = new EmProximity(context);
        }
        return eMProximityInstance;
    }

    public void didBeginVisit(Visit visit) {
        try {
            Logger.i("EmProximity", "didBeginVisit" + visit.getPlace().getName());
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("place_name", visit.getPlace().getName());
            //Call the emkit interface to open dex
            ProximityController.getInstance(mContext).didBeginVisit(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void didEndVisit(Visit visit) {
        try {
            Logger.i("EmProximity", "didEndVisit" + visit.getPlace().getName());
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("place_name", visit.getPlace().getName());
            //Call the emkit interface to open dex
            ProximityController.getInstance(mContext).didEndVisit(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void didBeaconSighting(BeaconSighting arg0) {
        try {
            Logger.i("EmProximity", "didBeaconSighting" + arg0.getBeacon().getName());
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("beacon_name", arg0.getBeacon().getName());
            hashMap.put("battery_level", "" + arg0.getBeacon().getBatteryLevel());
            hashMap.put("beacon_rssi", "" + arg0.getRSSI());
            hashMap.put("beacon_temp", "" + arg0.getBeacon().getTemperature());
            hashMap.put("beacon_id", arg0.getBeacon().getIdentifier());
            //Call the emkit interface to open dex
            ProximityController.getInstance(mContext).didBeaconSighting(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
