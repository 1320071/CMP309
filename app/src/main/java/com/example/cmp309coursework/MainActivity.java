package com.example.cmp309coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button play = findViewById(R.id.playBttn);
        final Button settings = findViewById(R.id.settingsBttn);
        final Button scores = findViewById(R.id.scoreBttn);

        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        scores.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playBttn:
                //startActivity(new Intent(MainActivity.this, playGame.class));
                break;
            case R.id.settingsBttn:
                //startActivity(new Intent(MainActivity.this, Settings.class));
                break;
            case R.id.scoreBttn:
                //startActivity(new Intent(MainActivity.this, HighScores.class));
                break;
        }


    }
}
