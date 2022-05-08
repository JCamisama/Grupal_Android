package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FranquiciaActivity extends AppCompatActivity {

    Franquicia franquicia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franquicia);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        int pos = (int) b.get("franquicia");

        FranchiseManager man = FranchiseManager.getInstance(this.getApplicationContext());
        franquicia = man.getFranquicia(pos);

        TextView tx = findViewById(R.id.textView2);
        TextView tipo = findViewById(R.id.tipoFranq);
        TextView desc = findViewById(R.id.descripcion);
        ImageView logo = findViewById(R.id.logo);
        tx.setText(franquicia.getNombre());
        tipo.setText("Tipo: " + franquicia.getTipo());
        desc.setText(franquicia.getDescripcion());
        logo.setImageResource(franquicia.getLogo());
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
//        startActivityForResult(i, 66);

    }

}
