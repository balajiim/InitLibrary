/**
 * Copyright Venuetize 2017
 * <EmkitNotificationActivity>
 * created by Venuetize
 */
package com.venue.emkitproximity.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.CloseNotifier;
import com.venue.initv2.EmkitInitialization;
import com.venue.initv2.EmkitInstance;

public class EmkitNotificationActivity extends Activity implements CloseNotifier {

    private final String TAG = EmkitNotificationActivity.this.getClass()
            .getSimpleName();
    private EmkitInitialization eMkit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "oncreate of EmkitNotificationActivity");
        //onNewIntent(getIntent());
        String cardIdnew = getIntent().getStringExtra("emkit_url");
        //System.out.println("cardIdnew::"+cardIdnew);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String cardId = getIntent().getStringExtra("emkit_url");
            try {
                String instantpush_id = getIntent().getStringExtra("instantpush_id");
                String campaign_id = getIntent().getStringExtra("campaign_id");
                getEmkit(EmkitNotificationActivity.this).emkitLogEvent(EmkitNotificationActivity.this, campaign_id, instantpush_id);
            } catch (Exception e) {
                Logger.i("EmkitNotificationActivity", "log event exception");
                e.printStackTrace();
            }

            if (cardId != null && cardId.trim().length() > 0) {
                //	EmkitInitialization s = EmkitInstance.getInstance(this);
                getEmkit(EmkitNotificationActivity.this).setFlag(this, true);
                getEmkit(EmkitNotificationActivity.this).displayCard(this, cardId.substring(cardId.indexOf("=") + 1, cardId.indexOf("&")));
            } else {

                //System.out.println("getIntent().getStringExtra(finalurl)::"+getIntent().getStringExtra("finalurl"));
                //System.out.println("getIntent().getStringExtra(linktype)::"+getIntent().getStringExtra("linktype"));
                //System.out.println("getIntent().getStringExtra(instantpush_id)::"+getIntent().getStringExtra("instantpush_id"));

                try {
                    if (getIntent().getStringExtra("finalurl").trim().equals("")) {
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
                        startActivity(LaunchIntent);
                        finish();
                    }
                } catch (Exception e) {
                }

				/*if (getIntent().getStringExtra("finalurl") != null && getIntent().getStringExtra("finalurl").length() > 0)
                    Utility.getKeyString(this, getIntent().getStringExtra("finalurl"), getIntent().getStringExtra("linktype"));*/


                Logger.i(TAG, "finalurl is :" + getIntent().getStringExtra("finalurl"));
                Logger.i(TAG, "finalurl is  1:" + getIntent().getStringExtra("linktype"));
                //Logger.i(TAG,"finalurl is  2:"+getIntent().getStringExtra("emkit_url"));
                Intent descriptionIntent = new Intent(this, EmkitDescriptionActivity.class);
                descriptionIntent.putExtra("finalurl", getIntent().getStringExtra("finalurl"));
                descriptionIntent.putExtra("linktype", getIntent().getStringExtra("linktype"));
                startActivity(descriptionIntent);
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

	/*@Override
    protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		//showDialog(1, intent.getExtras());
		EmkitInitialization s = new EmkitInitialization(this);
	   	s.displayCard(this, "462");
	}*/

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        // TODO Auto-generated method stub
        switch (id) {
            case 1:

                break;
            case 2:

                break;
            default:
                break;
        }

        return super.onCreateDialog(id);
    }

    @Override
    public void cardSuccess(String res) {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void cardFailure(String res) {
        // TODO Auto-generated method stub
        finish();
    }

    private EmkitInitialization getEmkit(Context ctx) {
        eMkit = EmkitInstance.getInstance(ctx);
        return eMkit;
    }
}
