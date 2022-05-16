package com.example.grupal_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupal_android.managers.CustomPreferencesManager;
import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.managers.LanguageManager;
import com.example.grupal_android.models.Franchise;

public class FranquiciaActivity extends MainActivity {

    private Franchise franquicia;
    private TextView tx;
    private TextView tipo;
    private TextView desc;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franquicia);

        super.initializeMenuBar();

        getExtras();

        getElements();

        setElements();

    }

    public void onWeb(View v){

        // Abrir en el navegador el link
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(franquicia.getUrl()));
        startActivity(i);

    }

    public void onMapa(View v){

        System.out.println("Tu imaginate que ahora se abre un mapa");
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("tienda",franquicia.getName());
        startActivity(i);

    }

    private void getExtras() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        int pos = (int) b.get("franquicia");

        FranchiseManager man = FranchiseManager.getInstance(this.getApplicationContext());
        franquicia = man.getFranchise(pos);

    }

    private void getElements() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            tx = findViewById(R.id.franquicia2);
            tipo = findViewById(R.id.tipoFranq2);
            desc = findViewById(R.id.descripcion2);
            logo = findViewById(R.id.logo2);

        } else {

            tx = findViewById(R.id.franquicia);
            tipo = findViewById(R.id.tipoFranq);
            desc = findViewById(R.id.descripcion);
            logo = findViewById(R.id.logo);

        }

    }

    private void setElements() {

        tx.setText(franquicia.getName());
        logo.setImageBitmap(franquicia.getLogo());

        if (LanguageManager.getInstance(this).getCurrentLanguageCodeFromPreferences().equals("en")){

            tipo.setText(getString(R.string.tipo)+": " + franquicia.getType_EN());
            desc.setText(franquicia.getDescription_EN());

        } else if (LanguageManager.getInstance(this).getCurrentLanguageCodeFromPreferences().equals("es")){

            tipo.setText(getString(R.string.tipo)+": " + franquicia.getType_ES());
            desc.setText(franquicia.getDescription_ES());

        }

    }

}
