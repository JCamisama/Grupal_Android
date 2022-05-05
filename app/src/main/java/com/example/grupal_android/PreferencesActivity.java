package com.example.grupal_android;

import android.os.Bundle;
import android.view.View;

/**
 * Esta actividad muestra la ventana de preferencias en la que se puede
 * modificar el idioma de la aplicación.
 */
public class PreferencesActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void onClickDone(View view) {
        finish();
    }
}