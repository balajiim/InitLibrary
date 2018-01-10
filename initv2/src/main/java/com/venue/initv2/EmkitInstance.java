/**
 * Copyright Venuetize 2017
 * <EmkitInstance>
 * created by Venuetize
 */
package com.venue.initv2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.venue.initv2.utility.Logger;

import java.util.Hashtable;

public class EmkitInstance {
    public static Context mCon;
    public static Context emkit_context;
    private static EmkitInitialization emkit = null;
    private static Hashtable<String, Object> fontHash = null;

    private EmkitInstance(Context con) {

        mCon = con;
        emkit = new EmkitInitialization(mCon);
    }

    public static Context getContext() {
        return mCon;
    }

    public synchronized static EmkitInitialization getInstance(Context con) {
        mCon = con.getApplicationContext();
        emkit_context = con;
        Logger.i("EmkitInstance", "EmkitInstance Context" + emkit_context);
        if (emkit == null)
            emkit = new EmkitInitialization(con);
        return emkit;
    }

    public static void setHashTable(Hashtable<String, Object> tempHash) {
        fontHash = tempHash;
    }

    public static Hashtable<String, Object> getFontHash() {
        return fontHash;
    }

}
