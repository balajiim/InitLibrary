package com.venue.venuewallet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.model.EmvPaymentRequest;

/**
 * Created by manager on 08-12-2017.
 */

public class VenueWalletDialogActivity extends FragmentActivity implements GetAccessToken {

    EmvPaymentRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.venue_wallet_dialog_activity);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // To retrieve object in second Activity
        request = (EmvPaymentRequest) getIntent().getSerializableExtra("paymentClass");

        initView();
    }

    void initView() {
        TextView cancel_textview = (TextView) findViewById(R.id.cancel_textview);
        cancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWalletManager().callingWallet(VenueWalletDialogActivity.this, "cart");

    }

    private VenueWalletManager getWalletManager() {
        return VenueWalletManager.getInstance(getApplicationContext());
    }

    @Override
    public void getAccessTokenSuccess(String accessToken) {

    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }
}
