package org.mobilitychoices.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.mobilitychoices.R;
import org.mobilitychoices.database.DbFacade;
import org.mobilitychoices.entities.FullTrack;
import org.mobilitychoices.entities.Location;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<Location> locations;
    private DbFacade dbFacade;
    private Button showAlternativesBtn;
    private boolean isFullTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ArrayList<Location> locations1 = (ArrayList<Location>) getIntent().getExtras().getSerializable("locations");
        if (locations1 != null) {
            locations = locations1;
        } else {
            FullTrack track = (FullTrack) (getIntent().getExtras().getSerializable("fullTrack"));
            if (track == null) {
                dbFacade = DbFacade.getInstance(this);
                long currentTrack = (long) getIntent().getExtras().get("currentTrack");
                locations = dbFacade.getTrack(currentTrack);
                isFullTrack = false;
            } else {
                locations = track.getLocations();
                isFullTrack = true;
            }
        }
        showAlternativesBtn = (Button) findViewById(R.id.showAlternativesBtn);
        showAlternativesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String origin = locations.get(0).getLatitude() + "," + locations.get(0).getLongitude();
                String destination = locations.get(locations.size() - 1).getLatitude() + "," + locations.get(locations.size() - 1).getLongitude();

                Intent directionsIntent = new Intent(MapsActivity.this, DirectionsActivity.class);
                directionsIntent.putExtra("origin", origin);
                directionsIntent.putExtra("destination", destination);
                startActivity(directionsIntent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (Location location :
                locations) {
            latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        if (locations != null && locations.size() >= 1) {
            LatLng latLng = new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            PolylineOptions po = new PolylineOptions().geodesic(true);
            for (LatLng l :
                    latLngs) {
                po.add(l);
            }
            if (isFullTrack) {
                po.color(Color.BLUE);
            }
            googleMap.addPolyline(po);
        } else {
            Toast.makeText(MapsActivity.this.getApplicationContext(), R.string.ErrorNoLocationsInList, Toast.LENGTH_LONG).show();
        }

    }
}
