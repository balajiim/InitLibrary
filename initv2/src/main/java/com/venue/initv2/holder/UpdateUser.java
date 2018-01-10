package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Puspak on 08-12-2017.
 */

public class UpdateUser {
    @SerializedName("externalUserIds")
    @Expose
    private ExternalUserIds externalUserIds;
    @SerializedName("userProfile")
    @Expose
    private List<UserProfile> userProfile = null;
    @SerializedName("device")
    @Expose
    private Device device;

    public ExternalUserIds getExternalUserIds() {
        return externalUserIds;
    }

    public void setExternalUserIds(ExternalUserIds externalUserIds) {
        this.externalUserIds = externalUserIds;
    }

    public List<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(List<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
