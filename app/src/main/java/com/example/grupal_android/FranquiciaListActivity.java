package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), franquiciaList, logos);
        simpleList.setAdapter(customAdapter);

    }
}