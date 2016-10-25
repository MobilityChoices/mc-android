package com.example.admin.mobilitychoicestest;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    GoogleApiClient myGoogleApiClient;
    Location myLastLocation;
    LocationRequest myLocationRequest;

    TextView latitude;
    TextView longitude;
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        latitude = (TextView) findViewById(R.id.textLatitude);
        longitude = (TextView) findViewById(R.id.textLongitude);
        time = (TextView) findViewById(R.id.textView);

        myGoogleApiClient.connect();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected synchronized void init(){
        myGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        myLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10*1000).setFastestInterval(1*1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //diese Methode wird aufgerufen wenn die Verbindung zur Google API erfolgreich ist
        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);
        if(myLastLocation == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);
        }else{
            handleNewLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        latitude.setText("Latutude: An error occured!");
        longitude.setText("Longitude: An error occured!");
    }

    @Override
    public void onLocationChanged(Location location) {
        myLastLocation = location;
        handleNewLocation();
    }

    protected void handleNewLocation(){
        latitude.setText("Latitude: " + String.valueOf(myLastLocation.getLatitude()));
        longitude.setText("Longitude: " + String.valueOf(myLastLocation.getLongitude()));
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        time.setText("" + date.toString());
    }
}
