package com.venue.emkitproximity;

import android.content.Context;
import android.content.Intent;

import com.venue.emkitproximity.manager.ProximityController;
import com.venue.emkitproximity.utils.EmkitProximityService;

/**
 * Created by santhana on 12/20/17.
 */

public class ProximityMaster {

    public static ProximityMaster emkitInitMaster = null;
    public static Context mContext = null;

    public ProximityMaster() {

    }

    public static ProximityMaster getInstance(Context context) {
        mContext = context;
        if (emkitInitMaster == null)
            emkitInitMaster = new ProximityMaster();
        return emkitInitMaster;
    }

    public void init() {
        ProximityController.getInstance(mContext).initiateProximity();
        mContext.startService(new Intent(mContext.getApplicationContext(), EmkitProximityService.class));
    }

}
