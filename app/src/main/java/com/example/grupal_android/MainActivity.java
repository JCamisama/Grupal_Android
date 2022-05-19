package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.grupal_android.PreferencesActivity;

import com.example.grupal_android.managers.CustomPreferencesManager;
import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.managers.LanguageManager;
import com.example.grupal_android.managers.MusicManager;
import com.example.grupal_android.managers.SessionManager;
import com.example.grupal_android.models.User;
import com.example.grupal_android.receiver.MyReceiver;
import com.example.grupal_android.services.MyService;
import com.example.grupal_android.workers.AllUsersGetter;
import com.example.grupal_android.workers.UserInsertWorker;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected String currentActivityLanguage = "";
    protected LanguageManager languageManager = null;
    protected CustomPreferencesManager preferencesManager = null;
    protected SessionManager sessionManager = null;
    protected FranchiseManager frachiseManager = null;
    protected MusicManager musicManager = null;
    private Intent intent;
    private MyService elservicio;
    private ServiceConnection laconexion= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            elservicio= ((MyService.miBinder)service).obtenServicio();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) { elservicio=null; }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);

        intent = new Intent(this, MyService.class);


        this.languageManager = LanguageManager.getInstance(this);
        this.preferencesManager = CustomPreferencesManager.getInstance(MainActivity.this);
        this.sessionManager = SessionManager.getInstance(MainActivity.this);
        this.frachiseManager = FranchiseManager.getInstance(MainActivity.this);
        this.musicManager = MusicManager.getInstance(MainActivity.this);
        this.manageCurrentAppLanguage();
        this.musicManager.getMusic();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Comprobar si al volver a la pantalla ha cambiado el idioma.
        if (this.languageManager.hasLanguageChanged(this.currentActivityLanguage)) {
            this.changeCurrentLocale(this.languageManager.getCurrentLanguageCodeFromPreferences());
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
        //this.languageManager.manageCurrentAppLanguage(this);
        this.currentActivityLanguage = this.languageManager.getCurrentLanguageCodeFromPreferences();
        this.changeCurrentLocale(this.currentActivityLanguage );
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
    }


    /**
     * Método que modifica el idioma de la aplicación.
     */
    private void changeCurrentLocale(String pCurrentLanguagePreference) {
        Locale newLocale = new Locale(pCurrentLanguagePreference);
        Locale.setDefault(newLocale);
        Configuration configuration = this.getResources().getConfiguration();
        configuration.setLocale(newLocale);
        configuration.setLayoutDirection(newLocale);

        Resources resources = this.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}