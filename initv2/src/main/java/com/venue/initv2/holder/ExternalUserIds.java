package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 06-12-2017.
 */

public class ExternalUserIds {
    @SerializedName("skidataUserId")
    @Expose
    private String skidataUserId;
    @SerializedName("gimbalUserId")
    @Expose
    private String gimbalUserId;
    @SerializedName("dolphinsUserId")
    @Expose
    private String dolphinsUserId;

    public String getSkidataUserId() {
        return skidataUserId;
    }

    public void setSkidataUserId(String skidataUserId) {
        this.skidataUserId = skidataUserId;
    }

    public String getGimbalUserId() {
        return gimbalUserId;
    }

    public void setGimbalUserId(String gimbalUserId) {
        this.gimbalUserId = gimbalUserId;
    }

    public String getDolphinsUserId() {
        return dolphinsUserId;
    }

    public void setDolphinsUserId(String dolphinsUserId) {
        this.dolphinsUserId = dolphinsUserId;
    }

}

