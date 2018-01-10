package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 08-12-2017.
 */

public class Device {

    @SerializedName("deviceKey")
    @Expose
    private String deviceKey;
    @SerializedName("androidId")
    @Expose
    private String androidId;

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }


}
