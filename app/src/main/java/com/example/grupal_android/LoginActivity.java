package com.example.grupal_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        super.initializeMenuBar();
    }

    /**
     * Se encarga de mostrar la ventana Register al clickar el texto.
     */
    public void onRegisterTextClicked(View view) {
        Log.i("LoginActivity", "I CLICKED ON THE REGISTER TEXT!!!");
        Intent intent = new Intent(getApplication(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Gestiona la petición de login. Si las credenciales son correctas,
     * manda al usuario a la ventana Home. De lo contrario, muestra un diálogo
     * como notificación.
     */
    public void onClickLogin(View view) {
        Log.i("LoginActivity", "I CLICKED ON THE LOGIN BUTTON!!!");
    }
}