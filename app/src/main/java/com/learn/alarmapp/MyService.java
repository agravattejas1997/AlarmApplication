package com.learn.alarmapp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    int alarmHour,alarmMinute;
    Ringtone ringtone;
    Timer timer = new Timer();

    static final  String CHANNELID ="MyChannelId";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        alarmHour = intent.getIntExtra("alarmHour",0);
        alarmMinute = intent.getIntExtra("alarmMinute",0);

        try {
                Intent notificationIntent = new Intent(this,MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
                Notification notification = new NotificationCompat.Builder(this,CHANNELID)
                        .setContentTitle("My Alarm Clock")
                        .setContentText("Time : H -> "+alarmHour+" M-> "+alarmMinute)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .build();

                startForeground(1,notification);

                NotificationChannel notificationChannel = new NotificationChannel(CHANNELID,"My Alarm Clock Service", NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);

        }catch (Exception e) {
            e.printStackTrace();
        }

        ringtone = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
             public void run() {
                if (Calendar.getInstance().getTime().getHours()==alarmHour && Calendar.getInstance().getTime().getMinutes()==alarmMinute)
                {
                    ringtone.play();
                }
                else
                {
                    ringtone.stop();
                }
            }
        },0,1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ringtone.stop();
        timer.cancel();
    }
}
