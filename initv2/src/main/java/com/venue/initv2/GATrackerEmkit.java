/**
 * Copyright Venuetize 2017
 * <GATrackerEmkit>
 * created by Venuetize
 */
package com.venue.initv2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.venue.initv2.utility.Logger;

import java.util.HashMap;

public class GATrackerEmkit {

    private static final String LOG_TAG = "GATrackerEmkit";

    private static GoogleAnalytics EmkitGA;

    private static Tracker EmkitTracker;

    private static String EMKIT_GA_PROPERTY_ID = null;// = "UA-47615352-1";

    private static int EMKIT_GA_DISPATCH_PERIOD; // = 10;

    private static Context mContext;
    // private static final boolean MWC_GA_IS_DRY_RUN = false;//Set this true
    // for testing


    @SuppressWarnings("deprecation")
    public static void init(Context context) {

        EMKIT_GA_PROPERTY_ID = context.getResources().getString(
                R.string.emkit_ga_property_id);
        mContext = context;
        EMKIT_GA_DISPATCH_PERIOD = Integer.parseInt(context.getResources()
                .getString(R.string.ga_dispatch_period));


        if (EmkitTracker == null) {

            EmkitGA = GoogleAnalytics.getInstance(context);

            /*EmkitTracker = EmkitGA.getTracker(EMKIT_GA_PROPERTY_ID);

            GAServiceManager.getInstance().setLocalDispatchPeriod(
                    EMKIT_GA_DISPATCH_PERIOD);

            // mwcGA.setDryRun(MWC_GA_IS_DRY_RUN);

            EmkitGA.getLogger().setLogLevel(EMKIT_GA_LOG_VERBOSITY);*/
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            EmkitTracker = analytics.newTracker(EMKIT_GA_PROPERTY_ID);

        } else {
            Logger.d(LOG_TAG, "EMKIT GA Tracker Initialized");
        }

    }

    public static void set(String screenName, String string) {
        if (EmkitTracker != null) {
            EmkitTracker.set(screenName, string);
        }
    }

    public static void set(String screenName, String string, boolean flag) {
        if (flag && EmkitTracker != null) {
            EmkitTracker.set(screenName, string);
            SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
            /*EmkitTracker.set(Fields.APP_ID, pref.getString("eMkitAPIKey", null));
            EmkitTracker.send(MapBuilder.createAppView().build());*/
            EmkitTracker.send(new HitBuilders.EventBuilder()
                    .set("appId", pref.getString("eMkitAPIKey", null))
                    .build());

        }
    }

    public static void logEvent(String category, String action, String label,
                                Long value) {
        if (EmkitTracker != null) {
            Logger.d(LOG_TAG, "G_Analytics data: " + category + " : " + action
                    + " : " + label + " : " + value);
            HashMap<String, String> hitParameters = new HashMap<String, String>();
            SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
            /*hitParameters.put(Fields.APP_ID, pref.getString("eMkitAPIKey", null));
            hitParameters.put(Fields.EVENT_CATEGORY, category);
            hitParameters.put(Fields.EVENT_ACTION, action);
            hitParameters.put(Fields.EVENT_LABEL, label);*/
            EmkitTracker.send(new HitBuilders.EventBuilder()
                    .set("appId", pref.getString("eMkitAPIKey", null))
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .setValue((value == null) ? 0L : value)
                    .build());
            /*EmkitTracker.send(MapBuilder.createEvent(category, action, label,
					null).build());*/
        } else {
            Logger.d(LOG_TAG, "EMKIT GA Tracker is null");
        }

    }

}