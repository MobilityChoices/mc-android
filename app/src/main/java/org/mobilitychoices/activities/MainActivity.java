package org.mobilitychoices.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.mobilitychoices.R;
import org.mobilitychoices.database.DbFacade;
import org.mobilitychoices.entities.Location;
import org.mobilitychoices.remote.UploadTrackTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
This class is the activity for the main screen. It contains the GPS Tracking (Start, Stop..)
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, android.location.LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private TextView latitude;
    private TextView longitude;
    private TextView time;
    private Button startStopBtn;
    private Button showAllRoutesBtn;


    private boolean isTracking;
    private ArrayList<Location> locations;
    private JSONArray jsonTracks;
    private DbFacade dbFacade;
    private boolean hasGooglePlay;

    private long currentTrack;

    private final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        latitude = (TextView) findViewById(R.id.textLatitude);
        longitude = (TextView) findViewById(R.id.textLongitude);
        time = (TextView) findViewById(R.id.currentPosition);
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        showAllRoutesBtn = (Button) findViewById(R.id.showAllRoutesBtn);

        dbFacade = DbFacade.getInstance(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        requestLocationPermissions();

        //check if google play is supported
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            startTracking();
            hasGooglePlay = true;
            System.out.println("Google Play Services are activated");
        } else {
            hasGooglePlay = false;
            System.out.println("Google Play Services are deactivated");
        }

        final MainActivity self = this;

        startStopBtn.setEnabled(true);
        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                self.doIT(locationManager);
            }
        });
        if (hasGooglePlay) {
            googleApiClient.connect();
        }

        showAllRoutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showRoutesIntent = new Intent(MainActivity.this, AllRoutesActivty.class);
                startActivity(showRoutesIntent);
            }
        });
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission check failed");
            final int PERMISSION_ALL = 0;
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void doIT(LocationManager locationManager) {
        if (!isTracking) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission check failed - oh noo");
                return;
            }
            isTracking = !isTracking;
            startStopBtn.setText(R.string.stop);
            startStopBtn.setBackgroundColor(Color.parseColor("#F44336"));

            if (hasGooglePlay) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                System.out.println("Google Play Services are used");
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                System.out.println("Android GPS Services are used");
            }

            locations = new ArrayList<>();
            jsonTracks = new JSONArray();
            currentTrack = dbFacade.saveTrack(System.currentTimeMillis());
        } else {
            isTracking = false;
            startStopBtn.setText(R.string.start);
            startStopBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
            if (hasGooglePlay) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                System.out.println("Google Play Services are used to remove updates");
            } else {
                locationManager.removeUpdates(this);
                System.out.println("Android GPS Services are used to remove updates");
            }
            if (locations != null && locations.size() >= 2) {
                try {
                    System.out.println(jsonTracks.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                new UploadTrackTask(token).execute(jsonTracks);

                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                mapsIntent.putExtra("currentTrack", currentTrack);
                startActivity(mapsIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
            }
        }
    }

    protected synchronized void startTracking() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);
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
        if (R.id.action_logout == id) {
            SharedPreferences sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.remove("token");
            editor.apply();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startStopBtn.setEnabled(true);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        latitude.setText(R.string.latitudeError);
        longitude.setText(R.string.longitudeError);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        handleNewLocation(new Location(location));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    protected void handleNewLocation(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        DecimalFormat f = new DecimalFormat("###.000000");
        latitude.setText(String.format("%s%s", getString(R.string.latitude), f.format(lat)));
        longitude.setText(String.format("%s%s", getString(R.string.longitude), f.format(lng)));

        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time.setText("Current position: " + dateFormat.format(date));

        locations.add(location);
        jsonTracks.put(location.toJSON());
        long id = dbFacade.saveLocation(location, currentTrack);
        System.out.println("New location db-id: " + id);
    }
}
