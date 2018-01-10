package com.venue.emvenue;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.venue.emvenue.holder.GetAppUserID;
import com.venue.emvenue.utils.VenueAPIService;

import static android.content.ContentValues.TAG;

/**
 * Created by santhana on 12/23/17.
 */

public class EmvenueMaster {
    public static EmvenueMaster emvenueMaster = null;
    public static Context mContext = null;
    public static final String IDENTITYPREFERENCES = "EmvenueMaster";

    public EmvenueMaster() {

    }

    public static EmvenueMaster getInstance(Context context) {
        mContext = context;
        if (emvenueMaster == null)
            emvenueMaster = new EmvenueMaster();
        return emvenueMaster;
    }

    public void init() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAppUser(String deviceKey, GetAppUserID notifier) {
        Log.i(TAG, "Emvenue Master getAppUser");
        VenueAPIService apiService = new VenueAPIService(mContext);
        apiService.getAppID(deviceKey, notifier);
    }


}
