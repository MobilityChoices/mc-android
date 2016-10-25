package com.example.admin.mobilitychoicestest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient myGoogleApiClient;
    LocationRequest myLocationRequest;

    TextView latitude;
    TextView longitude;
    TextView time;
    Button startStopBtn;
    ListView trackList;
    boolean isTracking;
    ArrayList<MCLocation> tracks;
    JSONArray jsonTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        latitude = (TextView) findViewById(R.id.textLatitude);
        longitude = (TextView) findViewById(R.id.textLongitude);
        time = (TextView) findViewById(R.id.textView);
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        trackList = (ListView) findViewById(R.id.trackList);

        startTracking();

        startStopBtn.setOnClickListener(view -> {
            isTracking = !isTracking;
            if (isTracking) {
                startStopBtn.setText("Stop");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    System.out.println("permission check failed");
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);

                tracks = new ArrayList<>();
                jsonTracks = new JSONArray();

            } else {
                startStopBtn.setText("Start");
                LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleApiClient, this);
                String[] listItems = new String[tracks.size()];
                for (int i = 0; i < tracks.size(); i++) {
                    MCLocation location = tracks.get(i);
                    listItems[i] = "Lat: " + location.getLatitude() + "; Lng: " + location.getLongitude();
                    jsonTracks.put(location.toJSON());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
                trackList.setAdapter(adapter);

                try {
                    System.out.println(jsonTracks.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendData();
            }
        });

        myGoogleApiClient.connect();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void sendData() {
       new UploadTrackTask().execute(jsonTracks);
    }

    protected synchronized void startTracking() {
        myGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        myLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 1000).setFastestInterval(1000);
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
        startStopBtn.setEnabled(true);

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
        handleNewLocation(new MCLocation(location));
    }

    protected void handleNewLocation(MCLocation location) {
        latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
        longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        time.setText(date.toString());

        tracks.add(location);

        System.out.println(location.toJSON());

    }
}
