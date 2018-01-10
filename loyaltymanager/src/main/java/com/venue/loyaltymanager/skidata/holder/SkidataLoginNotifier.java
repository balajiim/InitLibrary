package com.venue.loyaltymanager.skidata.holder;

/**
 * Created by manager on 09-01-2018.
 */

public interface SkidataLoginNotifier {

    void onLogInSuccess(String result);
    void onLogInError();

}
