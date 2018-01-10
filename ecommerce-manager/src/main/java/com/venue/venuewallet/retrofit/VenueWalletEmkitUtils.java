package com.venue.venuewallet.retrofit;

/**
 * Created by manager on 15-12-2017.
 */

public class VenueWalletEmkitUtils {

    private VenueWalletEmkitUtils() {
    }

    public static final String BASE_URL = "https://emkit.emcards.com/";

    public static VenueWalletApiServices getAPIService() {

        return VenueWalletClient.getClient(BASE_URL).create(VenueWalletApiServices.class);
    }
}
