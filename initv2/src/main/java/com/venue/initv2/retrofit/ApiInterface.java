package com.venue.initv2.retrofit;

import android.content.res.TypedArray;

import com.venue.initv2.holder.DeviceInfo;
import com.venue.initv2.holder.IniteMkitDetails;
import com.venue.initv2.holder.LogEvent;
import com.venue.initv2.holder.NotificationCategory;
import com.venue.initv2.holder.NotificationDetails;
import com.venue.initv2.holder.NotificationResponse;
import com.venue.initv2.holder.UpdateUser;
import com.venue.initv2.holder.UserAppData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @POST("eMcard_V21/initeMkit")
    Call<IniteMkitDetails> getInitEMkit(@Body DeviceInfo body);

    @GET("eMcard_V21/settings/setNotificationStatus?")
    Call<NotificationResponse> getNotificationStatus(@Query("eMp2UserId") String eMp2UserId,
                                                     @Query("notificationStatus") String notificationStatus);

    @GET("eMcard_V21/appUsers/{Id}")
    Call<UserAppData> getAppUsers(@Path("Id") String customerId);

    @GET("eMcard_V21/notificationCategories/{Id}")
    Call<List<NotificationCategory>> getNotificationCategories(@Path("Id") String customerId);

    @PUT("eMcard_V21/appUsers/{eMp2UserId}/settings/notification")
    Call<NotificationDetails> getNotificationDetails(@Path("eMp2UserId") String eMp2UserId,
                                                     @Body RequestBody body);

    @POST("eMcard_V21/LogEventService")
    Call<NotificationResponse> getLogEvent(@Body LogEvent body);

    @PUT("eMcard_V21/appUsers/{Id}")
    Call<UserAppData> getUpdateUser(@Path("Id") String customerId, @Body RequestBody body);

}
