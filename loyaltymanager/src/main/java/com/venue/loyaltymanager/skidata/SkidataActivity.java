package com.venue.loyaltymanager.skidata;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.venue.loyaltymanager.R;
import com.venue.loyaltymanager.skidata.holder.SkidataGetuserTypeNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataLoginNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataRegistrationNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataValidateUserNotifier;

/**
 * Created by manager on 08-01-2018.
 */

public class SkidataActivity extends Activity {

    String username = "kk@gamil.com";
    String password = "kk@gamil.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skidata);

        //Initializing UI
        initializeUI();

    }

    void initializeUI() {
        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  callingUserType();

                callingRegistraion();
            }
        });
    }
    void callingUserType()
    {
        SkidataManager.getInstance(SkidataActivity.this).getUserType(username, new SkidataGetuserTypeNotifier() {
            @Override
            public void onGetUserTypeSuccess(String result) {
                callingValidateUser(result);
            }

            @Override
            public void onGetUserTypeError() {

            }
        });
    }
    void callingRegistraion()
    {
        SkidataManager.getInstance(SkidataActivity.this).getUserRegistration(null,  new SkidataRegistrationNotifier() {
            @Override
            public void onRegistrationSuccess(String result) {
System.out.println("onRegistrationSuccess :: ");
            }

            @Override
            public void onRegistrationError() {
                System.out.println("onRegistrationError :: ");
            }


        });
    }

    void callingValidateUser(String result)
    {
        SkidataManager.getInstance(SkidataActivity.this).getValidateUser(username, result, new SkidataValidateUserNotifier() {
            @Override
            public void onValidateUserSuccess(String result) {
                callingLoginMethoed();
            }

            @Override
            public void onValidateUserError() {

            }
        });
    }

    void callingLoginMethoed()
    {
        SkidataManager.getInstance(SkidataActivity.this).getLoginUser(username, password, new SkidataLoginNotifier() {
            @Override
            public void onLogInSuccess(String result) {

            }

            @Override
            public void onLogInError() {

            }
        });
    }

}
