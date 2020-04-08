package com.example.cmp309coursework;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class activity_game extends AppCompatActivity implements View.OnClickListener
{
    final String TAG = "GAME";

    Map locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            locationMap.put("country", intent.getStringExtra("country"));
            locationMap.put("prefix", intent.getStringExtra("prefix"));
        }
        else {
            Toast error = Toast.makeText(this, "An error has occured", Toast.LENGTH_SHORT);
            error.show();
            Log.wtf(TAG, "Something else called me" + getParent().getClass().getSimpleName());
            finish();
        }

    }

    @Override
    public void onClick(View v)
    {

    }

    public void moveShip()
    {
        SensorManager sensorManager;
        Sensor sensor;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void gameTimer()
    {
        final TextView timer = findViewById(R.id.timerText);

        new CountDownTimer(120000,1000) {
            int counter = 120;
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
                timer.setText("Times up!");
                AlertDialog.Builder endGame = new AlertDialog.Builder(getApplicationContext());
                endGame.setMessage(R.string.end_screen).setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(activity_game.this, main_activity.class));
                    }
                });

            }
        };
    }
}