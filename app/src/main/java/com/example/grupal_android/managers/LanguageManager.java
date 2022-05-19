package com.example.grupal_android.managers;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.grupal_android.preferencias.CustomPreferences;

import java.util.Locale;

/**
 * Gestor dedicado al manejo de idiomas en la aplicación
 */
public class LanguageManager {

    private static LanguageManager instance = null;
    private AppCompatActivity myActivity = null;
    public static final String defaultLanguage = "en";

    /*** Singleton Pattern ***/
    private LanguageManager(AppCompatActivity pMyActivity) {
        this.myActivity = pMyActivity;
        this.changeCurrentLocale( pMyActivity, defaultLanguage);
    }

    public static synchronized LanguageManager getInstance(AppCompatActivity pMyActivity) {
        if (LanguageManager.instance == null) {
            LanguageManager.instance = new LanguageManager(pMyActivity);
        }

        return LanguageManager.instance;
    }
    /*************************/



    /**
     * Obtiene el idioma actual de la aplicación.
     */
    public String getCurrentLanguageCodeFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.myActivity);
        String languagePreferenceName = CustomPreferences.languagePreferenceName;
        String currentLanguagePreference = prefs.getString(languagePreferenceName, defaultLanguage);

        if (currentLanguagePreference == null || currentLanguagePreference =="") {
            currentLanguagePreference = defaultLanguage;
            CustomPreferencesManager.getInstance(this.myActivity).setStringInPreferences(
                    languagePreferenceName,
                    "english"
            );
        }

        return currentLanguagePreference;
    }


    /**
     * Método que modifica el idioma de la aplicación.
     */
    private void changeCurrentLocale(AppCompatActivity pMyActivity, String pCurrentLanguagePreference) {
        Locale newLocale = new Locale(pCurrentLanguagePreference);
        Locale.setDefault(newLocale);
        Configuration configuration = pMyActivity.getResources().getConfiguration();
        configuration.setLocale(newLocale);
        configuration.setLayoutDirection(newLocale);

        Resources resources = pMyActivity.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    /**
     * Este método determina si el idioma ha cambiado con respecto
     * al registrado en una actividad en concreto.
     */
    public boolean hasLanguageChanged(String pCurrentActivityLanguageCode) {
        String currentAppLanguageCode = this.getCurrentLanguageCodeFromPreferences();

        return pCurrentActivityLanguageCode != currentAppLanguageCode;
    }


    /*************************/
}
