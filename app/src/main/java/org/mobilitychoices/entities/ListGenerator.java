package org.mobilitychoices.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class ListGenerator extends Entity {

    private ArrayList<JSONObject> routes = new ArrayList<>();

    protected abstract String getName();

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        //TODO
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            JSONArray r = jsonObject.getJSONArray(getName());
            for (int i = 0; i < r.length(); i++) {
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
