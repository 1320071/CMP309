package com.example.cmp309coursework;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_instructions extends AppCompatActivity implements View.OnClickListener
{
    public String TAG = "INSTRUCTIONS";
    public String country;
    public String prefix;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Button play = findViewById(R.id.instructionsBttn);
        final TextView prefixText = findViewById(R.id.prefixTxt);
        final EditText shipName = findViewById(R.id.editText);
        final Button back = findViewById(R.id.backBttnI);

        play.setOnClickListener(this);
        back.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            country = intent.getStringExtra("country");
            prefix = intent.getStringExtra("prefix");
            Log.d(TAG, "Got intent:" + prefix + country);
        }
        else {
            Toast error = Toast.makeText(this, "An error has occured", Toast.LENGTH_SHORT);
            error.show();
            Log.wtf(TAG, "Something else called me" + getParent().getClass().getSimpleName());
            finish();
        }

        if(prefix == null)
        {
            prefix = "SS";
        }

        prefixText.setText(prefix);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final EditText shipName = findViewById(R.id.editText);
        String ship = shipName.getText().toString();

        switch (v.getId()) {
            case R.id.instructionsBttn:
                if (ship == null) {
                    Toast.makeText(getApplicationContext(), "Please enter ship name", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, activity_game.class);
                    intent.putExtra("country", country);
                    intent.putExtra("prefix", prefix);
                    intent.putExtra("shipName",ship);
                    startActivity(intent);

                    Log.d(TAG, "Sent intent to game");
                }
                break;
            case R.id.backBttnI:
                intent = new Intent(this, main_activity.class);
                startActivity(intent);

                Log.d(TAG, "Sent intent to main menu");

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}
