package com.example.cmp309coursework;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_settings extends AppCompatActivity implements View.OnClickListener
{
    final String TAG = "SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button changePerms = findViewById(R.id.permsBttn);
        final Button clearScores = findViewById(R.id.clearScoresBttn);
        changePerms.setOnClickListener(this);
        clearScores.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        // Lets user change their permissions or delete scores from the database
        switch (v.getId())
        {
            case R.id.permsBttn:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, R.string.app_name);
                Toast.makeText(getApplicationContext(), "Permissions Changed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearScoresBttn:
                database_helper databaseObj = new database_helper(this);
                databaseObj.clearDB();
                Toast.makeText(getApplicationContext(),"Scores Cleared", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Cleared scores button pressed");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}