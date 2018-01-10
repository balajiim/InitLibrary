package com.venue.venuewallet;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.EmkitResponseNotifier;
import com.venue.venueidentity.holder.GetAccessToken;

import static android.content.ContentValues.TAG;

/**
 * Created by mastmobilemedia on 20-12-2017.
 */

public class MainWalletActivity extends FragmentActivity implements EmkitResponseNotifier, GetAccessToken {

    Button initEmkit, authorizeToken, wallet, walletdialog, preAuth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //Initializing UI
        initializeUI();

        getWalletManager().callingWallet(MainWalletActivity.this);
    }

    public void initializeUI() {
        authorizeToken = (Button) findViewById(R.id.authorizeToken);
        wallet = (Button) findViewById(R.id.wallet);
        preAuth = (Button) findViewById(R.id.preauth);

        LinearLayout layout = (LinearLayout) findViewById(R.id.main_layout);
        layout.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void eMKitLoginSuccess() {

        Log.d(TAG, "onEmkitInitSuccess: ");

    }

    @Override
    public void eMKitLoginFailure() {

    }


    private VenuetizeIdentityManager getIdentityManager() {
        return VenuetizeIdentityManager.getInstance(getApplicationContext());
    }

    private VenueWalletManager getWalletManager() {
        return VenueWalletManager.getInstance(getApplicationContext());
    }

    @Override
    public void getAccessTokenSuccess(String accessToken) {
        Log.d(TAG, "getAccessTokenSuccess: " + accessToken);
    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }
}

