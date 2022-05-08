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
    FranchiseManager franquiciaList;
    int logos[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] plogos, Context pcontext) {
        this.context = pcontext;
        this.franquiciaList = FranchiseManager.getInstance(this.context);
        this.logos = plogos;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return franquiciaList.getSize();
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
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        Franquicia fran = this.franquiciaList.getFranquicia(i);
        franquicia.setText(fran.getNombre());
        tipo.setText("Tipo: " + fran.getTipo());

        setImages(icon, fran);

        return view;
    }

    // Cambiar la imagen del pokemon dependiendo del nombre de este
    private void setImages(ImageView icon, Franquicia fran) {

        Log.i("Recorrido","Paso por setImages CustomAdapter");
        switch (fran.getNombre()){

            case "Placeholder1": {

                fran.setLogo(logos[0]);
                icon.setImageResource(logos[0]);
                break;

            }

            case "Placeholder2": {

                fran.setLogo(logos[1]);
                icon.setImageResource(logos[1]);
                break;

            }

            case "Placeholder3": {

                fran.setLogo(logos[2]);
                icon.setImageResource(logos[2]);
                break;

            }

        }

    }

}