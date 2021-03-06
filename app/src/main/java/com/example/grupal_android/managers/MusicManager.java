package com.example.grupal_android.managers;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.preference.PreferenceManager;

import com.example.grupal_android.preferencias.CustomPreferences;
import com.example.grupal_android.receiver.MyReceiver;
import com.example.grupal_android.services.MyService;

import java.util.Locale;

public class MusicManager {
    private static MusicManager instance = null;
    private Context myContext = null;
    public static final String defaultLanguage = "en";
    private boolean bound = false;
    private Intent intent;
    private MyService elservicio;

    private ServiceConnection laconexion = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            elservicio = ((MyService.miBinder) service).obtenServicio();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            elservicio = null;
        }
    };

    /*** Singleton Pattern ***/
    private MusicManager(Context pContext) {
        this.myContext = pContext;

        //Filtro para saber el estado del movil y mandar una señal al recibidor
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        MyReceiver receiver = new MyReceiver();
        this.myContext.registerReceiver(receiver, filter);


        intent = new Intent(this.myContext, MyService.class);
    }

    public static synchronized MusicManager getInstance(Context pContext, LifecycleObserver lifecycleObserver) {
        if (MusicManager.instance == null) {
            MusicManager.instance = new MusicManager(pContext);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);


        }

        return MusicManager.instance;
    }
    /*************************/


    /**
     * Obtiene si el usuario quiere música o no y dependiendo del resultado se escuchara la canción o no
     */
    public void getMusic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.myContext);
        if (prefs.getBoolean("musica", false)) {
            this.onServicio();
        } else {
            this.onParar();
        }


    }
    /**
     *  Iniciar la música
     */
    public void onServicio() {
        this.myContext.bindService(intent, laconexion, Context.BIND_AUTO_CREATE);
        this.myContext.startService(intent);
        bound = true;
    }
    /**
     *  Parar la música
     */
    public void onParar() {
        if (bound) {
            this.myContext.unbindService(laconexion);
            this.myContext.stopService(intent);
            bound = false;
        }

    }

}

