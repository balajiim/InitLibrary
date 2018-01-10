package com.venue.venuewallet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.holder.PayWalletNotifier;
import com.venue.venuewallet.holder.VenueWalletAccessTokenNotifier;
import com.venue.venuewallet.holder.VenueWalletAddCardNotifier;
import com.venue.venuewallet.holder.VenueWalletCardsListNotifier;
import com.venue.venuewallet.holder.VenueWalletConfigNotifier;
import com.venue.venuewallet.holder.VenueWalletEditCardNotifier;
import com.venue.venuewallet.model.AuthorizeToken;
import com.venue.venuewallet.model.EmvPaymentItem;
import com.venue.venuewallet.model.EmvPaymentRequest;
import com.venue.venuewallet.model.PreAuthResponse;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.model.VenueWalletConfigResponse;
import com.venue.venuewallet.retrofit.VenueWalletApiServices;
import com.venue.venuewallet.retrofit.VenueWalletApiUtils;
import com.venue.venuewallet.retrofit.VenueWalletEmkitUtils;
import com.venue.venuewallet.retrofit.VenueWalletTokenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by manager on 07-12-2017.
 */

public class WalletApiService {

    String TAG = WalletApiService.class.getSimpleName();
    Context mContext;
    Context notifierContext;
    private VenueWalletApiServices mAPIService;
    SharedPreferences sharedpreferences;

    public WalletApiService(Context context) {
        mContext = context;
    }

    private String baseSecret = "v26K0gCOL99mRPwmiiq1ZtNpE3Ia";
    private String clientId = "LjtnGahDL66zzsGXIz3O8geFNWQa";
    String TENANT_DOMAIN = "i2asolutions.com";

    //To get Authorize Token
    public void getAuthorizeToken(final VenueWalletAccessTokenNotifier notifier) {

        mAPIService = VenueWalletTokenUtils.getAPIService();
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);

        //Do not hardcode the username and pwd. Get it from the object that is passed from main app and use it.

        // Retrofit Call
        mAPIService.getTokenFromUserAndPassword(generateBasicAuthorizationHeader(),
                "password", "openid", "pkrakowiak@i2asolutions.com" + "@" + TENANT_DOMAIN, "Password1!").enqueue(new Callback<AuthorizeToken>() {
            @Override
            public void onResponse(Call<AuthorizeToken> call, retrofit2.Response<AuthorizeToken> response) {
                Log.e(TAG, "Success");
                AuthorizeToken res = response.body();
                if (res != null) {
                    Gson gson = new Gson();
                    Log.e(TAG, "AcessToken Success " + gson.toJson(res));
                    // Storing AccesToken in local
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("AcessToken", res.getTokenType() + " " + res.getAccessToken());
                    editor.commit();

                    // Sending Success response to mywallet fragment
                    notifier.onAccessTokenSuccess();
                }
            }

            @Override
            public void onFailure(Call<AuthorizeToken> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API." + t);
                // Sending Failure response to mywallet fragment
                notifier.onAccessTokenFailure();
            }
        });
    }

    //To get Wallet CardsList
    public void getWalletConfigData(final VenueWalletConfigNotifier notifier, String accessToken) {
        // Retrofit Call
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        Log.e(TAG, "getWalletConfigData acessToken" + accessToken);
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getConfigFileData(accessToken).enqueue((new Callback<VenueWalletConfigResponse>() {
            @Override
            public void onResponse(Call<VenueWalletConfigResponse> call, retrofit2.Response<VenueWalletConfigResponse> response) {
                Log.e(TAG, "getWalletConfigData Success" + response.code());
                if (response.code() == 401 || response.code() == 403) {

                    notifier.getRefreshTokenFailure();
                }
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.e(TAG, "getWalletConfigData res" + response.body().getConfig_dict().getCreditCards().get(0).getCardDescription());
                        // Sending Success response to mywallet fragment
                        notifier.onConfigDataSuccess(response.body());
                    } else {
                        // Sending Failure response to mywallet fragment
                        notifier.ononfigDataFailure();
                    }
                } else {
                    notifier.ononfigDataFailure();
                }
            }

            @Override
            public void onFailure(Call<VenueWalletConfigResponse> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to mywallet fragment
                notifier.ononfigDataFailure();
            }
        }));

    }

    //To get Wallet CardsList
    public void getWalletCardData(final VenueWalletCardsListNotifier notifier, String accessToken) {
        // Retrofit Call
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        Log.e(TAG, "walletcards acessToken" + accessToken);
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getWalletCards(accessToken).enqueue((new Callback<ArrayList<VenueWalletCardsData>>() {
            @Override
            public void onResponse(Call<ArrayList<VenueWalletCardsData>> call, retrofit2.Response<ArrayList<VenueWalletCardsData>> response) {
                Log.e(TAG, "walletcards Success" + response.code());
                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                } else {
                    if (response.body() != null && response.body().size() > 0) {
                        Log.e(TAG, "walletcards size" + response.body().get(0).getFirst_name());
                        // Sending Success response to mywallet fragment
                        notifier.onCardsListSuccess(response.body());
                    } else {
                        // Sending Failure response to mywallet fragment
                        notifier.onCardsListFailure();
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<VenueWalletCardsData>> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to mywallet fragment
                notifier.onCardsListFailure();
            }
        }));

    }

    //To get perticular Card Data
    public void getWalletSpecificCardData(final String id, final VenueWalletEditCardNotifier notifier, String accessToken) {
        // Retrofit Call
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        Log.e(TAG, "getWalletSpecificCardData acessToken" + accessToken);
        Log.e(TAG, "getWalletSpecificCardData id" + id);
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getWalletCardData(accessToken, id).enqueue((new Callback<ArrayList<VenueWalletCardsData>>() {
            @Override
            public void onResponse(Call<ArrayList<VenueWalletCardsData>> call, retrofit2.Response<ArrayList<VenueWalletCardsData>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                }
                if (response.code() == 200) {
                    Log.e(TAG, "getWalletSpecificCardData Success" + response.toString());
                    if (response.body() != null) {
                        Log.e(TAG, "getWalletSpecificCardData Success string " + response.body().size());
                        // Sending Success response to AddFragment fragment
                        for (int i = 0; i < response.body().size(); i++) {
                            if (id.equals(response.body().get(i).getId())) {
                                notifier.onEditCardSuccess(response.body().get(i));
                                break;
                            }
                        }
                    }
                } else {
                    // Sending Failure response to AddFragment fragment
                    notifier.onEditCardsFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VenueWalletCardsData>> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to AddFragment fragment
                notifier.onEditCardsFailure();
            }
        }));

    }

    //To add Cards
    public void getAddCard(HashMap<String, Object> map, final VenueWalletAddCardNotifier notifier, String accessToken) {
        Log.i(TAG, "getAddCard map value" + map.toString());
        mAPIService = VenueWalletApiUtils.getAPIService();
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        mAPIService.getAddCard(accessToken, map).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                } else {
                    Log.e(TAG, "getAddCard Success" + response.body());
                    if (response.body() != null) {
                        Log.e(TAG, "getAddCard Success string " + response.body().toString());
                        // Sending Success response to AddFragment fragment
                        notifier.onAddCardSuccess();
                    } else {
                        // Sending Failure response to AddFragment fragment
                        notifier.onAddCardsFailure();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to AddFragment fragment
                notifier.onAddCardsFailure();
            }
        }));

    }

    //To update Cards
    public void getUpdateCard(String cardId, HashMap<String, Object> map, final VenueWalletAddCardNotifier notifier, String accessToken) {
        mAPIService = VenueWalletApiUtils.getAPIService();
        // Retrofit Call
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        Log.i(TAG, "getUpdateCard id" + cardId);
        Log.i(TAG, "getUpdateCard map" + map.toString());
        mAPIService.getEditCard(cardId, accessToken, map).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "getUpdateCard success" + response.code());
                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                } else {
                    Log.e(TAG, "getUpdateCard success" + response.toString());
                    if (response.body() != null) {
                        Log.e(TAG, "getUpdateCard Success string " + response.body().toString());
                        // Sending Success response to AddFragment fragment
                        notifier.onAddCardSuccess();
                    } else {
                        // Sending Failure response to AddFragment fragment
                        Log.e(TAG, "getUpdateCard failure" + response.toString());
                        notifier.onAddCardsFailure();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to AddFragment fragment
                notifier.onAddCardsFailure();
            }
        }));

    }

    //To delete Card
    public void getDeleteCard(String cardId, final VenueWalletAddCardNotifier notifier, String accessToken) {
        mAPIService = VenueWalletApiUtils.getAPIService();
        // Retrofit Call
        // Getting AccesToken from local
        sharedpreferences = mContext.getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        //accessToken = sharedpreferences.getString("AcessToken", "");
        Log.i(TAG, "getUpdateCard id" + cardId);
        mAPIService.getDeleteCard(cardId, accessToken).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 401 || response.code() == 403) {
                    notifier.getRefreshTokenFailure();
                } else {
                    Log.e(TAG, "getUpdateCard success" + response.toString());
                    if (response.body() != null) {
                        Log.e(TAG, "getUpdateCard Success string " + response.body().toString());
                        // Sending Success response to AddFragment fragment
                        notifier.onAddCardSuccess();
                    } else {
                        // Sending Failure response to AddFragment fragment
                        notifier.onAddCardsFailure();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                // Sending Failure response to AddFragment fragment
                notifier.onAddCardsFailure();
            }
        }));

    }

    //To get TransactionList
    public void getTransactiondata() {
        // Retrofit Call
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getTransactionsData("3oFXvdFSYztLGAG61mSt6xc87HzO1Xd6qjK22LUQXOzv4pkjkM23LiRTsNay2RAC").enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "Success" + response.body());
                //    ArrayList<VenueWalletCardsData> res = (ArrayList<VenueWalletCardsData>)response;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
            }
        }));

    }

    //To Transcation pre auth
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTransactionPreAuth(Context context, String accessToken, String cardID, EmvPaymentRequest request) {
        mAPIService = VenueWalletApiUtils.getAPIService();
        mContext = context;

        EmvPaymentItem emvPaymentItem​ = new EmvPaymentItem("2​ ​PC​ ​chicken​ ​menu", "20");

        JSONObject jobject = new JSONObject();
        JSONArray ja = new JSONArray();
        //Adding values into array map
        Map<String, Object> jsonParams = new ArrayMap<>();
        JSONObject obj = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONArray array = new JSONArray();
            try {
                obj.put("name", "2 chicken menu");
                obj.put("qty", 2);
                obj.put("unit_price", 15);
                obj.put("price", 30);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "payment items " + request.getEmvOrderDetails().getItems().size());
        // ja.put(obj);

        jsonParams.put("items", new Gson().toJson(request.getEmvOrderDetails().getItems()));
        jsonParams.put("credit_card", cardID);//pass the selected card id from wallet
        jsonParams.put("total_amount", request.getEmvOrderDetails().getTotalAmount());
        jsonParams.put("point_of_sale", "2");
        jsonParams.put("order_identifier", "" + request.getEmvOrderDetails().getOrderID());
        jsonParams.put("place_id", request.getEmvOrderDetails().getPlaceId());
        jsonParams.put("ordering_engine", 2);

        Log.e(TAG, "accessToken" + accessToken);
        Log.e(TAG, "new JSONObject(map) 111111" + (new JSONObject(jsonParams)).toString());

        final PayWalletNotifier notifier = VenueWalletManager.payChargeNotifier;
        // Retrofit Call
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(jsonParams));
        mAPIService.getTransactionAuth(accessToken, body).enqueue((new Callback<PreAuthResponse>() {
            @Override
            public void onResponse(Call<PreAuthResponse> call, retrofit2.Response<PreAuthResponse> response) {

                Gson gson = new Gson();

                PreAuthResponse res = (PreAuthResponse) response.body();

                ((Activity) mContext).finish();

                try {
                    if (response.code() == 400) {
                        notifier.onPreAuthFailure();
                    }

                    if (response.code() == 200) {
                        if (res.getHmac_token() != null)
                            notifier.onPreAuthSuccess(res.getId(), res.getHmac_token());
                        else
                            notifier.onPreAuthFailure();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PreAuthResponse> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API." + t.getMessage());
                notifier.onPreAuthFailure();
            }
        }));

    }

    //To Transcation Charge
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTransactionCharge(String accessToken, String cardID, EmvPaymentRequest request) {
        mAPIService = VenueWalletApiUtils.getAPIService();

        EmvPaymentItem emvPaymentItem​ = new EmvPaymentItem("2​ ​PC​ ​chicken​ ​menu", "20");

        JSONObject jobject = new JSONObject();
        JSONArray ja = new JSONArray();
        //Adding values into array map
        Map<String, Object> jsonParams = new ArrayMap<>();
        JSONObject obj = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONArray array = new JSONArray();
            try {
                obj.put("name", "2 chicken menu");
                obj.put("qty", 2);
                obj.put("unit_price", 15);
                obj.put("price", 30);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "payment items " + request.getEmvOrderDetails().getItems().size());
        // ja.put(obj);

        jsonParams.put("items", new Gson().toJson(request.getEmvOrderDetails().getItems()));
        jsonParams.put("credit_card", cardID);//pass the selected card id from wallet
        jsonParams.put("total_amount", request.getEmvOrderDetails().getTotalAmount());
        jsonParams.put("point_of_sale", "2");
        jsonParams.put("uuid", UUID.randomUUID().toString());
        jsonParams.put("order_identifier", "" + request.getEmvOrderDetails().getOrderID());
        jsonParams.put("place_id", request.getEmvOrderDetails().getPlaceId());
        jsonParams.put("ordering_engine", 2);

        Log.e(TAG, "accessToken" + accessToken);
        Log.e(TAG, "new JSONObject(map) 111111" + (new JSONObject(jsonParams)).toString());

        final PayWalletNotifier notifier = VenueWalletManager.payChargeNotifier;
        // Retrofit Call
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(jsonParams));
        mAPIService.getTransactionCharge(accessToken, body).enqueue((new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e(TAG, "Success" + response.toString());
                Log.e(TAG, "Success" + response.code());


                if (response.code() == 400) {
                    notifier.onPayChargeFailure();
                }

                if (response.code() == 200) {
                    notifier.onPayChargeSuccess();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
                notifier.onPayChargeFailure();
            }
        }));

    }

    //To get perticualr Transaction Data
    public void getPerticualrTransactiondata() {
        // Retrofit Call
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getPerticualrTransactionData("23").enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "Success" + response.body());
                //    ArrayList<VenueWalletCardsData> res = (ArrayList<VenueWalletCardsData>)response;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
            }
        }));

    }

    //To get Committing transcation Data
    public void getCommittingTransactiondata(String accessToken, String id) {
        // Retrofit Call
        mAPIService = VenueWalletApiUtils.getAPIService();
        mAPIService.getCommittingTransaction(accessToken, id).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "Success" + response.body());
                //    ArrayList<VenueWalletCardsData> res = (ArrayList<VenueWalletCardsData>)response;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
            }
        }));

    }

    //To add Cards
    public void getLogEvent(HashMap<String, Object> map) {
        //Retorfit call..
        Log.i(TAG, "getLogEvent map value" + map.toString());
        mAPIService = VenueWalletEmkitUtils.getAPIService();
        mAPIService.getLogEvent(map).enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, "getLogEvent Success" + response.body());
                if (response.body() != null) {
                    Log.e(TAG, "getLogEvent Success string " + response.body().toString());
                } else {
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit Get to API.");
            }
        }));

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
