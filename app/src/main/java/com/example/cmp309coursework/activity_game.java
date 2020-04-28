package com.example.cmp309coursework;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.graphics.Canvas;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;

import java.util.Locale;


public class activity_game extends AppCompatActivity implements SensorEventListener
{
    private Boolean timerDone;
    private long timeLeft = 1200000;
    final String TAG = "GAME";
    String prefix = "";
    String country = "";
    private long lastUpdate;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView timerTxt;

    AnimatedView mAnimatedView = null;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAnimatedView = new AnimatedView(this);
        setContentView(mAnimatedView);
        timerTxt = findViewById(R.id.timerText);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        gameTimer();
    }
    public void gameTimer()
    {
        CountDownTimer countDown = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish()
            {
                timerDone = true;
                Log.d(TAG, "Timers done");
                endScreen();
            }
        }.start();

        timerDone = false;
    }

    private void updateTimer()
    {
        int min = (int) (timeLeft / 1000) / 60;
        int sec = (int) (timeLeft/ 1000) % 60;

        String formattedTimer = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
        if(!timerDone)
        {
            timerTxt.setText(formattedTimer);
        }

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
                Log.d(TAG,"End Screen");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAnimatedView.onSensorEvent(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class AnimatedView extends View {

        private static final int CIRCLE_RADIUS = 35; //pixels

        private Paint grey;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            grey = new Paint();
            grey.setColor(Color.GRAY);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

        public void onSensorEvent(SensorEvent event) {
            x = x - (int) event.values[0];
            y = y + (int) event.values[1];
            //Make sure we do not draw outside the bounds of the view.
            //So the max values we can draw to are the bounds + the size of the circle
            if (x <= 0 + CIRCLE_RADIUS) {
                x = 0 + CIRCLE_RADIUS;
            }
            if (x >= viewWidth - CIRCLE_RADIUS) {
                x = viewWidth - CIRCLE_RADIUS;
            }
            if (y <= 0 + CIRCLE_RADIUS) {
                y = 0 + CIRCLE_RADIUS;
            }
            if (y >= viewHeight - CIRCLE_RADIUS) {
                y = viewHeight - CIRCLE_RADIUS;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(x, y, CIRCLE_RADIUS, grey);
            //We need to call invalidate each time, so that the view continuously draws
            invalidate();

        }

    }

}