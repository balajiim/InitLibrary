package com.venue.emvenue.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class VenuetizeEMVenueApiUtils {

    private VenuetizeEMVenueApiUtils() {
    }

    public static final String BASE_URL = "https://venuefwk.embience.com/VenueFwk/v2/";

    public static VenuetizeIdentityApiService getAPIService() {

        return VenuetizeIdentityClient.getClient(BASE_URL).create(VenuetizeIdentityApiService.class);
    }
}
