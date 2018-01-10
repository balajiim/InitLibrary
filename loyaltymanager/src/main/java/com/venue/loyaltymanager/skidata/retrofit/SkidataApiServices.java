package com.venue.loyaltymanager.skidata.retrofit;

import com.venue.loyaltymanager.skidata.model.RegistrationResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by manager on 08-01-2018.
 */

public interface SkidataApiServices {

    @GET("DesktopModules/v1/API/Authentication/GetLoginType/")
    Call<ResponseBody> getUserType( @Query("username") String username);

    @POST("/DesktopModules/v1/API/Authentication/Validate/{username}/{loginType}")
    Call<ResponseBody> getValidateUser(@Path("username") String username,@Path("loginType")String loginType);

    @POST("/DesktopModules/v1/API/user")
    Call<ResponseBody> getLoginUser();

    @POST("DesktopModules/v1/API/registration/{id}")
    Call<RegistrationResponse> getUserRegistration(@Path("id") String id, @Header("Authorization") String header, @Body RequestBody params);

}
