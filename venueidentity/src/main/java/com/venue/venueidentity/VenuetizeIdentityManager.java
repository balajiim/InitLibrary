package com.venue.venueidentity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venueidentity.model.AuthorizeToken;
import com.venue.venueidentity.model.UserData;
import com.venue.venueidentity.retrofit.VenuetizeIdentityApiService;
import com.venue.venueidentity.utils.IdentityAPIService;
import com.venue.venueidentity.utils.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by balaji.M on 05-12-2017.
 */

public class VenuetizeIdentityManager {

    // static variable single_instance of type Singleton
    private static VenuetizeIdentityManager single_instance = null;
    Context mContext;
    private VenuetizeIdentityApiService mAPIService;
    public static final String IDENTITYPREFERENCES = "VenueIdentity";
    SharedPreferences sharedpreferences;

    public VenuetizeIdentityManager(Context context) {
        mContext = context;
    }

    // static method to create instance of Singleton class
    public static VenuetizeIdentityManager getInstance(Context context) {
        if (single_instance == null)
            single_instance = new VenuetizeIdentityManager(context);
        return single_instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initeMWallet(UserData userdata, GetAccessToken accessTokenNotifier) {
        Logger.i(TAG, "init_AuthToken");
        IdentityAPIService apiService = new IdentityAPIService(mContext);
        apiService.getAuthorizeToken(userdata, false, "", accessTokenNotifier);
    }

    //To Get Access Token
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAuthToken(Context context, GetAccessToken notifier, final Context payContext) {
        mContext = context;
        sharedpreferences = mContext.getSharedPreferences(VenuetizeIdentityManager.IDENTITYPREFERENCES, Context.MODE_PRIVATE);

        //To Check Saved access token value
        if (sharedpreferences.getString("AccessToken", null) != null) {

            //Getting AuthorizeToken object from saved string
            Gson gson = new Gson();
            AuthorizeToken authorizeToken = gson.fromJson(sharedpreferences.getString("AccessToken", null), AuthorizeToken.class);

            try {
                notifier.getAccessTokenSuccess(authorizeToken.getTokenType() + " " + authorizeToken.getAccess_token());
                Logger.d(TAG, "currentDate is equal to savedDate");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.i(TAG, "getAuthToken");

            if (payContext != null) {
                ((GetAccessToken) payContext).getAccessTokenFailure();
            } else {
                notifier.getAccessTokenFailure();
            }

        }


    }

    //To Get Access Token
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getRefreshToken(Context context, GetAccessToken notifier) {
        mContext = context;
        sharedpreferences = mContext.getSharedPreferences(VenuetizeIdentityManager.IDENTITYPREFERENCES, Context.MODE_PRIVATE);

        //To Check Saved access token value
        if (sharedpreferences.getString("AccessToken", null) != null) {
            //GetAccessToken accessToken = (GetAccessToken) mContext;

            //Getting AuthorizeToken object from saved string
            Gson gson = new Gson();
            AuthorizeToken authorizeToken = gson.fromJson(sharedpreferences.getString("AccessToken", null), AuthorizeToken.class);
            try {
                IdentityAPIService apiService = new IdentityAPIService(context);
                apiService.getAuthorizeToken(null, true, authorizeToken.getRefreshToken(), notifier);
                Logger.d(TAG, "currentDate is after savedDate");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.i(TAG, "getAuthToken");
        }
    }


}
