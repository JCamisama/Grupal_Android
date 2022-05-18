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

/**
 *  Servicio para la música
 */

public class MyService extends Service {
    private final IBinder elBinder= new miBinder();
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return elBinder;
    }
    public MyService() {
    }

    /**
     *  Seleccionar la cancion deseada al crear el servicio
     */
    @Override
    public void onCreate() {
        super.onCreate();
        player=MediaPlayer.create(getApplicationContext(), R.raw.jumper);
    }
    /**
     *  Visualizar una notificación al iniciar el servicio
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        player.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager elmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel canalservicio = new NotificationChannel("IdCanal", "NombreCanal",NotificationManager.IMPORTANCE_LOW);
            elmanager.createNotificationChannel(canalservicio);
            Notification.Builder builder = new Notification.Builder(this, "IdCanal").setContentTitle("ShopWorld").setAutoCancel(false);
            Notification notification = builder.build();
            startForeground(1, notification);
        }

        return START_STICKY;
    }

    /**
     *  Enlazar el servicio
     */
    public class miBinder extends Binder {
        public MyService obtenServicio(){
            return MyService.this;
        }
    }
    /**
     *  Parar la música
     */
    public void parar(){
        player.pause();
    }

    /**
     *  Quitar la música
     */
    @Override
    public void onDestroy() {
        player.release();
    }
}
