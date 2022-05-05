package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RegisterActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        super.initializeMenuBar();
    }


    /**
     * Registra al usuario y lo redirige a la ventana Home si las credenciales
     * son adecuadas.
     * Posibles casos incorrectos: Campos vacíos, usuario existente,
     * repetición de contraseña incorrecta.
     */
    public void onClickRegister(View view) {
        Log.i("RegisterActivity", "I CLICKED ON THE REGISTER BUTTON!!!");
    }
}