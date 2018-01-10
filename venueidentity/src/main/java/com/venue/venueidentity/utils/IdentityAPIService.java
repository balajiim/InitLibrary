package com.venue.venueidentity.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.google.gson.Gson;
import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venueidentity.model.AuthorizeToken;
import com.venue.venueidentity.model.UserData;
import com.venue.venueidentity.retrofit.VenuetizeIdentityApiService;
import com.venue.venueidentity.retrofit.VenuetizeWalletApiUtils;

import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class IdentityAPIService {

    Context mContext;
    private VenuetizeIdentityApiService mAPIService;
    String getAuthToken = "";

    SharedPreferences sharedpreferences;

    // Secret and Client Id values
    private String baseSecret = "v26K0gCOL99mRPwmiiq1ZtNpE3Ia";
    private String clientId = "LjtnGahDL66zzsGXIz3O8geFNWQa";
    String TENANT_DOMAIN = "i2asolutions.com";
    ProgressDialog progressDialog;

    public IdentityAPIService(Context context) {
        mContext = context;
    }

    //To get Authorize Token
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAuthorizeToken(UserData userdata, final boolean returnValue, String refreshToken, final GetAccessToken notifier) {

        mAPIService = VenuetizeWalletApiUtils.getAPIService();
        sharedpreferences = mContext.getSharedPreferences(VenuetizeIdentityManager.IDENTITYPREFERENCES, Context.MODE_PRIVATE);

        // Retrofit Call
        Call<AuthorizeToken> call;

        //To check Userdata vlaue, if it is null then we have to make Refresh token call else we have to make Access token call
        if (userdata == null) {
            Logger.d(TAG, "Refresh Token :: ");
            call = mAPIService.getAuthorizeRefreshToken(generateBasicAuthorizationHeader(), "refresh_token",
                    refreshToken);
        } else {
            Logger.d(TAG, "Access Token :: ");
            Logger.d(TAG, "Success userdata.getUserName() :: " + userdata.getUserName());
            Logger.d(TAG, "Success userdata.getPassword() :: " + userdata.getPassword());
            call = mAPIService.getAuthorizeToken(generateBasicAuthorizationHeader(),
                    "password", "openid", userdata.getUserName() + "@" + TENANT_DOMAIN, userdata.getPassword());
        }

        call.enqueue(new Callback<AuthorizeToken>() {
            @Override
            public void onResponse(Call<AuthorizeToken> call, retrofit2.Response<AuthorizeToken> response) {
                Logger.d(TAG, "Response code :: " + response.code());

                //Parsing string to JSONObject
                AuthorizeToken res = response.body();
                Gson gson = new Gson();
                Logger.d(TAG, "Success Response :: " + gson.toJson(res));

                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                }

                if (response.code() == 200) {
                    //Hardcoded Expirydate For Testing
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");

                    long time = System.currentTimeMillis();
                    long timeinms = Long.parseLong(res.getExpiresIn());
                    long times = timeinms * 1000;

                    Date date = new Date(time + times);

                    Logger.d(TAG, "time :: " + time);
                    Logger.d(TAG, "times :: " + times);
                    Logger.d(TAG, "times 123:: " + (time - times));

                    res.setExpiresIn("" + (time + times));

                    // Storing Access Token in local
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("AccessToken", gson.toJson(res));
                    editor.commit();

                    //  VenuetizeIdentityManager.getInstance(mContext).getAuthToken(mContext,notifier);

                    Logger.d(TAG, "Success " + res.getAccess_token());
                    getAuthToken = res.getAccess_token();

                    if (returnValue) {
                        notifier.getAccessTokenSuccess(res.getTokenType() + " " + getAuthToken);
                    }

                }

            }

            @Override
            public void onFailure(Call<AuthorizeToken> call, Throwable t) {
                Logger.e(TAG, "Unable to submit post to API." + t);

                if (returnValue) {
                    notifier.getAccessTokenFailure();
                }
            }

        });
    }


    public static OkHttpClient getOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String generateBasicAuthorizationHeader() {
        String code = clientId + ":" + baseSecret;
        return String.format(Locale.ENGLISH, "Basic %s", Base64.encodeToString(code.getBytes(), Base64.NO_WRAP));
    }
}
