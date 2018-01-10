package com.venue.loyaltymanager.skidata.holder;

/**
 * Created by manager on 08-01-2018.
 */

public interface SkidataValidateUserNotifier {

    void onValidateUserSuccess(String result);
    void onValidateUserError();

}
