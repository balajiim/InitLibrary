package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 28-12-2017.
 */

public class PreauthOperationDetails {

    @SerializedName("id")
    private String id;
    @SerializedName("timestamp")
    private String timestamp;

    public PreauthOperationDetails getMetadata() {
        return metadata;
    }

    public void setMetadata(PreauthOperationDetails metadata) {
        this.metadata = metadata;
    }

    @SerializedName("metadata")
    private PreauthOperationDetails metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    private String token;

}
