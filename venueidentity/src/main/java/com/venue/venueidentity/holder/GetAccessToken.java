package com.venue.venueidentity.holder;

/**
 * Created by mastmobilemedia on 11-12-2017.
 */

public interface GetAccessToken {

    public void getAccessTokenSuccess(String accessToken);

    public void getAccessTokenFailure();

    public void getRefreshTokenFailure();
}
