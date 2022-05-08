package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.grupal_android.PreferencesActivity;

import com.example.grupal_android.managers.CustomPreferencesManager;
import com.example.grupal_android.managers.LanguageManager;
import com.example.grupal_android.managers.SessionManager;
import com.example.grupal_android.models.User;
import com.example.grupal_android.workers.AllUsersGetter;
import com.example.grupal_android.workers.UserInsertWorker;

public class MainActivity extends AppCompatActivity {

    protected String currentActivityLanguage = "";
    protected LanguageManager languageManager = null;
    protected CustomPreferencesManager preferencesManager = null;
    protected SessionManager sessionManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        Log.i("MainActivity", "El peluca sappeeeeeeeeeeeeeeeeeeeeee!");
=======

        this.languageManager = LanguageManager.getInstance(MainActivity.this);
        this.preferencesManager = CustomPreferencesManager.getInstance(MainActivity.this);
        this.sessionManager = SessionManager.getInstance(MainActivity.this);
        this.manageCurrentAppLanguage();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Comprobar si al volver a la pantalla ha cambiado el idioma.
        if (this.languageManager.hasLanguageChanged(this.currentActivityLanguage)) {
            finish();
            startActivity(getIntent());
        }
    }


    /**
     * Se utiliza en todas las actividades herederas para poder inicializar el menú
     * de la aplicación.
     */
    protected void initializeMenuBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * Asigna y maneja la pulsación del menú a la opción correspondiente.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.preferences: {
                // Será necesario reiniciar la actividad desde la cual se haya cambiado el idioma.
                Intent intent = new Intent(getApplication(), PreferencesActivity.class);
                startActivityForResult(intent, 123);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Este método se encarga de gestionar el idioma de cualquier ventana que herede de la clase
     * MainActivity
     */
    protected void manageCurrentAppLanguage() {
        this.languageManager.manageCurrentAppLanguage();
        this.currentActivityLanguage = this.languageManager.getCurrentLanguageCodeFromPreferences();
    }


    /**
     * Resetea el contenido de los inputs de la aplicación que se guardó en preferencias.
     */
    protected void resetUserInputFields() {
        this.preferencesManager.clearAllInputtedTextInFields();
    }


    /**
     * Obtiene el usuario especificado por el username
     */
    public User getUser(String pUsername) {
        AllUsersGetter usersGetter = new AllUsersGetter();

        return usersGetter.getUser(pUsername);
    }


    /**
     * Añade un nuevo usuario a la base de datos.
     */
    public void addUser(String pUsername, String pPassword) {
        Data datos = new Data.Builder().putString("username",pUsername).putString("password", pPassword)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(UserInsertWorker.class).setInputData(datos).build();
        WorkManager.getInstance(MainActivity.this).enqueue(otwr);
    }


    /**
     * Comprueba si las credenciales introducidas son correctas.
     */
    public boolean checkIfUserCredentialsAreCorrect(String pUsername, String pPassword) {
        boolean areCredentialsCorrect = false;
        User user = this.getUser(pUsername);

        if (user != null) {
            areCredentialsCorrect = pUsername.equals(user.getUsername())
                    && pPassword.equals(user.getPassword());
        }

        return areCredentialsCorrect;
    }


    /**
     * Indica si un usuario existe en la base de datos.
     */
    public boolean checkIfUserExists(String pUsername) {
        User user = this.getUser(pUsername);
        boolean doesUserExist = (user != null);

        return doesUserExist;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
>>>>>>> 49d002836054459d98450b319ac490eff4f71738
    }
}