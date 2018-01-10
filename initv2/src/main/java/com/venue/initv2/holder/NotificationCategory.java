package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 07-12-2017.
 */

public class NotificationCategory {
    @SerializedName("notificationCategoryValue")
    @Expose
    private String notificationCategoryValue;
    @SerializedName("notificationCategoryKey")
    @Expose
    private String notificationCategoryKey;

    public String getNotificationCategoryValue() {
        return notificationCategoryValue;
    }

    public void setNotificationCategoryValue(String notificationCategoryValue) {
        this.notificationCategoryValue = notificationCategoryValue;
    }

    public String getNotificationCategoryKey() {
        return notificationCategoryKey;
    }

    public void setNotificationCategoryKey(String notificationCategoryKey) {
        this.notificationCategoryKey = notificationCategoryKey;
    }


}
