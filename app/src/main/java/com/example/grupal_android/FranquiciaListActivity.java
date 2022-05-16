package com.example.grupal_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class FranquiciaListActivity extends MainActivity {

    ListView simpleList;
    /**
     * Actividad de la lista de las franquicias
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.franquicia_list_main);

        super.initializeMenuBar();

        simpleList = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), this.getApplicationContext());
        simpleList.setAdapter(customAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    default: {

                        Intent i = new Intent(FranquiciaListActivity.this, FranquiciaActivity.class);
                        i.putExtra("franquicia", position);
                        startActivity(i);

                    }
                }

            }
        });

    }

}