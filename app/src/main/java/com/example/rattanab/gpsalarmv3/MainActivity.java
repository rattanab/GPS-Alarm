package com.example.rattanab.gpsalarmv3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.sql.Time;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    String text;
    Context geodata;
    private TextView geolocation;
    private FusedLocationProviderClient mFusedLocationClient;
    double manlat;
    double manlong;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button localpicker_btn = (Button) findViewById(R.id.localpicker_btn);
        geolocation = (TextView) findViewById(R.id.textView);
        Button openmap_btn = (Button) findViewById(R.id.openmap_btn);
        Button gotomap_btn = (Button) findViewById(R.id.gotomap_btn);
        Button getlocal_btn = (Button) findViewById(R.id.getcurrentloca_btn);
        Button mangeo_btn = (Button) findViewById(R.id.manGeoSubmit_btn);
        Button ringtestalarm_btn = (Button) findViewById(R.id.testalarm_btn);
        Button stopalarm_btn = (Button) findViewById(R.id.stopalarm_btn);
        final TextView displaylocal = (TextView) findViewById(R.id.displaylocal);
        final EditText inputlat = (EditText) findViewById(R.id.editLat);
        final EditText inputlong = (EditText) findViewById(R.id.editLong);
        final TextView displaydistance = (TextView) findViewById(R.id.calculatedDistance);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        localpicker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalpicker_btn(v);
            }
        });
        openmap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapApp(v);
            }
        });
        gotomap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotomapact(v);
            }
        });
        getlocal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaylocal.setText(getcurrentlocation());
            }
        });
        mangeo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templat = inputlat.getText().toString();
                manlat = Double.valueOf(templat);
                String templong = inputlong.getText().toString();
                manlong = Double.valueOf(templong);
                displaydistance.setText(calculate(manlat,manlong));
            }
        });
        ringtestalarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm();
            }
        });
        stopalarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopalarm();
            }
        });
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        text = location.toString();
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });
//        geolocation.setText(text);
    }

    public void setLocalpicker_btn(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getAddress());
                geolocation.setText(toastMsg);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openMapApp(View v) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=13.8,100.013988");
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    public void gotomapact(View v) {
        Intent gotomap = new Intent(this, MapsActivity.class);
        startActivity(gotomap);
    }

    public String getcurrentlocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String local = String.valueOf(latitude) + "," + String.valueOf(longitude);
        return local;
    }

    public String calculate(double manlat, double manlong) {
        // current location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        // calculation
        double resultlat = manlat - latitude;
        double resultlong = manlong - longitude;
        if (resultlat<0){
            resultlat=resultlat*-1;
        }
        if (resultlong<0){
            resultlong=resultlong*-1;
        }
        if (resultlat < 0.01 && resultlong < 0.01){
            alarm();
        }
        return String.valueOf(resultlat)+", "+String.valueOf(resultlong);
    }

    public void alarm(){
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
    }

    public void stopalarm(){
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "STOP ALARM!", Toast.LENGTH_LONG).show();
    }
}
