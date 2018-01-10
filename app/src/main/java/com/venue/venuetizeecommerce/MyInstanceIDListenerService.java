package com.venue.venuetizeecommerce;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.venue.emkitmanager.EmkitMaster;

import java.util.HashMap;

/**
 * Created by mastmobilemedia on 11-12-2017.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        // sendRegistrationToServer(refreshedToken);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
        parameters.put("notificationIcon", R.mipmap.ic_launcher);
        EmkitMaster.getInstance(getApplicationContext()).init(parameters);
    }

}