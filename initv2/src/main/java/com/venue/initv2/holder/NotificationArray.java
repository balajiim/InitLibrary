package com.venue.initv2.holder;

import java.util.List;

/**
 * Created by Puspak on 07-12-2017.
 */

public class NotificationArray {
    public List<NotificationCategory> getNotificationCategories() {
        return notificationCategories;
    }

    public void setNotificationCategories(List<NotificationCategory> notificationCategories) {
        this.notificationCategories = notificationCategories;
    }

    List<NotificationCategory> notificationCategories;
}
