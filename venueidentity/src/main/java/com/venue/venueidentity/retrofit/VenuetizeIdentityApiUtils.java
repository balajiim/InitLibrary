package com.venue.venueidentity.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class VenuetizeIdentityApiUtils {

    private VenuetizeIdentityApiUtils() {
    }

    public static final String BASE_URL = "https://emkit.emcards.com/eMcard_V21/";

    public static VenuetizeIdentityApiService getAPIService() {

        return VenuetizeIdentityClient.getClient(BASE_URL).create(VenuetizeIdentityApiService.class);
    }


}
