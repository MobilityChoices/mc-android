package org.mobilitychoices.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.adapter.AlternativeRoutesListViewAdapter;
import org.mobilitychoices.entities.Route;
import org.mobilitychoices.entities.RoutesList;
import org.mobilitychoices.remote.DirectionsTask;
import org.mobilitychoices.remote.Response;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity {

    private ListView listView;
    private AlternativeRoutesListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.routesListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
                Route route = adapter.getItem(position);
                System.out.println(route.getModes() + " --> " + route.getTime());
            }
        });

        ArrayList<Route> arrayOfRoutes = new ArrayList<Route>();
        adapter = new AlternativeRoutesListViewAdapter(this, R.layout.list_item, 0, arrayOfRoutes);
        listView.setAdapter(adapter);

        requestRoutes();
    }

    private void requestRoutes() {
        Bundle bundle = getIntent().getExtras();
        String o = bundle.getString("origin");
        String d = bundle.getString("destination");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("origin", o);
            jsonObject.put("destination", d);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DirectionsTask(new DirectionsTask.IDirectionsCallback() {
            @Override
            public void done(Response<Object> response) {
                if (response != null) {
                    if (response.getCode() == 200) {
                        showAlternativeRoutes((RoutesList) response.getData());
                    } else {
                        if (response.getCode() == 400) {
                            //irgendwas fehlt origin oder dest
                        } else {
                            Toast.makeText(DirectionsActivity.this.getApplicationContext(), String.valueOf(getString(R.string.internalServerError)), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }).execute(jsonObject);
    }

    private void showAlternativeRoutes(RoutesList data) {
        ArrayList<Route> routes = new ArrayList<>();

        ArrayList<JSONObject> Jroutes = data.getRoutes();

        for (int i = 0; i < Jroutes.size(); i++) {
            Route route = new Route(Jroutes.get(i));
            route.fromJSON(Jroutes.get(i));
            routes.add(route);
        }

        adapter.clear();
        adapter.addAll(routes);
    }


}
