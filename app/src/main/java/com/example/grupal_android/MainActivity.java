package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.grupal_android.PreferencesActivity;

import com.example.grupal_android.managers.CustomPreferencesManager;
import com.example.grupal_android.managers.LanguageManager;
import com.example.grupal_android.managers.SessionManager;

public class MainActivity extends AppCompatActivity {

    protected String currentActivityLanguage = "";
    protected LanguageManager languageManager = null;
    protected CustomPreferencesManager preferencesManager = null;
    protected SessionManager sessionManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.languageManager = LanguageManager.getInstance(MainActivity.this);
        this.preferencesManager = CustomPreferencesManager.getInstance(MainActivity.this);
        this.sessionManager = SessionManager.getInstance(MainActivity.this);

        this.manageCurrentAppLanguage();
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

}