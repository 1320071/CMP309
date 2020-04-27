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
import android.content.pm.ActivityInfo;


public class activity_game extends AppCompatActivity implements SensorEventListener
{
    final String TAG = "GAME";

    String prefix = "";
    String country = "";
    private long lastUpdate;
    private SensorManager sensorManager;
    private Sensor sensor;

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
        //Set our content to a view, not like the traditional setting to a layout
        setContentView(mAnimatedView);

        //sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //ImageView ship = findViewById(R.id.boat);
        //lastUpdate = System.currentTimeMillis();

        //animatedView = new AnimatedView(this);



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

        //gameTimer(sensorManager);

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

    public void moveShip()
    {


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

        private static final int CIRCLE_RADIUS = 25; //pixels

        private Paint mPaint;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.MAGENTA);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

        public void onSensorEvent (SensorEvent event) {
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
            canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);
            //We need to call invalidate each time, so that the view continuously draws
            invalidate();

    }



}