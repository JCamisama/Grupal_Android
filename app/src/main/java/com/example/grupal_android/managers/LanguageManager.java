package com.example.grupal_android.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.preference.PreferenceManager;

import com.example.grupal_android.preferencias.CustomPreferences;

import java.util.Locale;

/**
 * Gestor dedicado al manejo de idiomas en la aplicación
 */
public class LanguageManager {

    private static LanguageManager instance = null;
    private Context myContext = null;
    public static final String defaultLanguage = "en";

    /*** Singleton Pattern ***/
    private LanguageManager(Context pContext) {
        this.myContext = pContext;
        this.changeCurrentLocale(defaultLanguage);
    }

    public static synchronized LanguageManager getInstance(Context pContext) {
        if (LanguageManager.instance == null) {
            LanguageManager.instance = new LanguageManager(pContext);
        }

        return LanguageManager.instance;
    }
    /*************************/


    /**
     * Determina si el idioma ha cambiado. De haberlo hecho, cambia el Locale de la
     * aplicación
     */
    public boolean manageCurrentAppLanguage() {
        boolean hasLanguageChanged = false;

        Locale locale = this.myContext.getResources().getConfiguration().locale;

        String currentSetLanguage = locale.getLanguage();
        String currentLanguagePreferenceCode = this.getCurrentLanguageCodeFromPreferences();

        hasLanguageChanged = !currentSetLanguage.equals(currentLanguagePreferenceCode);
        if (hasLanguageChanged) {
            this.changeCurrentLocale(currentLanguagePreferenceCode);
        }

        return hasLanguageChanged;
    }


    /**
     * Obtiene el idioma actual de la aplicación.
     */
    public String getCurrentLanguageCodeFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.myContext);
        String languagePreferenceName = CustomPreferences.languagePreferenceName;
        String currentLanguagePreference = prefs.getString(languagePreferenceName, defaultLanguage);

        if (currentLanguagePreference == null || currentLanguagePreference =="") {
            currentLanguagePreference = defaultLanguage;
            CustomPreferencesManager.getInstance(this.myContext).setStringInPreferences(
                    languagePreferenceName,
                    "english"
            );
        }

        return currentLanguagePreference;
    }


    /**
     * Método que modifica el idioma de la aplicación.
     */
    private void changeCurrentLocale(String pCurrentLanguagePreference) {
        Locale newLocale = new Locale(pCurrentLanguagePreference);
        Locale.setDefault(newLocale);
        Configuration configuration = this.myContext.getResources().getConfiguration();
        configuration.setLocale(newLocale);
        configuration.setLayoutDirection(newLocale);

        Resources resources = this.myContext.getResources();
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
