package org.mobilitychoices.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mobilitychoices.R;
import org.mobilitychoices.database.DbFacade;
import org.mobilitychoices.entities.Location;

import java.util.ArrayList;

public class MapsAlternativeActivity extends AppCompatActivity {
    private ListView trackList;
    private ArrayList<Location> locations;
    private DbFacade dbFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_alternative);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trackList = (ListView) findViewById(R.id.trackList);
        dbFacade = DbFacade.getInstance(this);
        long currentTrack =  (long) getIntent().getExtras().get("currentTrack");
        locations = dbFacade.getTrack(currentTrack);

        String[] listItems = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            listItems[i] = "Lat: " + location.getLatitude() + "; Lng: " + location.getLongitude();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        trackList.setAdapter(adapter);
    }

}
