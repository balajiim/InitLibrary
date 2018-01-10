package com.venue.venuewallet.holder;

import com.venue.venuewallet.model.VenueWalletCardsData;

/**
 * Created by manager on 14-12-2017.
 */

public interface VenueWalletEditCardNotifier {

    public void onEditCardSuccess(VenueWalletCardsData cardsData);

    public void onEditCardsFailure();

    public void getRefreshTokenFailure();
}
