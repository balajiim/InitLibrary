package com.venue.venueidentity.model;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class EmkitRequest {

    final String requestUrl;
    final String deviceKey;
    final String eMkitAPIKey;

    EmkitRequest(String requestUrl, String deviceKey, String eMkitAPIKey) {
        this.requestUrl = requestUrl;
        this.deviceKey = deviceKey;
        this.eMkitAPIKey = eMkitAPIKey;
    }
}
