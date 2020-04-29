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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.graphics.Canvas;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;

import java.util.Locale;
import java.util.Random;


public class activity_game extends AppCompatActivity implements SensorEventListener
{
    private Boolean timerDone;
    private long timeLeft = 3000; // 120000 = 2 mins (actual gameplay) | 10000 = 10 seconds (for showing off) | 3000 = 3 seconds (for debugging)
    final String TAG = "GAME";
    String prefix;
    String country = "";
    private long lastUpdate;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView timerTxt;
    private int finalScore;
    private int itemX = 0;
    private int itemY = 0;
    private String nickname = "";

    //private View layout = findViewById(R.id.gameScreen);
    AnimatedView mAnimatedView = null;

    public activity_game() {
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        ConstraintLayout layout = findViewById(R.id.gameScreen);
        layout.setBackgroundColor(Color.parseColor("#3498DB"));
        mAnimatedView = new AnimatedView(this);
        layout.addView(mAnimatedView);


        timerTxt = findViewById(R.id.timerText);

        Log.d(TAG, "Created screen");

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            country = intent.getStringExtra("country");
            prefix = intent.getStringExtra("prefix");
            nickname = intent.getStringExtra("shipName");
            Log.d(TAG, "Got intent:" + prefix + country + nickname);
        }
        else {
            Toast error = Toast.makeText(this, "An error has occured", Toast.LENGTH_SHORT);
            error.show();
            Log.wtf(TAG, "Something else called me" + getParent().getClass().getSimpleName());
            finish();
        }

        gameTimer();
    }

    // >>>>>>>>>> Timer <<<<<<<<<< //
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

        timerTxt.setText(formattedTimer);

    }

    // >>>>>>>>>> End screen <<<<<<<<< //
    public void endScreen()
    {
        Log.d(TAG, "Game over");
        database_helper databaseObj = new database_helper(this);

        databaseObj.addHighScores(prefix, nickname, finalScore);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme).create();
        Log.d(TAG, "End Screen");
        dialog.setTitle("GAME OVER");
        dialog.setMessage("Good job! Your final score was:" + finalScore);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back to menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(activity_game.this, main_activity.class));
                finish();
            }
        });
        dialog.show();
    }

    // >>>>>>>>>> Items <<<<<<<<<< //

    public void moveItems(Canvas canvas, int x)
    {
        itemY =  300;
        int radius = 10;
        Paint yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        Random random = new Random();

        canvas.drawCircle(itemY, x, radius, yellow);
    }


    // >>>>>>>>>> Move ship <<<<<<<<<< //
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

        private static final int RADIUS = 40; //pixels

        private Paint grey;
        private int boatX;
        private int boatY;
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
            viewWidth = h;
            viewHeight = w;
        }

        // Ensures ship doesn't go off screen
        public void onSensorEvent(SensorEvent event) {
            boatX = boatX - (int) event.values[0];
            boatY = boatY + (int) event.values[1];

            if (boatX <= RADIUS) {
                boatX = RADIUS;
            }
            if (boatX >= viewWidth - RADIUS) {
                boatX = viewWidth - RADIUS;
            }
            if (boatY <= RADIUS) {
                boatY = RADIUS;
            }
            if (boatY >= viewHeight - RADIUS) {
                boatY = viewHeight - RADIUS;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // random.nextInt(300);
            canvas.drawCircle(boatY, boatX, RADIUS, grey);//for rectangle: left, top, right, bottom, colour

            moveItems(canvas, 100);

            // need to call invalidate each time, so that the view continuously draws
            invalidate();

        }
    }
}