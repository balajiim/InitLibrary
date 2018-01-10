package com.venue.emkitproximity.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.venue.emkitproximity.json.JSONParser;
import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitInstance;


public class LocationAsyncTask extends AsyncTask<String, Void, String> {

    private final String TAG = LocationAsyncTask.this.getClass()
            .getSimpleName();

    //private LocationNotifier notifier;
    private Context context;


    public LocationAsyncTask() {
    }

    public LocationAsyncTask(Context context) {
        //this.notifier = (LocationNotifier) context;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        Logger.i(TAG, "doInBackground");

        String status = params[0];

        JSONParser jParser = new JSONParser();
        jParser.setContext(EmkitInstance.getContext());
        try {
            return jParser.setLocationStatus(context, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }


}
