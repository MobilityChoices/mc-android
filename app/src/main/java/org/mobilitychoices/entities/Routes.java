package org.mobilitychoices.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Routes extends Entity {

    ArrayList<JSONObject> routes = new ArrayList<>();

    @Override
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        //TODO
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject){
        try {
            JSONArray r = jsonObject.getJSONArray("routes");
            for (int i = 0; i < r.length(); i++){
                routes.add((JSONObject) r.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<JSONObject> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<JSONObject> routes) {
        this.routes = routes;
    }
}
