package com.yodaapp.live.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.yodaapp.live.MessageViewSent;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;

/**
 * Created by PurpleFront02 on 30-09-2015.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static final int NOTIFICATION_ID = 1;
    String group_id="";
    String group_name="";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("This");
        group_id = data.getString("group_id");
        group_name = data.getString("group_name");


        Log.d(TAG, "From: " + from); // Gives the Google project id
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

try {
    if (group_name.equals("")) {
        sendNotification1(message);

    } else {
        sendNotification(message);
    }
}catch (Exception e){
    e.printStackTrace();
    sendNotification1(message);

}
}
        // [END_EXCLUDE]

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(),MessageViewSent.class);
        notificationIntent.putExtra("group_id",group_id);
        notificationIntent.putExtra("group_name",group_name);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);




        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification1(String message) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashScreen.class), 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }
}
