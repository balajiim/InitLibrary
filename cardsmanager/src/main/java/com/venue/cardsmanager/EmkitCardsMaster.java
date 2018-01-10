package com.venue.cardsmanager;

import android.content.Context;

/**
 * Created by santhana on 12/23/17.
 */

public class EmkitCardsMaster {
    public static EmkitCardsMaster emkitCardsMaster = null;
    public static Context mContext = null;

    public EmkitCardsMaster() {

    }

    public static EmkitCardsMaster getInstance(Context context) {
        mContext = context;
        if (emkitCardsMaster == null)
            emkitCardsMaster = new EmkitCardsMaster();
        return emkitCardsMaster;
    }

    public void init() {

    }
}
