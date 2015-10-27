/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.conversationsdk;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.moxtra.sdk.MXNotificationManager;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                boolean handled = MXNotificationManager.preProcessMXNotification(getApplicationContext(), intent);
                if (handled) {
                    // This is a moxtra message and it will be handled by moxtra
                    if (intent.getBooleanExtra(MXNotificationManager.MOXTRA_MESSAGE_SHOW_NOTIFICATION, false)) {
                        String title = intent.getStringExtra(MXNotificationManager.MOXTRA_MESSAGE_TITLE_TEXT);
                        if (intent.hasExtra(MXNotificationManager.MOXTRA_MESSAGE_ALERT_SOUND)) {
                            String soundUrl = intent.getStringExtra(MXNotificationManager.MOXTRA_MESSAGE_ALERT_SOUND);
                            Log.d(TAG, "soundUrl = " + soundUrl);
                            Uri uri = Uri.parse(soundUrl);
                            sendMoxtraNotification(title, uri, intent);
                        } else {
                            sendMoxtraNotification(title, intent);
                        }
                    }
                }else {
                    // Not a moxtra message and app should handle it.
                    Log.i(TAG, "App should handle it.");
                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendMoxtraNotification(String msg, Intent intent) {
        sendMoxtraNotification(msg, null, intent);
    }

    private void sendMoxtraNotification(String msg, Uri uri, Intent intent) {
        Log.d(TAG, "Got notification: msg = " + msg + ", uri = " + uri);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = MXNotificationManager.getMXNotificationIntent(this, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        if (uri != null) {
            mBuilder.setSound(uri).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        } else {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
