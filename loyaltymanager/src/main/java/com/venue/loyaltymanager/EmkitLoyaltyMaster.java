package com.venue.loyaltymanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.venue.loyaltymanager.skidata.SkidataActivity;

/**
 * Created by santhana on 12/23/17.
 */

public class EmkitLoyaltyMaster {
    public static EmkitLoyaltyMaster emkitLoyaltyMaster = null;
    public static Context mContext = null;

    public EmkitLoyaltyMaster() {

    }

    public static EmkitLoyaltyMaster getInstance(Context context) {
        mContext = context;
        if (emkitLoyaltyMaster == null)
            emkitLoyaltyMaster = new EmkitLoyaltyMaster();
        return emkitLoyaltyMaster;
    }

    public void init() {

    }

    public void callSkidata(Activity activity) {
        Intent i = new Intent(activity, SkidataActivity.class);
        activity.startActivity(i);
    }
}
