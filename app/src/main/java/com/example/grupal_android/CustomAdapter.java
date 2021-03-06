package com.example.grupal_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupal_android.managers.CustomPreferencesManager;
import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.managers.LanguageManager;
import com.example.grupal_android.models.Franchise;

import java.util.ArrayList;

/**
 *  Adaptador de listview
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Franchise> franquiciaList;
    LayoutInflater inflter;
    TextView franquicia;
    TextView tipo;
    ImageView icon;
    Franchise fran;

    public CustomAdapter(Context applicationContext, Context pcontext) {
        this.context = pcontext;
        this.franquiciaList = FranchiseManager.getInstance(this.context).getFranchises();
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

        getElements(view);

        setElements(i);

        setImages(icon, fran);

        return view;
    }

    // Cambiar la imagen del pokemon dependiendo del nombre de este
    private void setImages(ImageView icon, Franchise fran) {

        Log.i("Recorrido","Paso por setImages CustomAdapter");
        icon.setImageBitmap(fran.getLogo());

    }

    private void getElements(View view) {

        franquicia = (TextView) view.findViewById(R.id.textView);
        tipo = (TextView) view.findViewById(R.id.tipo);
        icon = (ImageView) view.findViewById(R.id.icon);

    }

    private void setElements(int i) {

        fran = this.franquiciaList.get(i);
        franquicia.setText(fran.getName());

        String type = this.context.getString(R.string.tipo);
        String texto;

        if (LanguageManager.getInstance(null).getCurrentLanguageCodeFromPreferences().equals("en")){

            texto = type+": " + fran.getType_EN();
            tipo.setText(texto);

        } else if (LanguageManager.getInstance(null).getCurrentLanguageCodeFromPreferences().equals("es")){

            texto = type+": " + fran.getType_ES();
            tipo.setText(texto);

        }

    }

}