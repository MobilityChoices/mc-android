package org.mobilitychoices.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.adapter.AllRoutesListViewAdapter;
import org.mobilitychoices.entities.Track;
import org.mobilitychoices.entities.TrackList;
import org.mobilitychoices.remote.GetAllTracksTask;
import org.mobilitychoices.remote.Response;

import java.util.ArrayList;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();

            }
        });

        ArrayList<Track> arrayOfTracks = new ArrayList<Track>();
        adapter = new AllRoutesListViewAdapter(this, R.layout.list_item, 0, arrayOfTracks);
        listView.setAdapter(adapter);

        requestTracks();
    }

    private void requestTracks() {
        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);

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
        ArrayList<Track> tracks = new ArrayList<>();

        ArrayList<JSONObject> Jtracks = data.getRoutes();

        for (int i = 0; i < Jtracks.size(); i++) {
            Track track = new Track();
            track.fromJSON(Jtracks.get(i));
            tracks.add(track);
        }

        adapter.clear();
        adapter.addAll(tracks);
    }

}
