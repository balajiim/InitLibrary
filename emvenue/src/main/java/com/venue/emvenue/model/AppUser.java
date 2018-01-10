package com.venue.emvenue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class AppUser {

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    @SerializedName("appUserId")
    @Expose
    private String appUserId;


}

