/**
 * Copyright Venuetize 2017
 * <EmkitDescriptionActivity>
 * created by Venuetize
 */
package com.venue.emkitproximity.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitInitialization;
import com.venue.initv2.EmkitInstance;


public class EmkitDescriptionActivity extends Activity {
    String postid;
    String linkType = "";
    String linkurl = "";
    private EmkitInitialization eMkit;
    String TAG = "EmkitDescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(R.id.info);
        Logger.i(TAG, "oncreate of EmkitDescriptionActivity");
        linkurl = getIntent().getStringExtra("finalurl");
        linkType = getIntent().getStringExtra("linktype");

        if (linkType != null && linkType.length() > 0) {
            if (linkType.equalsIgnoreCase("deeplink")) {
                Logger.d(TAG, "sightings details inside deeplink ");

                getEmkit(getApplicationContext()).launchDeeplink(getApplicationContext(), linkurl);

            } else if (linkType.equalsIgnoreCase("audio")) {
                Logger.d(TAG, "sightings details inside audio ");

                getEmkit(getApplicationContext()).launchAudio(getApplicationContext(), linkurl);
            } else if (linkType.equalsIgnoreCase("video")) {
                Logger.d(TAG, "sightings details inside video ");
                getEmkit(getApplicationContext()).launchVideo(getApplicationContext(), linkurl);
            } else if (linkType.equalsIgnoreCase("link")) {
                Logger.d(TAG, "sightings details inside link ");
                getEmkit(getApplicationContext()).launchWebView(getApplicationContext(), linkurl);
            }
        }
        finish();

    }

    private EmkitInitialization getEmkit(Context ctx) {
        eMkit = EmkitInstance.getInstance(ctx);
        return eMkit;
    }

    private void selectItem(String link) {

    }

    public boolean isUserLoggedIn() {
        return true;
    }

    public void callingHome() {

    }

    public void callingMemberCentralNoAccount() {

    }

    public void callingWebView() {

    }

    private void openTMSDK() {

    }


    private void openWallet() {


    }


}
