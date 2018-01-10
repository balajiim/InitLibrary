package com.venue.loyaltymanager.skidata.retrofit;

/**
 * Created by manager on 08-01-2018.
 */

public class SkidataApiUtils {

    private SkidataApiUtils() {
    }

    public static final String BASE_URL = "https://thedistrictdetroit.skidataus.com/";

    public static SkidataApiServices getAPIService() {

        return SkidataClient.getClient(BASE_URL).create(SkidataApiServices.class);
    }
}
