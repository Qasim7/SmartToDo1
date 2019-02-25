package com.example.qasim.smarttodo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    MediaPlayer mediaPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(context, "a")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle("Time of Task")
                .setContentText("alarm is running")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_beep);
        mediaPlayer.start();

    }

}
