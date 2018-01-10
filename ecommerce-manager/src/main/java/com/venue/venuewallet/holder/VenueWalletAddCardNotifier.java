package com.venue.venuewallet.holder;

/**
 * Created by manager on 11-12-2017.
 */

public interface VenueWalletAddCardNotifier {

    public void onAddCardSuccess();

    public void onAddCardsFailure();

    public void getRefreshTokenFailure();
}
