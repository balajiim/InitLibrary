package com.venue.venuewallet.retrofit;

/**
 * Created by manager on 11-12-2017.
 */

public class VenueWalletTokenUtils {

    private VenueWalletTokenUtils() {
    }

    public static final String BASE_URL = "https://52.33.61.238:443/";

    public static VenueWalletApiServices getAPIService() {

        return VenueWalletClient.getClient(BASE_URL).create(VenueWalletApiServices.class);
    }
}
