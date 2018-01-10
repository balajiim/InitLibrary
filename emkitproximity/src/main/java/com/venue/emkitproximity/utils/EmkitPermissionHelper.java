/**
 * Copyright Venuetize 2017
 * <EmkitPermissionHelper>
 * created by Venuetize
 */
package com.venue.emkitproximity.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.venue.emkitproximity.activity.EmkitPermissionsActivity;
import com.venue.initv2.EmkitInstance;

/**
 * Created by Santhanam on 8/1/2016.
 */
public class EmkitPermissionHelper {

    public static final int MANIFEST_PERMISSION_CAMERA = 1;
    public static final int MANIFEST_PERMISSION_SDCARD = 2;
    public static final int MANIFEST_PERMISSION_CALENDAR = 3;
    public static final int MANIFEST_PERMISSION_MICROPHONE = 4;
    public static final int MANIFEST_PERMISSION_LOCATION = 5;
    public static final int MANIFEST_PERMISSION_CALLPHONE = 6;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static EmkitPermissionHelper permissionHelper = null;
    private Context ctx;

    private EmkitPermissionHelper(Context ctx) {
        //do the init here
        this.ctx = ctx;
    }

    public synchronized static EmkitPermissionHelper getInstance(Context ctx) {
        if (permissionHelper == null) {
            permissionHelper = new EmkitPermissionHelper(ctx);
        }
        return permissionHelper;
    }

    public boolean verifyCameraPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

    public boolean verifySDCardPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

    public boolean verifyCalendarPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

    public boolean verifyLocationPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

    public void startPermissionActivity(String from, int type) {
        Intent permissionIntent = new Intent(ctx, EmkitPermissionsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putInt("type", type);
        permissionIntent.putExtras(bundle);
        permissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(permissionIntent);
    }

    public boolean verifyPhonePermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

}
