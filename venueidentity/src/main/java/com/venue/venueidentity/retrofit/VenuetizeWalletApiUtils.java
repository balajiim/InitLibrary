package com.venue.venueidentity.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class VenuetizeWalletApiUtils {

    private VenuetizeWalletApiUtils() {
    }

    public static final String BASE_URL = "https://52.33.61.238:443/";

    public static VenuetizeIdentityApiService getAPIService() {

        return VenuetizeIdentityClient.getClient(BASE_URL).create(VenuetizeIdentityApiService.class);
    }
}
