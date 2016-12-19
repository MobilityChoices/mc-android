package org.mobilitychoices.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Route extends Entity {

    private String time;
    private ArrayList<String> modes;
    private ArrayList<Step> steps;

    public Route(JSONObject routes) {
        modes = new ArrayList<>();
    }

    public Route(String time, ArrayList<String> modes) {
        this.time = time;
        this.modes = modes;
        if (modes == null) {
            this.modes = new ArrayList<>();
        }
    }

    public ArrayList<String> getModes() {
        return modes;
    }

    public void setModes(ArrayList<String> modes) {
        this.modes = modes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            JSONArray legs = jsonObject.getJSONArray("legs");
            for (int i = 0; i < legs.length(); i++) {
                JSONObject object = (JSONObject) legs.get(i);
                int timeInSec = object.getInt("duration") / 60;
                time = timeInSec + "";

                steps = new ArrayList<>();
                //steps
                JSONArray steps = object.getJSONArray("steps");
                for (int j = 0; j < steps.length(); j++){
                    Step step = new Step();
                    step.fromJSON(steps.getJSONObject(j));
                    this.steps.add(step);
                }
            }
            JSONArray travelModes = jsonObject.getJSONArray("travelMode");
            for (int i = 0; i < travelModes.length(); i++) {
                modes.add(travelModes.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }
}
