package com.venue.initv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;

import com.venue.initv2.holder.DeviceInfo;
import com.venue.initv2.holder.EmkitInitNotifier;
import com.venue.initv2.holder.IniteMkitDetails;
import com.venue.initv2.holder.LogEvent;
import com.venue.initv2.holder.NotificationCategory;
import com.venue.initv2.holder.NotificationDetails;
import com.venue.initv2.holder.NotificationResponse;
import com.venue.initv2.holder.UserAppData;
import com.venue.initv2.retrofit.ApiClient;
import com.venue.initv2.retrofit.ApiInterface;
import com.venue.initv2.utility.InitV2Utility;
import com.venue.initv2.utility.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by santhana on 12/19/17.
 */

public class EmkitInitMaster {

    public static EmkitInitMaster emkitInitMaster = null;
    IniteMkitDetails initeMkitDetails;
    ApiInterface apiService;
    private static Context mContext = null;
    public static boolean emkitInit = false;

    public EmkitInitMaster() {

    }

    public static EmkitInitMaster getInstance(Context context) {
        mContext = context;
        if (emkitInitMaster == null)
            emkitInitMaster = new EmkitInitMaster();
        return emkitInitMaster;
    }

    public void initEmkit(final String deviceToken, final EmkitInitNotifier notifier) {
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String devicetype = android.os.Build.DEVICE;
        String deviceModel = android.os.Build.MODEL;

        String requestUrl = mContext.getResources().getString(R.string.emkit_server_protocol) + mContext.getResources().getString(R.string.emkit_server_domainname) + "/" + mContext.getResources().getString(R.string.emkit_server_servletpkg)
                + "?app=emcard"
                + "&client=ANDROID&platform_id=(null)&device_name=" + devicetype
                + "&model=" + deviceModel + "&@2=&@3=1.0&@4=1.0&";
        final SharedPreferences pref = mContext.getSharedPreferences("emkit_info", 0);
        String emp2userid = pref.getString("emp2UserId", null);
        if (emp2userid != null) {
            String res = requestUrl;
            int n = res.indexOf("@2");
            String res1 = res.substring(0, n + 3);
            String res2 = res.substring(n + 3);
            String res3 = emp2userid;
            String res4 = res1 + res3 + res2;
            requestUrl = res4;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        if (deviceToken != null)
            deviceInfo.setDeviceKey(deviceToken);
        deviceInfo.seteMkitAPIKey(mContext.getResources().getString(R.string.emkitAPIKey));
        deviceInfo.setRequestUrl(requestUrl);

        Call<IniteMkitDetails> call = apiService.getInitEMkit(deviceInfo);
        call.enqueue(new Callback<IniteMkitDetails>() {
            @Override
            public void onResponse(Call<IniteMkitDetails> call, Response<IniteMkitDetails> response) {
                int statusCode = response.code();
                initeMkitDetails = response.body();
                notifier.emkitInitSuccess();
                emkitInit = true;
                Logger.d("Response", initeMkitDetails.getEmp2UserId());
                InitV2Utility.getInstance(mContext).saveIniteMkitDetails(initeMkitDetails);
                if (!pref.getBoolean("emkitInit", false)) {
                    //To perform the below only once. Put a check
                    updateSDKSettings(deviceToken);

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putBoolean("emkitInit", true);
                    edit.commit();
                }
            }

            @Override
            public void onFailure(Call<IniteMkitDetails> call, Throwable t) {
                // Log error here since request failed
                Logger.d("Response", "error");
                notifier.emkitInitFailure();
            }
        });
    }

    private void updateSDKSettings(final String deviceToken) {
        Call<NotificationResponse> notificationResponseCall = apiService.getNotificationStatus(initeMkitDetails.getEmp2UserId(), "1");
        notificationResponseCall.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                Logger.d("Response", response.toString());
                NotificationResponse notificationResponse = response.body();
                Logger.d("Response", notificationResponse.getStatus());

                Call<List<NotificationCategory>> notificationArrayCall = apiService.getNotificationCategories(mContext.getResources().getString(R.string.emkitAPIKey));
                notificationArrayCall.enqueue(new Callback<List<NotificationCategory>>() {
                    @Override
                    public void onResponse(Call<List<NotificationCategory>> call, Response<List<NotificationCategory>> response) {
                        Logger.d("Response", response.body().toString());
                        List<NotificationCategory> notificationResponse = response.body();

                        Logger.d("Response for ", "list of notification categories" + notificationResponse.size());

                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        for (int i = 0; i < notificationResponse.size(); i++) {
                            hashmap.put(notificationResponse.get(i).getNotificationCategoryKey(), "1");
                        }
                        String json = "{";
                        StringBuffer sb = new StringBuffer();
                        int i = 1;
                        Set<String> keys = hashmap.keySet();
                        Iterator<String> keyIter = keys.iterator();
                        while (keyIter.hasNext()) {
                            String key = keyIter.next();
                            String value1 = hashmap.get(key);
                            sb.append(" \"" + key + "\":" + Integer.parseInt(value1) + " ");
                            if (i != hashmap.size()) {
                                sb.append(",");
                            }
                            i += 1;
                        }
                        String jsonString = json + sb.toString() + "}";

                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
                        Call<NotificationDetails> notificationArrayCall = apiService.getNotificationDetails(initeMkitDetails.getEmp2UserId(), body);
                        notificationArrayCall.enqueue(new Callback<NotificationDetails>() {
                            @Override
                            public void onResponse(Call<NotificationDetails> call, Response<NotificationDetails> response) {
                                Logger.d("Responsexxxxxx", "mmm");
                                NotificationDetails notificationDetails = response.body();
                                //  Logger.d("Response", notificationDetails.getGameUpdates().toString());
                            }

                            @Override
                            public void onFailure(Call<NotificationDetails> call, Throwable t) {
                                Logger.d("Responsexxxxxx", "mmm");

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<NotificationCategory>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                // Log error here since request failed
                Logger.d("Response", "error");

            }
        });
    }

    public void updateUserWithEmkit(String appUserId) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("appUserId", appUserId);
        map.put("deviceKey", EmkitInstance.getInstance(mContext).getDeviceId());
        JSONObject jobject = new JSONObject();
        JSONArray ja = new JSONArray();

        map.put("userProfile", ja);
        map.put("externalUserIds", jobject);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());

        Call<UserAppData> userAppDataCall = apiService.getUpdateUser(initeMkitDetails.getEmp2UserId(), body);
        userAppDataCall.enqueue(new Callback<UserAppData>() {
            @Override
            public void onResponse(Call<UserAppData> call, Response<UserAppData> response) {
                Logger.d("Response", response.toString());
                UserAppData notificationResponse = response.body();


            }

            @Override
            public void onFailure(Call<UserAppData> call, Throwable t) {
                // Log error here since request failed
                Logger.d("Response", "error");

            }
        });
    }

    private void emkitLogEvent() {
        //generalize the log event call. Or reuse the logevent present in wallet
        LogEvent logEvent = new LogEvent();
        logEvent.setEmp2UserId("IPHONEd9196d33-41a2-4579-82b5-2284ba020513");
        logEvent.setEventCategory("Notification_Open");
        logEvent.setEventType("PROXIMITY_PUSH");
        logEvent.setEventValue("920");
        logEvent.setScreenReference("sss");
        logEvent.setSessionId("123");
        Call<NotificationResponse> logevent = apiService.getLogEvent(logEvent);
        logevent.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                Logger.d("Responsexxxxxx", "noti");
                //Call the update user call
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
    }

}
