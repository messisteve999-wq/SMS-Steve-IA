package com.steve.sms;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Bundle;

public class MessageNotificationListener extends NotificationListenerService {
    @Override public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;
        Bundle extras = sbn.getNotification().extras;
        if (extras == null) return;
        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        if (text == null || text.length() == 0) text = extras.getCharSequence(Notification.EXTRA_TEXT);
        if (text == null || text.length() == 0) return;
        String body = text.toString().trim();
        if (body.isEmpty()) return;
        // Keep only the latest readable notification locally on the device.
        getSharedPreferences("steve_ia", MODE_PRIVATE).edit()
            .putString("latest_message", body)
            .putString("latest_sender", title == null ? "" : title.toString())
            .putLong("latest_time", System.currentTimeMillis())
            .apply();
    }
}
