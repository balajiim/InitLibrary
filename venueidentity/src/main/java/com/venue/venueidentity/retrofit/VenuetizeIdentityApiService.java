package com.venue.venueidentity.retrofit;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

import com.venue.venueidentity.model.AppUser;
import com.venue.venueidentity.model.AuthorizeToken;
import com.venue.venueidentity.model.EmkitResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface VenuetizeIdentityApiService {

    @POST("initeMkit")
    Call<EmkitResponse> getEmkitID(@Body RequestBody params);

    @POST("appUsers")
    Call<AppUser> getAppUser(@Header("Content-Type") String contentType, @Header("User-Agent") String useragent, @Header("reg_id") String reg_id, @Header("clientType") String clientType, @Header("eMProfileAPIKey") String eMProfileAPIKey, @Body RequestBody params);

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<AuthorizeToken> getAuthorizeToken(@Header("Authorization") String authorization, @Field("grant_type") String grantType, @Field("scope") String scope, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<AuthorizeToken> getAuthorizeRefreshToken(@Header("Authorization") String authorization, @Field("grant_type") String grantType, @Field("refresh_token") String refresh_token);


}
