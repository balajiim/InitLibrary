package com.venue.loyaltymanager.skidata.holder;

/**
 * Created by manager on 09-01-2018.
 */

public interface SkidataRegistrationNotifier {

    void onRegistrationSuccess(String result);
    void onRegistrationError();

}
