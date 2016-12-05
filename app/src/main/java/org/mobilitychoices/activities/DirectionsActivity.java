package org.mobilitychoices.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.RouteListViewAdapter;
import org.mobilitychoices.entities.Route;
import org.mobilitychoices.remote.DirectionsTask;
import org.mobilitychoices.remote.Response;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity {

    private TextView textView;
    private ListView listView;
    private RouteListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.success);
        listView = (ListView) findViewById(R.id.routesListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(getApplicationContext(), "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
            }
        });

        ArrayList<Route> arrayOfRoutes= new ArrayList<Route>();
        adapter = new RouteListViewAdapter(this,R.layout.list_item,R.id.modes,arrayOfRoutes);
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
                if(response != null){
                    if (response.getCode() == 200) {
                        showAlternativeRoutes(response.getData());
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

    private void showAlternativeRoutes(Object data) {
        textView.setText("Success!");

        //TODO extract routes
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new Route("10", new String[]{"bicycling", "walking"}));
        routes.add(new Route("11", new String[]{"transit", "walking"}));
        routes.add(new Route("12", new String[]{"driving", "driving"}));
        routes.add(new Route("13", new String[]{"walking", "walking"}));

        adapter.clear();
        adapter.addAll(routes);
    }


}
