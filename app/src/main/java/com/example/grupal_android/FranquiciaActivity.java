package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.models.Franchise;

public class FranquiciaActivity extends AppCompatActivity {

    Franchise franquicia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franquicia);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        int pos = (int) b.get("franquicia");

        FranchiseManager man = FranchiseManager.getInstance(this.getApplicationContext());
        franquicia = man.getFranchise(pos);

        TextView tx;
        TextView tipo;
        TextView desc;
        ImageView logo;

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

        tx.setText(franquicia.getName());
        tipo.setText("Tipo: " + franquicia.getType_ES());
        desc.setText(franquicia.getDescription_ES());
        logo.setImageBitmap(franquicia.getLogo());

        //logo, tipo, nombre, descripcion
        // botones: mapa(pasar nombre tienda), web
    }

    public void onWeb(View v){

        // Abrir en el navegador el link
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(franquicia.getUrl()));
        startActivity(i);

    }

    public void onMapa(View v){

        System.out.println("Tu imaginate que ahora se abre un mapa");
//        Intent i = new Intent(this, FranquiciaListActivity.class);
//        i.putExtra("tienda",franquicia.getName());
//        startActivity(i);

    }

}
