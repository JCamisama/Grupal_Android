package com.example.grupal_android.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.grupal_android.R;


public class MyService extends Service {
    private final IBinder elBinder= new miBinder();
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return elBinder;
    }
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player=MediaPlayer.create(getApplicationContext(), R.raw.monamour);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("aaaaaa","musicser");

        player.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager elmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel canalservicio = new NotificationChannel("IdCanal", "NombreCanal",NotificationManager.IMPORTANCE_LOW);
            elmanager.createNotificationChannel(canalservicio);
            Notification.Builder builder = new Notification.Builder(this, "IdCanal").setContentTitle(getString(R.string.app_name)).setAutoCancel(false);
            Notification notification = builder.build();
            startForeground(1, notification);
        }

        return START_STICKY;
    }


    public class miBinder extends Binder {
        public MyService obtenServicio(){
            return MyService.this;
        }
    }
    public void parar(){
        player.pause();
    }

    @Override
    public void onDestroy() {
        player.release();
    }
}
