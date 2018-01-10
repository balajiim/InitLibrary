package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Puspak on 06-12-2017.
 */

public class AppUserId {
    @SerializedName("externalUserIds")
    @Expose
    private ExternalUserIds externalUserIds;
    @SerializedName("userProfile")
    @Expose
    private List<UserProfile> userProfile = null;

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

}

