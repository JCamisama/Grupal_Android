package com.example.grupal_android.preferencias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.example.grupal_android.R;

/**
 * Clase que se utiliza para emplear las preferencias de usuario.
 */
public class CustomPreferences extends PreferenceFragmentCompat
                            implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String languagePreferenceName = "language";
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.my_preferences);
    }


    /**
     * Responde a un cambio en las preferencias del usuario.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case languagePreferenceName:
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                break;
            default:
                System.out.println("Current user value control coming soon!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
