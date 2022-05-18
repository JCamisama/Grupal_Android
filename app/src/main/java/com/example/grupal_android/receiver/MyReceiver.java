package com.example.grupal_android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.grupal_android.services.MyService;

public class MyReceiver extends BroadcastReceiver {

    TelephonyManager telManager;
    MyService elservicio;

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Log.d("Hola","Receive");
        telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        MyService.miBinder binder = (MyService.miBinder) peekService(context, new Intent(context, MyService.class));
        elservicio = binder.obtenServicio();

        //ya puedo llamar a los m�todos del servicio

    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        //Tel�fono sonando
                        Log.d("Hola","Hola");
                        elservicio.parar();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        //Tel�fono descolgado
                        elservicio.parar();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        //Tel�fono inactivo

                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception ex) {

            }
        }
    };
}