package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FranquiciaListActivity extends AppCompatActivity {

    ListView simpleList;
    private ArrayList<String> franquiciaList = new ArrayList<String>();
    int logos[] = {R.drawable.loli};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.franquicia_list_main);

        franquiciaList.add("Placeholder1");
        franquiciaList.add("Placeholder2");
        franquiciaList.add("Placeholder3");

        simpleList = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), franquiciaList, logos, this.getApplicationContext());
        simpleList.setAdapter(customAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    default: {

                        Intent i = new Intent(FranquiciaListActivity.this, FranquiciaActivity.class);
                        i.putExtra("franquicia", franquiciaList.get(position));
                        startActivity(i);

                    }
                }

            }
        });

    }

}