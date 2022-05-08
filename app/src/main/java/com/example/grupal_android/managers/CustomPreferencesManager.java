package com.example.grupal_android.managers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.grupal_android.utils.GlobalVariablesUtil;

/**
 * Gestor que maneja el acceso programatico a las preferencias de la aplicación.
 */
public class CustomPreferencesManager {

    private static CustomPreferencesManager instance = null;
    private Context myContext = null;
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor= null;


    /*** Singleton Pattern ***/
    private CustomPreferencesManager(Context pContext) {
        this.myContext = pContext;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.myContext);
        this.editor = this.preferences.edit();
    }

    public static synchronized CustomPreferencesManager getInstance(Context pContext) {
        if (CustomPreferencesManager.instance == null) {
            CustomPreferencesManager.instance = new CustomPreferencesManager(pContext);
        }

        return CustomPreferencesManager.instance;
    }
    /*************************/


    /**
     * Permite insertar valores de tipo String en las preferencias.
     */
    public void setStringInPreferences(String pKey, String pValue) {
        this.editor.putString(pKey, pValue);
        this.editor.apply();
    }


    /**
     * Permite obtener valores del tipo String de las preferencias.
     */
    public String getString(String preferenceKey) {
        String obtainedValue = this.preferences.getString(preferenceKey, "");

        return obtainedValue;
    }


    /**
     * Borra todos los valores asociados a inputs de las preferencias.
     */
    public void clearAllInputtedTextInFields() {
        this.editor.putString(GlobalVariablesUtil.INPUTTED_USER, "");
        this.editor.putString(GlobalVariablesUtil.INPUTTED_PASSWORD, "");
        this.editor.putString(GlobalVariablesUtil.REGISTER_USER, "");
        this.editor.putString(GlobalVariablesUtil.REGISTER_PASSWORD, "");
        this.editor.putString(GlobalVariablesUtil.REGISTER_REPEAT_PASSWORD, "");
        this.editor.apply();
    }

}
