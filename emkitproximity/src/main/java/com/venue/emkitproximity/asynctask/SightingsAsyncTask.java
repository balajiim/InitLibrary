package com.venue.emkitproximity.asynctask;

import android.os.AsyncTask;

import com.venue.emkitproximity.holder.CardGimbalDetails;
import com.venue.emkitproximity.json.JSONParser;
import com.venue.emkitproximity.notifier.SightingsNotifier;
import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitInstance;


public class SightingsAsyncTask extends AsyncTask<String, Void, CardGimbalDetails> {

    private final String TAG = SightingsAsyncTask.this.getClass().getSimpleName();

    private SightingsNotifier notify;

    public SightingsAsyncTask(SightingsNotifier notify) {
        this.notify = notify;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected CardGimbalDetails doInBackground(String... params) {
        Logger.d(TAG, TAG + ":: ");

        String url = params[0];
        String emp2UserId = params[1];
        String transmitterId = params[2];
        String trigger_event = params[3];
        String beacon_name = transmitterId;
        if (params.length > 4)
            beacon_name = params[4];

//		Logger.d(TAG,"url is"+url);
//		Logger.d(TAG,"emp2UserId is"+emp2UserId);
//		Logger.d(TAG,"transmitterId is"+transmitterId);
        JSONParser jParser = new JSONParser();
        jParser.setContext(EmkitInstance.getContext());
        try {

            CardGimbalDetails cardDetails = jParser.getSightingsDetails(url, emp2UserId, transmitterId, trigger_event, beacon_name);
            if (cardDetails != null) {
                return cardDetails;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(CardGimbalDetails cardDetails) {

        if (notify != null) {
            if (cardDetails != null) {

                notify.onSightingsSuccess(cardDetails);

            } else {

                notify.onSightingsError();

            }
        }
    }

}