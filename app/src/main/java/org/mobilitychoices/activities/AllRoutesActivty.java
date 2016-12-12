package org.mobilitychoices.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import org.mobilitychoices.GeocoderTask;
import org.mobilitychoices.R;
import org.mobilitychoices.adapter.AllRoutesListViewAdapter;
import org.mobilitychoices.entities.FullTrack;
import org.mobilitychoices.entities.Track;
import org.mobilitychoices.entities.TrackList;
import org.mobilitychoices.remote.GetAllTracksTask;
import org.mobilitychoices.remote.GetTrackTask;
import org.mobilitychoices.remote.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllRoutesActivty extends AppCompatActivity {
    private ListView listView;
    private AllRoutesListViewAdapter adapter;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_routes_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.allRoutesListView);
        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                new GetTrackTask(new GetTrackTask.IGetTrackCallback() {
                    @Override
                    public void done(Response<Object> result) {
                        System.out.println(((FullTrack)result.getData()).toJSON());
                    }
                }, token, track.getId()).execute();
            }
        });

        ArrayList<Track> arrayOfTracks = new ArrayList<Track>();
        adapter = new AllRoutesListViewAdapter(this, R.layout.list_item, 0, arrayOfTracks);
        listView.setAdapter(adapter);

        requestTracks(token);
    }

    private void requestTracks(String token) {
        new GetAllTracksTask(new GetAllTracksTask.IGetAllTracksCallback() {
            @Override
            public void done(Response<Object> response) {
                if (response != null) {
                    if (response.getCode() == 200) {
                        showTracks((TrackList) response.getData());
                    } else {
                        if (response.getCode() == 400) {
                            //irgendwas fehlt origin oder dest
                        } else {
                            Toast.makeText(AllRoutesActivty.this.getApplicationContext(), String.valueOf(getString(R.string.internalServerError)), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }, token).execute();
    }

    private void showTracks(TrackList data) {

        ArrayList<JSONObject> Jtracks = data.getRoutes();
        ArrayList<Track> tracks = new ArrayList<>();

        for (JSONObject t :
                Jtracks) {
            Track track = new Track();
            track.fromJSON(t);
            track.getStart().setAddress(track.getStart().getLatitude() + "&" + track.getStart().getLongitude());
            track.getEnd().setAddress(track.getEnd().getLatitude() + "&" + track.getEnd().getLongitude());
            tracks.add(track);
        }

        new GeocoderTask(new GeocoderTask.IGeocoderCallback() {
            @Override
            public void done(ArrayList<Track> response) {
                System.out.println(response.get(0).toJSON());
                adapter.clear();
                adapter.addAll(response);
            }
        }, getApplicationContext()).execute(tracks);
    }

}
