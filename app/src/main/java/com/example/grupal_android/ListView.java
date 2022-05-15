package com.example.grupal_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class ListView extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franquicia_list);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        Log.i("Recorrido","Paso por onOptionsItemSelected ListaPokemon para acceder a la lista de favoritos");
//        int id=item.getItemId();
//        switch (id){
//            default:{
//
//                Intent i = new Intent(this, FranquiciaActivity.class);
//                startActivity(i);
//
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

}