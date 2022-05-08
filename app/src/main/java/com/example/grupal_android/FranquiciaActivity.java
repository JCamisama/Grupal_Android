package com.example.grupal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FranquiciaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franquicia);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String franquicia = (String) b.get("franquicia");

        TextView tx = findViewById(R.id.textView2);
        tx.setText("Soy la Franquicia " + franquicia);

    }
}