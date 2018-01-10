package com.venue.initv2.holder;

/**
 * Created by Puspak on 05-12-2017.
 */

public class DeviceInfo {
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String geteMkitAPIKey() {
        return eMkitAPIKey;
    }

    public void seteMkitAPIKey(String eMkitAPIKey) {
        this.eMkitAPIKey = eMkitAPIKey;
    }

    String requestUrl, deviceKey, eMkitAPIKey;
}
