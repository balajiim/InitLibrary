package com.venue.venueidentity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class UserProfile {
    @SerializedName("propertyName")
    @Expose
    private String propertyName;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @SerializedName("propertyValue")
    @Expose
    private String propertyValue;


}
