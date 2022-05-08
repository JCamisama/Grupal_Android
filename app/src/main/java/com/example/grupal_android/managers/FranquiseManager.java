package com.example.grupal_android.managers;

import android.content.Context;

public class FranquiseManager {
    private static FranquiseManager instance = null;
    private Context myContext = null;
    public static final String defaultLanguage = "en";

    /*** Singleton Pattern ***/
    private FranquiseManager(Context pContext) {
        this.myContext = pContext;
    }

    public static synchronized FranquiseManager getInstance(Context pContext) {
        if (FranquiseManager.instance == null) {
            FranquiseManager.instance = new FranquiseManager(pContext);
        }

        return FranquiseManager.instance;
    }
}
