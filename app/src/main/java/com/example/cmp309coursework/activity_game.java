package com.example.cmp309coursework;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;


public class activity_game extends AppCompatActivity implements SensorEventListener2
{
    final String TAG = "GAME";
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private Bitmap ship;
    private SensorManager sensorManager;

    String prefix = "";
    String country = "";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BallView ballView = new BallView(this);
        setContentView(ballView);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y - 100;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Log.d(TAG, "Created screen");

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

        gameTimer(sensorManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        Log.d(TAG, "on start");
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener((SensorEventListener) this);
        super.onStop();
        Log.d(TAG, "on stop");
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            updateBall();
        }
    }
        private void updateBall() {
        float frameTime = 0.666f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        float xS = (xVel / 2) * frameTime;
        float yS = (yVel / 2) * frameTime;

        xPos -= xS;
        yPos -= yS;

        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
        } else if (yPos < 0) {
            yPos = 0;
        }
        Log.d(TAG,"Set coordinate info");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Log.d(TAG, "Creating ship");
            //Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.crappyboat);
            final int dstWidth = 100;
            final int dstHeight = 100;
            //ship = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
            Log.d(TAG, "Made ship");
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(ship, xPos, yPos, null);
            invalidate();
        }
    }


    public void moveShip(View v)
    {

    }


    public void gameTimer(final SensorManager sensorManager)
    {
        final TextView timer = findViewById(R.id.timerText);

        new CountDownTimer(120000,1000) {
            int counter = 120;

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "Create timer");
                timer.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
                timer.setText("Times up!");
                Log.d(TAG, "Timers done");
                endScreen();
            }
        };
    }

    public void endScreen()
    {
        if(prefix == null)
        {
            prefix = "SS";
        }

        AlertDialog.Builder endGame = new AlertDialog.Builder(getApplicationContext());
        endGame.setMessage(R.string.end_screen).setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(activity_game.this, main_activity.class));
            }
        });
    }
}