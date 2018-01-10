package com.venue.venuewallet.retrofit;

import com.venue.venuewallet.utils.WalletApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manager on 07-12-2017.
 */

public class VenueWalletClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(WalletApiService.getOkHttpClient())
                .build();

        return retrofit;
    }
}
