package com.example.grupal_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button btnMapa;

    /**
     *  Crear la actividad y vincular con la actividad correspondiente
     */
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

    /**
     *  Iniciar la actividad MapActivity mediante el botón mapa
     */
    public void onMapa(View v){

        System.out.println("Tu imaginate que ahora se abre un mapa");
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("tienda",franquicia.getName());
        startActivity(i);

    }

    /**
     * Conseguir extras mandadas por la actividad anterior
     */

    private void getExtras() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        int pos = (int) b.get("franquicia");

        FranchiseManager man = FranchiseManager.getInstance(this.getApplicationContext());
        franquicia = man.getFranchise(pos);

    }

    /**
     *  Conseguir todos los elementos del layout
     */
    private void getElements() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            tx = findViewById(R.id.franquicia2);
            tipo = findViewById(R.id.tipoFranq2);
            desc = findViewById(R.id.descripcion2);
            logo = findViewById(R.id.logo2);
            btnMapa = findViewById(R.id.mapa2);

        } else {

            tx = findViewById(R.id.franquicia);
            tipo = findViewById(R.id.tipoFranq);
            desc = findViewById(R.id.descripcion);
            logo = findViewById(R.id.logo);
            btnMapa = findViewById(R.id.mapa);

        }

    }

    /**
     *  Settear todos los elementos del layout
     */
    private void setElements() {

        tx.setText(franquicia.getName());
        logo.setImageBitmap(franquicia.getLogo());


        String type = this.getString(R.string.tipo);
        String texto;

        if (LanguageManager.getInstance(this).getCurrentLanguageCodeFromPreferences().equals("en")){

            texto = type+": " + franquicia.getType_EN();
            tipo.setText(texto);

        } else if (LanguageManager.getInstance(this).getCurrentLanguageCodeFromPreferences().equals("es")){

            texto = type+": " + franquicia.getType_ES();
            tipo.setText(texto);

        }

    }

}
