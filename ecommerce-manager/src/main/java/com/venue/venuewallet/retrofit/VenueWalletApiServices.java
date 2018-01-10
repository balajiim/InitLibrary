package com.venue.venuewallet.retrofit;

import com.venue.venuewallet.model.AuthorizeToken;
import com.venue.venuewallet.model.PreAuthResponse;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.model.VenueWalletConfigResponse;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by manager on 07-12-2017.
 */

public interface VenueWalletApiServices {

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<AuthorizeToken> getTokenFromUserAndPassword(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType, @Field("scope") String scope, @Field("username") String username,
            @Field("password") String password);

    @GET("end_user/merchants/applications/this/")
    Call<VenueWalletConfigResponse> getConfigFileData(@Header("Authorization") String header);

    @GET("end_user/payments/credit_cards/")
    Call<ArrayList<VenueWalletCardsData>> getWalletCards(@Header("Authorization") String header);

    @GET("end_user/payments/credit_cards/")
    Call<ArrayList<VenueWalletCardsData>> getWalletCardData(@Header("Authorization") String header, @Query("id") String id);

    /*@POST("payments/credit_cards/register/")
    Call<ResponseBody> getAddCard(@Header("Authorization") String header,@Body RequestBody params);*/
    @POST("end_user/payments/credit_cards/register")
    Call<ResponseBody> getAddCard(@Header("Authorization") String header, @Body HashMap<String, Object> body);

    @PUT("end_user/payments/credit_cards/{id}/update/")
    Call<ResponseBody> getEditCard(@Path("id") String cardId, @Header("Authorization") String header, @Body HashMap<String, Object> body);

    @DELETE("end_user/payments/credit_cards/{id}/")
    Call<ResponseBody> getDeleteCard(@Path("id") String cardId, @Header("Authorization") String header);

    @GET("end_user/transactions/transactions/")
    Call<ResponseBody> getTransactionsData(@Header("Authorization") String header);

    @POST("end_user/transactions/transactions/te2_preauth")
    Call<PreAuthResponse> getTransactionAuth(@Header("Authorization") String header, @Body RequestBody params);

    @POST("end_user/transactions/transactions/charge")
    Call<String> getTransactionCharge(@Header("Authorization") String header, @Body RequestBody params);

    @GET("end_user/transactions/transactions/{id}/")
    Call<ResponseBody> getPerticualrTransactionData(@Path("id") String transactionId);

    @POST("end_user/transactions/transactions/{id}/commit/")
    Call<ResponseBody> getCommittingTransaction(@Header("Authorization") String header, @Path("id") String transactionId);

    @POST("eMcard_V21/LogEventService")
    Call<ResponseBody> getLogEvent(@Body HashMap<String, Object> body);

}
