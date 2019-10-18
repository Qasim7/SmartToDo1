package com.smarttodoapp.qasim.smarttodo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(context, "a")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle("Time of Task")
                .setContentText("alarm is running")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Uri.parse("android.resource://com.smarttodoapp.qasim.smarttodo/" + R.raw.beep_beep))
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);

    }

}
