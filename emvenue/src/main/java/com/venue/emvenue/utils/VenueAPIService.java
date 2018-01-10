package com.venue.emvenue.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Log;

import com.venue.emvenue.EmvenueMaster;
import com.venue.emvenue.holder.GetAppUserID;
import com.venue.emvenue.model.AppUser;
import com.venue.emvenue.retrofit.VenuetizeEMVenueApiUtils;
import com.venue.emvenue.retrofit.VenuetizeIdentityApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

/**
 * Created by mastmobilemedia on 27-12-2017.
 */

public class VenueAPIService {
    Context mContext;
    private VenuetizeIdentityApiService mAPIService;
    String getAuthToken = "";

    SharedPreferences sharedpreferences;

    ProgressDialog progressDialog;

    public VenueAPIService(Context context) {
        mContext = context;
    }

    //To get App User ID
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAppID(String deviceKey, final GetAppUserID notifier) {

        mAPIService = VenuetizeEMVenueApiUtils.getAPIService();
        sharedpreferences = mContext.getSharedPreferences(EmvenueMaster.IDENTITYPREFERENCES, Context.MODE_PRIVATE);

        //Adding values into array map
        Map<String, Object> map = new ArrayMap<>();
        map.put("appUserId", "");
        map.put("deviceKey", deviceKey);
        JSONObject jobject = new JSONObject();
        JSONArray ja = new JSONArray();

        map.put("userProfile", ja);
        map.put("externalUserIds", jobject);
        Log.d(TAG, "Retro Emkit Intialization: " + (new JSONObject(map)).toString());

        // Retrofit Call
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());
        mAPIService.getAppUser("application/json", body).enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, retrofit2.Response<AppUser> response) {
                Log.e(TAG, "Success ");
                AppUser res = response.body();
                Log.e(TAG, "Success Appuserid :: " + res.getAppUserId());
                //   progressDialog.dismiss();

                notifier.getAppUserIDSuccess(res.getAppUserId());

                // Storing Emp2UserID in local
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("AppUserId", res.getAppUserId());
                editor.commit();
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
                // progressDialog.dismiss();
                notifier.getAppUserIDFailure();
            }
        });
    }
}
