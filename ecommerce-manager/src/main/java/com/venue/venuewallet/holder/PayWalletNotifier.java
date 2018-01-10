package com.venue.venuewallet.holder;

/**
 * Created by mastmobilemedia on 21-12-2017.
 */

public interface PayWalletNotifier {

    public void onPayChargeSuccess();

    public void onPayChargeFailure();

    public void onPreAuthSuccess(String Id, String HMacToken);

    public void onPreAuthFailure();

}
