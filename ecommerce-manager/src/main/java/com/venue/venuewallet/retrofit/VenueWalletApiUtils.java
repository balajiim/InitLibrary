package com.venue.venuewallet.retrofit;

/**
 * Created by manager on 07-12-2017.
 */

public class VenueWalletApiUtils {

    private VenueWalletApiUtils() {
    }

    // public static final String BASE_URL = "http://venuetize.qa-aws.i2asolutions.com/api/";
    public static final String BASE_URL = "https://fwg7eu0gg2.execute-api.us-west-2.amazonaws.com/stage/api/v1/";

    public static VenueWalletApiServices getAPIService() {

        return VenueWalletClient.getClient(BASE_URL).create(VenueWalletApiServices.class);
    }
}
