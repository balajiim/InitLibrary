package com.venue.venuewallet.holder;

import com.venue.venuewallet.model.VenueWalletCardsData;

import java.util.ArrayList;

/**
 * Created by manager on 11-12-2017.
 */

public interface VenueWalletCardsListNotifier {

    public void onCardsListSuccess(ArrayList<VenueWalletCardsData> cardsList);

    public void onCardsListFailure();

    public void getRefreshTokenFailure();
}
