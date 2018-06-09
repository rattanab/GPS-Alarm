package com.example.rattanab.gpsalarmv3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import static com.example.rattanab.gpsalarmv3.R.*;
import static com.example.rattanab.gpsalarmv3.R.raw.alarm;

public class AlarmReceiver extends BroadcastReceiver {
//    Ringtone ringtone;
    MediaPlayer myPlayer;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BE ALARM!", Toast.LENGTH_LONG).show();
        myPlayer = MediaPlayer.create(context, raw.alarm);
//        myPlayer.setLooping(true); // Set looping
        myPlayer.start();
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        ringtone.play();
    }

}
