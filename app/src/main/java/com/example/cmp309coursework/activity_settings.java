package com.example.cmp309coursework;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_settings extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button back = findViewById(R.id.backBttn);
        final Button changePerms = findViewById(R.id.permsBttn);
        final Button clearScores = findViewById(R.id.clearScoresBttn);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backBttn:
                startActivity(new Intent(activity_settings.this, main_activity.class));
                break;
            case R.id.permsBttn:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, R.string.app_name);
                break;
            case R.id.clearScoresBttn:
                //TODO: implement
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}