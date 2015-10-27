package com.example.conversationsdk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.conversationsdk.activity.RemoteNotificationActivity;
import com.moxtra.sdk.MXNotificationManager;
import com.moxtra.sdk.MXPushBaseIntentService;

/**
 * Created by Administrator on 2015/6/12.
 */
public class MXPushIntentService extends MXPushBaseIntentService {
    private static final String TAG = MXPushIntentService.class.getSimpleName();
    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 0x1111;

    @Override
    protected void onMessage(Context context, Intent intent) {

        boolean handled = MXNotificationManager.preProcessMXNotification(context, intent);
        if (handled) {
            // This is a moxtra message and it will be handled by moxtra
            if (intent.getBooleanExtra(MXNotificationManager.MOXTRA_MESSAGE_SHOW_NOTIFICATION, false)) {
                String title = intent.getStringExtra(MXNotificationManager.MOXTRA_MESSAGE_TITLE_TEXT);
                if (intent.hasExtra(MXNotificationManager.MOXTRA_MESSAGE_ALERT_SOUND)) {
                    String soundUrl = intent.getStringExtra(MXNotificationManager.MOXTRA_MESSAGE_ALERT_SOUND);
                    Log.d(TAG, "soundUrl = " + soundUrl);
                    Uri uri = Uri.parse(soundUrl);
                    sendMoxtraNotification(context, title, uri, intent);
                } else {
                    sendMoxtraNotification(context, title, intent);
                }
            }
        } else {
            // Not a moxtra message and app should handle it.
            Log.i(TAG, "App should handle it.");
        }
    }

    private void sendMoxtraNotification(Context context, String msg, Intent intent) {
        Log.d(TAG,"sendMoxtraNotification msg="+msg);
        sendMoxtraNotification(context, msg, null, intent);
    }

    private void sendMoxtraNotification(Context context, String msg, Uri uri, Intent intent) {
        Log.d(TAG, "sendMoxtraNotification Got notification: msg = " + msg + ", uri = " + uri);
        if (context == null)
            throw new RuntimeException("context can not be null");

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = getMXNotifiationIntent(context, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(context.getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        if (uri != null) {
            mBuilder.setSound(uri);
        }

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static PendingIntent getMXNotifiationIntent(Context context, Intent gcmIntent, int requestCode) {
        Log.d(TAG, "getMXNotifiationIntent requestCode=" + requestCode + " gcmIntent=" + gcmIntent.getExtras());
        Intent notificationIntent = new Intent(context, RemoteNotificationActivity.class);
        notificationIntent.putExtras(gcmIntent);
        PendingIntent intent = PendingIntent.getActivity(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return intent;
    }
}