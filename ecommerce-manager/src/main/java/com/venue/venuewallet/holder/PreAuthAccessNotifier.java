package com.venue.venuewallet.holder;

/**
 * Created by mastmobilemedia on 21-12-2017.
 */

public interface PreAuthAccessNotifier {

    public void onPreAuthSuccess(String HMacToken);

    public void onPreAuthFailure();

}
