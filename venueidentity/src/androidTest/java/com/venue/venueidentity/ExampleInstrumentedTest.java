package com.venue.venueidentity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.ArrayMap;
import android.util.Log;

import com.venue.venueidentity.model.AppUser;
import com.venue.venueidentity.model.EmkitResponse;
import com.venue.venueidentity.retrofit.VenuetizeEMVenueApiUtils;
import com.venue.venueidentity.retrofit.VenuetizeIdentityApiService;
import com.venue.venueidentity.retrofit.VenuetizeIdentityApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.venue.venueidentity.test", appContext.getPackageName());
    }

    @Test
    public void init_Emkit() {


        VenuetizeIdentityApiService mAPIService = VenuetizeIdentityApiUtils.getAPIService();

        //Adding values into array map
        Map<String, Object> map = new ArrayMap<>();
        map.put("requestUrl", "http://35.164.194.205/eMcard_V21/emp2?app=emcard&client=ANDROID&platform_id=(null)&device_name=condor_umtsds&scr_group=540x888&carr_id=1&model=XT1022&carrierName=AT%26T&@2=&@3=1.0&@4=1.0&");
        map.put("deviceKey", "dmY7UEOHW2o:APA91bGG1M2tWV4xcEkRNhBBFY1sDvf-Na9kzNgdTmI2FKgxSnpCCVZQ_VNe3lM5mnh26kSBJP6RXiCY7NirS8nHpN9dmApVoQVg-3QkCC85GkibTlg9XgXzUlQWoVPDsNZdU0zf6XYQ");
        map.put("eMkitAPIKey", "");

        // Retrofit Call
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());
        Call<EmkitResponse> response = mAPIService.getEmkitID(body);
        response.enqueue(new Callback<EmkitResponse>() {
            @Override
            public void onResponse(Call<EmkitResponse> call, retrofit2.Response<EmkitResponse> rawResponse) {
                try {
                    //Emkit response....
                    EmkitResponse res = rawResponse.body();

                    Log.d(TAG, "Retro Emkit Intialization: " + res.getEmp2UserId());
                    if (res.getEmp2UserId() != null && res.getEmp2UserId().length() > 0) {
                        assertTrue(true);
                    } else {
                        assertTrue(false);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EmkitResponse> call, Throwable throwable) {

                assertTrue(true);
            }
        });

    }

    @Test
    public void getAppUserID() {

        VenuetizeIdentityApiService mAPIService = VenuetizeEMVenueApiUtils.getAPIService();

        //Adding values into array map
        Map<String, Object> map = new ArrayMap<>();
        map.put("appUserId", "");
        map.put("deviceKey", "dmY7UEOHW2o:APA91bGG1M2tWV4xcEkRNhBBFY1sDvf-Na9kzNgdTmI2FKgxSnpCCVZQ_VNe3lM5mnh26kSBJP6RXiCY7NirS8nHpN9dmApVoQVg-3QkCC85GkibTlg9XgXzUlQWoVPDsNZdU0zf6XYQ");
        JSONObject jobject = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            jobject = new JSONObject();
            jobject.put("skidataUserId", 62);
            jobject.put("emkitUserId", "IPHONEeab1b5c7-1701-4e8a-be07-4a5ccc9a19e2");
            JSONObject jo = new JSONObject();
            jo.put("propertyName", "Email");
            jo.put("propertyValue", "aaron@email.com");
            ja.put(jo);

            jo = new JSONObject();
            jo.put("propertyName", "Name");
            jo.put("propertyValue", "AaronWatts");
            ja.put(jo);

            jo = new JSONObject();
            jo.put("propertyName", "memberType");
            jo.put("propertyValue", "Season Ticket Holder");
            ja.put(jo);

            jo = new JSONObject();
            jo.put("propertyName", "memberTypeCategory");
            jo.put("propertyValue", "Business Alliance");
            ja.put(jo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("userProfile", ja);
        map.put("externalUserIds", jobject);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());
        mAPIService.getAppUser("application/json", "10", "IPHONE88899b73-c501-4ac5-adca-716358d06dce", "IPHONE", "dolphins1402973360694", body).enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, retrofit2.Response<AppUser> response) {

                AppUser res = response.body();
                if (res.getAppUserId() != null) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {
                assertTrue(false);
            }
        });
    }
}
