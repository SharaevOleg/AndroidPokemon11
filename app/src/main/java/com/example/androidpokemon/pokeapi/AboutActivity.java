package com.example.androidpokemon.pokeapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.androidpokemon.R;

import java.util.ArrayList;

public class AboutActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        ArrayList<String> favouritesPokemon = intent.getStringArrayListExtra("keyOne");
        if (favouritesPokemon != null) {
            for (String klist : favouritesPokemon) {
                textView.setText(klist + "\n");
            }
        } else {
            textView.setText("НЕТ ИЗБРАННЫХ ПОКЕМОНОВ");
        }
    }//                textView.setText(favouritesPokemon.size());


}