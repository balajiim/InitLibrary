package com.venue.venuewallet;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.fragments.VenueWalletTabFragment;
import com.venue.venuewallet.holder.PayWalletNotifier;
import com.venue.venuewallet.holder.VenueWalletAccessTokenNotifier;
import com.venue.venuewallet.holder.VenueWalletAddCardNotifier;
import com.venue.venuewallet.holder.VenueWalletCardsListNotifier;
import com.venue.venuewallet.holder.VenueWalletConfigNotifier;
import com.venue.venuewallet.holder.VenueWalletEditCardNotifier;
import com.venue.venuewallet.model.EmvPaymentRequest;
import com.venue.venuewallet.retrofit.VenueWalletApiServices;
import com.venue.venuewallet.retrofit.VenueWalletApiUtils;
import com.venue.venuewallet.utils.WalletApiService;

import java.util.HashMap;

/**
 * Created by manager on 05-12-2017.
 */

public class VenueWalletManager implements GetAccessToken {

    Context mContext;
    public static VenueWalletManager venueWalletManager = null;
    private VenueWalletApiServices mAPIService;
    public static final String WALLETPREFERENCES = "VenueWallet";
    private String walletAPI = "";
    private String paymentId = "";

    private VenueWalletConfigNotifier venueWalletConfigNotifier;
    private VenueWalletCardsListNotifier venueWalletCardsListNotifier;
    private VenueWalletEditCardNotifier venueWalletEditCardNotifier;
    private VenueWalletAddCardNotifier venueWalletAddCardNotifier;
    private String cardId;
    private HashMap<String, Object> cardMap;
    public static PayWalletNotifier payChargeNotifier;
    public static EmvPaymentRequest paymentRequest;
    public static Context accessContext;


    public VenueWalletManager(Context context) {
        mContext = context;
    }

    public static VenueWalletManager getInstance(Context context) {
        if (venueWalletManager == null) {
            venueWalletManager = new VenueWalletManager(context);
        }
        return venueWalletManager;
    }

    public void callingWallet(FragmentActivity context) {
        getFragmentFromLibraryNonstack(context, VenueWalletTabFragment.newInstance());
    }

    public void callingPayWallet(FragmentActivity context) {
        accessContext = context;
        Intent dialogIntent = new Intent(mContext, MainWalletActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(dialogIntent);
    }

    public void payWitheMWallet(Context context, EmvPaymentRequest payRequest, PayWalletNotifier notifier) {
        payChargeNotifier = notifier;
        this.paymentRequest = payRequest;
        accessContext = context;
        Intent dialogIntent = new Intent(mContext, VenueWalletDialogActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(dialogIntent);
    }


    public void getFragmentFromContent(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void getFragmentFromContentNonstack(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content, fragment)
                .commit();
    }

    public void getFragmentFromContentNonstackreplace(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }

    public void getFragmentFromLibrary(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void getFragmentFromLibraryadd(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void getFragmentFromLibraryNonstack(FragmentActivity context, Fragment fragment) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Bundle args = new Bundle();
        //fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWalletConfigData(Context context, VenueWalletConfigNotifier notifier) {
        mContext = context;
        walletAPI = "getWalletConfigData";
        venueWalletConfigNotifier = notifier;
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWalletCards(Context context, VenueWalletCardsListNotifier notifier) {
        mContext = context;
        walletAPI = "getWalletCards";
        venueWalletCardsListNotifier = notifier;
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getCardDetails(Context context, String id, VenueWalletEditCardNotifier notifier) {
        mContext = context;
        walletAPI = "getCardDetails";
        venueWalletEditCardNotifier = notifier;
        cardId = id;
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWalletAddCardCard(Context context, HashMap<String, Object> map, VenueWalletAddCardNotifier notifier) {
        mContext = context;
        walletAPI = "getWalletAddCardCard";
        venueWalletAddCardNotifier = notifier;
        cardMap = map;
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWalletUpdateCard(Context context, String cardId, HashMap<String, Object> map, VenueWalletAddCardNotifier notifier) {
        mContext = context;
        this.cardId = cardId;
        cardMap = map;
        venueWalletAddCardNotifier = notifier;
        walletAPI = "getWalletUpdateCard";
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWalletDeleteCard(Context context, String cardId, VenueWalletAddCardNotifier notifier) {
        mContext = context;
        this.cardId = cardId;
        venueWalletAddCardNotifier = notifier;
        walletAPI = "getWalletDeleteCard";
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    public void getAuthToken(Context context, VenueWalletAccessTokenNotifier notifier) {
        mAPIService = VenueWalletApiUtils.getAPIService();
        WalletApiService service = new WalletApiService(context);
        service.getAuthorizeToken(notifier);
    }

    public void getWalletLogEvent(Context context, HashMap<String, Object> map) {
        mAPIService = VenueWalletApiUtils.getAPIService();
        WalletApiService service = new WalletApiService(context);
        service.getLogEvent(map);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getPreAuthOrder(Context context, String cardId) {
        mContext = context;
        walletAPI = "getPreAuthOrder";
        this.cardId = cardId;
        VenuetizeIdentityManager.getInstance(context).getAuthToken(context, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getChargeOrder(String Id) {
        walletAPI = "getChargeOrder";
        paymentId = Id;
        VenuetizeIdentityManager.getInstance(mContext).getAuthToken(mContext, VenueWalletManager.this, VenueWalletManager.accessContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getAccessTokenSuccess(String accessToken) {
        if (walletAPI == "getWalletConfigData") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getWalletConfigData(venueWalletConfigNotifier, accessToken);
        } else if (walletAPI == "getWalletCards") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getWalletCardData(venueWalletCardsListNotifier, accessToken);
        } else if (walletAPI == "getCardDetails") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getWalletSpecificCardData(cardId, venueWalletEditCardNotifier, accessToken);
        } else if (walletAPI == "getWalletAddCardCard") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getAddCard(cardMap, venueWalletAddCardNotifier, accessToken);
        } else if (walletAPI == "getWalletUpdateCard") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getUpdateCard(cardId, cardMap, venueWalletAddCardNotifier, accessToken);
        } else if (walletAPI == "getWalletDeleteCard") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getDeleteCard(cardId, venueWalletAddCardNotifier, accessToken);
        } else if (walletAPI == "getPreAuthOrder") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            service.getTransactionPreAuth(mContext, accessToken, cardId, paymentRequest);
        } else if (walletAPI == "getChargeOrder") {
            mAPIService = VenueWalletApiUtils.getAPIService();
            WalletApiService service = new WalletApiService(mContext);
            //service.getTransactionCharge(accessToken, cardId, paymentRequest);
            service.getCommittingTransactiondata(accessToken, paymentId);//To pass the id that is returned in the preauth response
        }
    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }

    public void callingWallet(FragmentActivity context, String from) {
        getFragmentFromLibraryNonstack(context, VenueWalletTabFragment.newInstance(from));
    }
}
