package com.example.cmp309coursework;

import android.os.Bundle;
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

        TextView highScores = (TextView) findViewById(R.id.scrollTxt);
        database_helper databaseObj = new database_helper(this);
        String scores = databaseObj.getScores();
        highScores.setText(scores);
    }

    @Override
    public void onClick(View v)
    {

    }
}