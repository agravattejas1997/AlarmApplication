package com.learn.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Integer alarmHour,alarmMinute;

    Intent intent;
    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

        intent = new Intent(this,MyService.class);
        timePicker = findViewById(R.id.timepicker);

        serviceCall(intent);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                serviceCall(intent);

                Toast.makeText(getApplicationContext()," H : "+alarmHour+" M : "+alarmMinute,Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void serviceCall(Intent intent)
    {
        alarmHour= timePicker.getCurrentHour();
        alarmMinute = timePicker.getCurrentMinute();

        stopService(intent);
        intent.putExtra("alarmHour",alarmHour);
        intent.putExtra("alarmMinute",alarmMinute);
        startService(intent);
    }
}
