package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Menú con idioma coming soon!");
        this.initializeMenuBar();
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

}