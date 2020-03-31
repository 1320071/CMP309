package com.example.cmp309coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class main_activity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Draw the screen
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

        // Checks if user has allowed location data and if not requests it
        if ((ActivityCompat.checkSelfPermission(main_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != 0))
        {
            final AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme).create();
            dialog.setTitle("Permissions required");
            dialog.setMessage("Hi! This game uses location data to figure out where to place you on the map\nIt isn't essential and we aren't using it to track you so don't feel\npressured to accept");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Change permissions", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, R.string.app_name);
                }
            });
            dialog.show();
        }

        final LocationListener listener = new LocationListener()
        {

            @Override
            public void onLocationChanged(Location location)
            {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
            }

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

            }

        };
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.playBttn:
                startActivity(new Intent(main_activity.this, activity_game.class));
                break;
            case R.id.settingsBttn:
                startActivity(new Intent(main_activity.this, activity_settings.class));
                break;
            case R.id.scoreBttn:
                startActivity(new Intent(main_activity.this, activity_scores.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }


}