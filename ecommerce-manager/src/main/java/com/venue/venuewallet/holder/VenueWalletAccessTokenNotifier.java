package com.venue.venuewallet.holder;

/**
 * Created by manager on 11-12-2017.
 */

public interface VenueWalletAccessTokenNotifier {

    public void onAccessTokenSuccess();

    public void onAccessTokenFailure();

    public void getRefreshTokenFailure();
}
