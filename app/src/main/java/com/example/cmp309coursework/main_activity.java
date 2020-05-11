package com.example.cmp309coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class main_activity extends AppCompatActivity implements View.OnClickListener
{
    // This is both the 'main menu' and setup for the app
    final String TAG = "MAIN";

    public double latitude;
    public double longitude;
    public String country;
    public String countryPrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("Main Activity", "Start");

        new setUp();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Set button listeners
        final Button play = findViewById(R.id.playBttn);
        final Button settings = findViewById(R.id.settingsBttn);
        final Button scores = findViewById(R.id.scoreBttn);
        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        scores.setOnClickListener(this);
        Log.d(TAG, "Set on click listeners");

        // Checks if user has allowed location data and if not requests it
        if ((ActivityCompat.checkSelfPermission(main_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "Showing dialogue for permissions");
            final AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme).create();
            dialog.setTitle("Permissions required");
            dialog.setMessage("Hi! This game uses location data to figure out where to place you on the map.\n\nIt isn't essential and we aren't using it to track you so don't feel pressured to accept");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Log.d(TAG, "Asking for permissions");
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS}, R.string.app_name);
                }
            });

            dialog.show();
        }

    }

    @Override
    public void onClick(View v)
    {
        // Starts different pages depending on where the user wants to go
        Intent intent;
        switch (v.getId())
        {
            case R.id.playBttn:
                intent = new Intent (main_activity.this, activity_instructions.class);
                intent.putExtra("country", country);
                intent.putExtra("prefix", countryPrefix);
                startActivity(intent);
                Log.d(TAG, "Sent intent to game instructions");
                break;
            case R.id.settingsBttn:
                startActivity(new Intent(main_activity.this, activity_settings.class));
                Log.d(TAG, "Sent intent to settings");
                break;
            case R.id.scoreBttn:
                startActivity(new Intent(main_activity.this, activity_scores.class));
                Log.d(TAG, "Sent intent to scores");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    final LocationListener listener = new LocationListener()
    {
        @Override
        public void onLocationChanged(final Location location)
        {
            // Gets the users location in order to determine what country their ship is from and therefore its prefix
            try
            {
                Log.d(TAG, "Set up location listener");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(TAG, "Got lat and long");

                if (((latitude >= 25.0) && (latitude <= 48.0)) && ((longitude <= -78.0) && (longitude >= -125.0))) {
                    country = "USA";
                    countryPrefix = "USS";
                } else if (((latitude <= 58.0) && (latitude >= 52.0)) && ((longitude <= -5.0) && (longitude >= -3.0))) {
                    country = "UK";
                    countryPrefix = "HMS";
                } else if (((latitude <= -46.0) && (latitude >= -37.0)) && ((longitude >= 137.0) && (longitude <= 169.0))) {
                    country = "New Zealand";
                    countryPrefix = "HMNZS";
                }
                Log.d(TAG, "Got country prefix");
            }
            catch(Error e)
            {
                Log.e(TAG, "error getting location: " + e);
            }
        };

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

       }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            // If location is turned off then just use default prefix
            Log.d(TAG,"Set default prefix");
            country = "United Kingdom";
            countryPrefix = "HMS";
        }
    };

    private class setUp implements Runnable
    {
        // Runs db creation in background on seperate thread
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            database_helper database = new database_helper(main_activity.this);
            database.addExampleData();
            Log.d(TAG, "Created db in main:"+ database.createCreateString());
        }
    }

}