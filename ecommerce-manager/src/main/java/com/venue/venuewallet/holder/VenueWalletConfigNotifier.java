package com.venue.venuewallet.holder;

import com.venue.venuewallet.model.VenueWalletConfigResponse;

/**
 * Created by manager on 14-12-2017.
 */

public interface VenueWalletConfigNotifier {

    public void onConfigDataSuccess(VenueWalletConfigResponse response);

    public void ononfigDataFailure();

    public void getRefreshTokenFailure();
}
