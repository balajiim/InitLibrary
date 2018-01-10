package com.venue.loyaltymanager.skidata;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.venue.loyaltymanager.skidata.holder.SkidataGetuserTypeNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataLoginNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataRegistrationNotifier;
import com.venue.loyaltymanager.skidata.holder.SkidataValidateUserNotifier;
import com.venue.loyaltymanager.skidata.model.ProfilePropertiesItem;
import com.venue.loyaltymanager.skidata.model.UserRegistrationInfo;
import com.venue.loyaltymanager.skidata.retrofit.SkidataApiServices;
import com.venue.loyaltymanager.skidata.retrofit.SkidataApiUtils;
import com.venue.loyaltymanager.skidata.utils.SkidataApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by manager on 08-01-2018.
 */

public class SkidataManager {

    public static SkidataManager skidataManager = null;
    Context mContext;
    private SkidataApiServices mAPIService;

    public SkidataManager(Context context) {
        mContext = context;
    }

    public static SkidataManager getInstance(Context context) {
        if (skidataManager == null) {
            skidataManager = new SkidataManager(context);
        }
        return skidataManager;
    }


    public void getUserType(String userName,SkidataGetuserTypeNotifier notifier) {
        mAPIService = SkidataApiUtils.getAPIService();
        SkidataApiService service = new SkidataApiService(mContext);
        service.getUserType(userName,notifier);
    }

    public void getValidateUser(String userName,String userResponse,SkidataValidateUserNotifier notifier)
    {
        SkidataApiService service = new SkidataApiService(mContext);
        service.getValidateUser(userName,userResponse,notifier);
    }

    public void getLoginUser(String userName,String password,SkidataLoginNotifier notifier)
    {
        SkidataApiService service = new SkidataApiService(mContext);
        service.getLoginUser(userName,password,notifier);
    }

    public void getUserRegistration(UserRegistrationInfo info,SkidataRegistrationNotifier notifier)
    {
        String firstName = "Balaji123";
        String lastName = "M12";
        String email = "mbalaji.java@gmail.com123";
        String password = "test12312";
        String zipcode = "560076";
        String City = "Bangalore";
        String birthday = "";
        String unit = "";
        String Region = "";
        String Telephone = "";

        UserRegistrationInfo regInfo = new UserRegistrationInfo();
        regInfo.setDisplayName(firstName + " " + lastName);
        regInfo.setEmail(email);
        regInfo.setPassword(password);
        regInfo.setUsername(email);
        regInfo.setCreateSeatEntities("true");//This is added to bypass the verification. This is accompanied with the harcoded admin authorization in the header.
        regInfo.setSendAdminRegistrationEmail("true");
        regInfo.setSendAdminVerificationEmail("true");

        String USERNAME = email;
        String PASSWORD = password;
        ProfilePropertiesItem propItem1 = new ProfilePropertiesItem();
        //propItem1.setPropertyCategory("Name");
        propItem1.setPropertyName("FirstName");
        propItem1.setPropertyValue(firstName);

        ProfilePropertiesItem propItem2 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem2.setPropertyName("LastName");
        propItem2.setPropertyValue(lastName);

        ProfilePropertiesItem propItem3 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem3.setPropertyName("PassWord");
        propItem3.setPropertyValue(password);

        ProfilePropertiesItem propItem4 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem4.setPropertyName("eMail");
        propItem4.setPropertyValue(email);

        ProfilePropertiesItem propItem5 = new ProfilePropertiesItem();
        //propItem3.setPropertyCategory("Address");
        propItem5.setPropertyName("PostalCode");
        propItem5.setPropertyValue(zipcode);

        ProfilePropertiesItem propItem6 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem6.setPropertyName("BirthDate");
        propItem6.setPropertyValue(birthday);

        ProfilePropertiesItem propItem7 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem7.setPropertyName("Unit");
        propItem7.setPropertyValue(unit);

        ProfilePropertiesItem propItem8 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem8.setPropertyName("City");
        propItem8.setPropertyValue(City);

        ProfilePropertiesItem propItem9 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem9.setPropertyName("Region");
        propItem9.setPropertyValue(Region);

        ProfilePropertiesItem propItem10 = new ProfilePropertiesItem();
        //propItem2.setPropertyCategory("Name");
        propItem10.setPropertyName("Telephone");
        propItem10.setPropertyValue(Telephone);
        boolean isExtSource = false;

        List<ProfilePropertiesItem> profileProperties = new ArrayList<ProfilePropertiesItem>();
        profileProperties.add(propItem1);
        profileProperties.add(propItem2);
        profileProperties.add(propItem3);
        profileProperties.add(propItem4);
        profileProperties.add(propItem5);
        profileProperties.add(propItem6);
        profileProperties.add(propItem7);
        profileProperties.add(propItem8);
        profileProperties.add(propItem9);
        profileProperties.add(propItem10);

        regInfo.setProfileProperties(profileProperties);

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();


        Log.i(TAG, "gson.toJson(regInfo)::" + gson.toJson(regInfo));
        SkidataApiService service = new SkidataApiService(mContext);
        service.getUserRegistration(regInfo,notifier);
    }
}
