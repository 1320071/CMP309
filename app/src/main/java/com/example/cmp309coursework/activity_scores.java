package com.example.cmp309coursework;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_scores extends AppCompatActivity implements View.OnClickListener
{
    final String TAG = "SCORES";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Log.d(TAG, "I drew the page");

        TextView highScores = (TextView) findViewById(R.id.scrollTxt);
        Log.d(TAG, "I found ScrollTxt");

        database_helper databaseObj = new database_helper(this);
        Log.d(TAG, "Getting Scores");

        String scores = databaseObj.getScores();
        Log.d(TAG, "Updating scrollTXT");

        highScores.setText(scores);
    }

    @Override
    public void onClick(View v)
    {

    }
}