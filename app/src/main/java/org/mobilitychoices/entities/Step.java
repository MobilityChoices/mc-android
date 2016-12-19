package org.mobilitychoices.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Step extends Entity {
    private int distance;
    private int duration;
    private Point start;
    private Point end;
    private ArrayList<Step> substeps;
    private String travelmode;

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            distance = jsonObject.getInt("distance");
            duration  =jsonObject.getInt("duration");
            travelmode= jsonObject.getString("travelMode");

            JSONObject start = jsonObject.getJSONObject("start");
            JSONObject locStart = start.getJSONObject("location");
            this.start = new Point(locStart.getDouble("lat"), locStart.getDouble("lng"));

            JSONObject end = jsonObject.getJSONObject("end");
            JSONObject locEnd = end.getJSONObject("location");
            this.end = new Point(locEnd.getDouble("lat"), locEnd.getDouble("lng"));

            JSONArray subs = jsonObject.getJSONArray("steps");
            substeps = new ArrayList<>();
            for (int i = 0; i < subs.length(); i++){
                Step step = new Step();
                step.fromJSON(subs.getJSONObject(i));
                substeps.add(step);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public ArrayList<Step> getSubsteps() {
        return substeps;
    }

    public String getTravelmode() {
        return travelmode;
    }
}
