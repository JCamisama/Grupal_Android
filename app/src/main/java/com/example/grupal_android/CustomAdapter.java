package com.example.grupal_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> franquiciaList;
    int logos[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> pfranquiciaList, int[] plogos, Context pcontext) {
        this.context = pcontext;
        this.franquiciaList = pfranquiciaList;
        this.logos = plogos;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return franquiciaList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Log.i("Recorrido","Paso por getView CustomAdapter");
        view = inflter.inflate(R.layout.activity_franquicia_list, null);
        TextView franquicia = (TextView) view.findViewById(R.id.textView);
        TextView tipo = (TextView) view.findViewById(R.id.tipo);
        tipo.setText("tipo: reposteria");
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        franquicia.setText(franquiciaList.get(i));

        setImages(icon, franquicia.getText().toString());

        return view;
    }

    // Cambiar la imagen del pokemon dependiendo del nombre de este
    private void setImages(ImageView icon, String nombre) {

        Log.i("Recorrido","Paso por setImages CustomAdapter");
        switch (nombre){

            case "Placeholder1": {

                icon.setImageResource(logos[0]);
                break;

            }

            case "Placeholder2": {

                icon.setImageResource(logos[1]);
                break;

            }

            case "Placeholder3": {

                icon.setImageResource(logos[2]);
                break;

            }

        }

    }

}