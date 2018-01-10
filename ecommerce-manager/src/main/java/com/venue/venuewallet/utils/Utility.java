package com.venue.venuewallet.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by manager on 07-12-2017.
 */

public class Utility {

    public void showInfoDialog(String msg, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        R.style.AboutDialog));

        alertDialog.setTitle("");
        alertDialog.setPositiveButton(context.getResources().getString(R.string.venue_wallet_ok_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setMessage(msg);
        AlertDialog alertDialog1 = alertDialog.create();

        alertDialog1.show();
    }

    public static int getResourceId(String iconName, Context mainActivity) {
        if (iconName == null || iconName.equalsIgnoreCase(""))
            return mainActivity.getResources().getIdentifier(
                    mainActivity.getPackageName() + ":drawable/" + iconName,
                    null, null);
        else {
            int drawableId = 0;
            try {
                Class res = R.drawable.class;
                Field field = res.getField(iconName);
                drawableId = field.getInt(null);
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return drawableId;
        }
    }

    public void emkitButtonLogEvent(Context context, String Category, String lable) {

        //Adding values into array map
        HashMap<String, Object> map = new HashMap<>();
        map.put("emp2UserId", "");
        map.put("eventCategory", Category);
        map.put("eventType", context.getResources().getString(R.string.venue_wallet_button_eventtype));
        map.put("eventValue", lable);
        map.put("screenReference", "");
        VenueWalletManager.getInstance(context).getWalletLogEvent(context, map);
    }

    //To Create HMAC Key using HMACSHA1 algorithm
    public static String walletHMac(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}



