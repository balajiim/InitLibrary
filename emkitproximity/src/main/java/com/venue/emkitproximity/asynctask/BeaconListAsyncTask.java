package com.venue.emkitproximity.asynctask;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.venue.emkitproximity.json.JSONParser;
import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitInstance;

/**
 * Created by santhana on 12/22/17.
 */

public class BeaconListAsyncTask extends AsyncTask<String, Integer, String> {
    protected void onPreExecute() {

    }

    protected void onPostExecute(String result) {
        Logger.i("Utility",
                "going to save resume time ::" + System.currentTimeMillis());
        /*SharedPreferences pref = mContext.getSharedPreferences(
                "emkit_info", 0);
        Editor editor = pref.edit();
        editor.putString("resume_interval_time",
                "" + System.currentTimeMillis());
        editor.commit();*/
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            JSONParser jp = new JSONParser();
            jp.setContext(EmkitInstance.getContext());
            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences(
                    "emkit_info", 0);
            String eMkitAPIKey = pref.getString("eMkitAPIKey", null);
            String beaconsURL = pref.getString("beaconsURL", null);
            if (beaconsURL != null) {
                jp.getBeaconReceivers(beaconsURL + eMkitAPIKey);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
