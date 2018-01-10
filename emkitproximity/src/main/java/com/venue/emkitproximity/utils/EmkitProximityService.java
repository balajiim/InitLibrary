package com.venue.emkitproximity.utils;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.venue.emkitproximity.activity.CustomDialogActivity;
import com.venue.emkitproximity.manager.ProximityController;
import com.venue.initv2.EmkitInstance;

import java.util.Timer;
import java.util.TimerTask;

public class EmkitProximityService extends Service {

    static ProgressDialog ringProgressDialog = null;
    static Context mCon;

    private static final int UPDATE_STARTED = 0;
    private static final int UPDATE_FINISHED = 1;

    private static Intent progressIntent;
    public static TimerTask timerTask = null;
    public static Timer timer = null;

    public static TimerTask stopTimerTask = null;
    public static Timer stopTimer = null;

    @Override
    public void onCreate() {
        System.out.println("Venuetize SDK proximity service started");
        EmkitInstance.getInstance(getApplicationContext()).checkGimbalPresence();
        clearBeaconTimer(getApplicationContext());
        mCon = this;
        progressIntent = new Intent(mCon, CustomDialogActivity.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mCon = this;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void showProgressDlg() {

		/*
         * Message m = new Message(); m.what = UPDATE_STARTED;
		 * handler.sendMessage(m);
		 */

        progressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle myKillerBundle = new Bundle();
        myKillerBundle.putInt("kill", 0);
        progressIntent.putExtras(myKillerBundle);
        mCon.startActivity(progressIntent);
    }

    public static void hideProgressDlg() {
        /*
         * Message m = new Message(); m.what = UPDATE_FINISHED;
		 * handler.sendMessage(m);
		 */
        progressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle myKillerBundle = new Bundle();
        myKillerBundle.putInt("kill", 1);
        progressIntent.putExtras(myKillerBundle);
        mCon.startActivity(progressIntent);
    }

    public void clearBeaconTimer(Context context) {

        if (timer != null) {
            timer.cancel();
            timer = null;

        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                try {
                    ProximityController.getInstance(getApplicationContext())
                            .clearBeacon();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        timer.schedule(timerTask, 0, 300000);

    }

    public void stopBeaconTimer(Context context) {

        if (stopTimer != null) {
            stopTimer.cancel();
            stopTimer = null;

        }
        if (stopTimerTask != null) {
            stopTimerTask.cancel();
            stopTimerTask = null;
        }
        stopTimer = new Timer();
        stopTimerTask = new TimerTask() {
            public void run() {
                try {
                    ProximityController.getInstance(getApplicationContext())
                            .stopBeaconListening();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        stopTimer.schedule(stopTimerTask, 0, 10000);

    }
}
