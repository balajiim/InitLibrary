package com.venue.venueidentity.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

import com.venue.venueidentity.utils.IdentityAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VenuetizeIdentityClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(IdentityAPIService.getOkHttpClient())
                .build();

        return retrofit;
    }
}
