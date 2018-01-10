/**
 * Copyright Venuetize 2017
 * <EmkitPermissionsActivity>
 * created by Venuetize
 */
package com.venue.emkitproximity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.venue.emkitproximity.manager.ProximityController;
import com.venue.emkitproximity.utils.EmkitPermissionHelper;
import com.venue.initv2.EmkitInstance;
import com.venue.initv2.utility.Logger;

import java.util.ArrayList;
import java.util.List;

public class EmkitPermissionsActivity extends Activity {

    private static final String TAG = "EmkitPermissionsActivity";
    Context ctx;
    AlertDialog permissionDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ctx = this;

        if (Build.VERSION.SDK_INT >= 23) {
            Logger.i(TAG, " 11111");
            if (getIntent() != null && getIntent().getExtras() != null) {
                Logger.i(TAG, " 22222");
                String from = getIntent().getStringExtra("from");
                int type = getIntent().getIntExtra("type", -1);
                if (type != -1) {
                    if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_CAMERA) {
                        checkCameraPermission();
                    } else if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_SDCARD) {
                        checkSdcardPermission();
                    } else if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_LOCATION) {
                        checkLocationPermission();
                    } else if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_CALENDAR) {
                        checkCalendarPermission();
                    } else if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_CALLPHONE) {
                        checkPhonePermission();
                    } else if (type == EmkitPermissionHelper.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
                        checkNeededPermission();
                    }
                } else {
                    finish();
                }
            } else {
                Logger.i(TAG, " 33333");
                SharedPreferences pref = getSharedPreferences("emkit_info", 0);
                if (pref.getString("emkit_perm_dialog_display", null) == null) {
                    Logger.i(TAG, " 44444");
                    if (!EmkitInstance.getInstance(this).isApplicationInForeground()) {
                        final SharedPreferences.Editor editor = pref.edit();
                        editor.putString("emkit_perm_dialog_display", "1");
                        editor.commit();
                        finish();
                    } else
                        showMultiplePermissions();
                } else
                    finish();
            }
        } else
            finish();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();
    }

    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void checkCameraPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                    EmkitPermissionsActivity.this, new String[]{Manifest.permission.CAMERA},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_CAMERA);

        } else {

        }
    }

    protected void checkCalendarPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                    EmkitPermissionsActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_CALENDAR);
            response = false;
        } else {
            response = true;
        }
    }

    protected void checkPhonePermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                    EmkitPermissionsActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_CALENDAR);
            response = false;
        } else {
            response = true;
        }
    }

    protected void checkLocationPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_LOCATION);
            response = false;
        } else {
            response = true;
        }
    }

    protected boolean checkMicrophonePermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            response = false;
        } else {
            response = true;
        }
        return response;
    }

    protected void checkSdcardPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                    EmkitPermissionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_SDCARD);
            response = false;
        } else {
            response = true;
        }
    }

    protected void checkNeededPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return;
        boolean response = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                    EmkitPermissionHelper.MANIFEST_PERMISSION_LOCATION);
        } else {

        }
    }

    protected void displayMessageDialog(final Context context, final int type, final String displayMessage) {
        if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_CAMERA) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((permissionDialog != null) && permissionDialog.isShowing()) {
                        return;
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle(getString(com.venue.initv2.R.string.app_name))
                            .setMessage(displayMessage)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, final int whichButton) {
                                    ActivityCompat.requestPermissions(
                                            EmkitPermissionsActivity.this, new String[]{Manifest.permission.CAMERA},
                                            1);
                                }
                            });

                    permissionDialog = dialog.create();
                    permissionDialog.setCancelable(false);
                    permissionDialog.show();
                }
            });
        } else if (type == EmkitPermissionHelper.MANIFEST_PERMISSION_SDCARD) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((permissionDialog != null) && permissionDialog.isShowing()) {
                        return;
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle(getString(com.venue.initv2.R.string.app_name))
                            .setMessage(displayMessage)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, final int whichButton) {
                                    ActivityCompat.requestPermissions(
                                            EmkitPermissionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            1);
                                }
                            });

                    permissionDialog = dialog.create();
                    permissionDialog.setCancelable(false);
                    permissionDialog.show();
                }
            });
        }
    }


    private void showMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) ;
        permissionsNeeded.add("Read and Write storage");
        /*if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Reading phone state");*/

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                ActivityCompat.requestPermissions(EmkitPermissionsActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        EmkitPermissionHelper.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            ActivityCompat.requestPermissions(EmkitPermissionsActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                    EmkitPermissionHelper.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        } else {
            finish();
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(EmkitPermissionsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(EmkitPermissionsActivity.this, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("Manimaran onRequestPermissionsResult "+requestCode);
        switch (requestCode) {
            case EmkitPermissionHelper.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (verifyLocationPermission()) {
                    ProximityController.getInstance(this).startProximity();
                }
                finish();
            }
            break;
            case EmkitPermissionHelper.MANIFEST_PERMISSION_SDCARD: {
                finish();
            }
            break;
            case EmkitPermissionHelper.MANIFEST_PERMISSION_CALENDAR: {
                finish();
            }
            break;
            case EmkitPermissionHelper.MANIFEST_PERMISSION_MICROPHONE: {
                finish();
            }
            break;
            case EmkitPermissionHelper.MANIFEST_PERMISSION_LOCATION: {
                ProximityController.getInstance(this).startProximity();
                finish();
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
}
