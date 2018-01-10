package com.venue.emvenue.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */


import com.venue.emvenue.model.AppUser;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface VenuetizeIdentityApiService {


    @POST("appUsers")
    Call<AppUser> getAppUser(@Header("Content-Type") String contentType, @Body RequestBody params);


}
