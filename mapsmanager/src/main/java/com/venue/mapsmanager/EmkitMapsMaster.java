package com.venue.mapsmanager;

import android.content.Context;

/**
 * Created by santhana on 12/23/17.
 */

public class EmkitMapsMaster {
    public static EmkitMapsMaster emkitMapsMaster = null;
    public static Context mContext = null;

    public EmkitMapsMaster() {

    }

    public static EmkitMapsMaster getInstance(Context context) {
        mContext = context;
        if (emkitMapsMaster == null)
            emkitMapsMaster = new EmkitMapsMaster();
        return emkitMapsMaster;
    }

    public void init() {

    }
}
