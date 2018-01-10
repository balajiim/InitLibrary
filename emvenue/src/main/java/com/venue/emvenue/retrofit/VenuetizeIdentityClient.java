package com.venue.emvenue.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VenuetizeIdentityClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
