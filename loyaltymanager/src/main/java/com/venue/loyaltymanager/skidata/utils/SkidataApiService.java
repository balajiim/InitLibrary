package com.venue.loyaltymanager.skidata.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.venue.loyaltymanager.skidata.holder.SkidataGetuserTypeNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataLoginNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataRegistrationNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataValidateUserNotifier;
import com.venue.loyaltymanager.skidata.model.RegistrationResponse;
import com.venue.loyaltymanager.skidata.model.UserRegistrationInfo;
import com.venue.loyaltymanager.skidata.retrofit.SkidataApiServices;
import com.venue.loyaltymanager.skidata.retrofit.SkidataApiUtils;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by manager on 08-01-2018.
 */

public class SkidataApiService {

    Context mContext;
    String TAG = SkidataApiService.class.getSimpleName();
    SkidataApiServices mAPIService;

    public SkidataApiService(Context context) {
        mContext = context;
    }

    public void getUserType(String userName, final SkidataGetuserTypeNotifier notifier) {
        // Retrofit Call
        mAPIService = SkidataApiUtils.getAPIService();
        mAPIService.getUserType(userName).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "getUserType Success" + response.body());
                if (response.code() == 200) {
                    // Sending Success response
                    notifier.onGetUserTypeSuccess("1");
                } else {
                    // Sending Failure response
                    notifier.onGetUserTypeError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response
                notifier.onGetUserTypeError();
            }
        }));
    }

    public void getValidateUser(String userName, String userResponse, final SkidataValidateUserNotifier notifier) {
        // Retrofit Call
        mAPIService = SkidataApiUtils.getAPIService();
        mAPIService.getValidateUser(userName,userResponse).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "getUserType Success" + response.body());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        // Sending Success response
                        notifier.onValidateUserSuccess("");
                    } else {
                        // Sending Failure response
                        notifier.onValidateUserError();
                    }
                } else {
                    notifier.onValidateUserError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response
                notifier.onValidateUserError();
            }
        }));
    }

    public void getLoginUser(String userName, String password, final SkidataLoginNotifier notifier) {
        // Retrofit Call
        mAPIService = SkidataApiUtils.getAPIService();
        mAPIService.getLoginUser().enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "getUserType Success" + response.body());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        // Sending Success response
                        notifier.onLogInSuccess("");
                    } else {
                        // Sending Failure response
                        notifier.onLogInError();
                    }
                } else {
                    notifier.onLogInError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response
                notifier.onLogInError();
            }
        }));
    }

    public void getUserRegistration(UserRegistrationInfo userinfo, final SkidataRegistrationNotifier notifier) {
        // Retrofit Call
        mAPIService = SkidataApiUtils.getAPIService();
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(userinfo));
        mAPIService.getUserRegistration("61","Basic VGhlRGlzdHJpY3REZXRyb2l0QWRtaW46RCE1VFIhQ1Q=",body).enqueue((new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, retrofit2.Response<RegistrationResponse> response) {
                Log.e(TAG, "getUserType Success" + response.body());
                Log.e(TAG, "getUserType Success" + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        // Sending Success response
                        notifier.onRegistrationSuccess("");
                    } else {
                        // Sending Failure response
                      notifier.onRegistrationError();
                    }
                } else {
                   notifier.onRegistrationError();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API."+t.getMessage());
                // Sending Failure response
//                notifier.onLogInError();
            }
        }));
    }
}
