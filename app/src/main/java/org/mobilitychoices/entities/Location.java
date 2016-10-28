package org.mobilitychoices.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;
    private long time;

    public Location(android.location.Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
        this.speed = location.getSpeed();
        this.time = location.getTime();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public long getTime() {
        return time;
    }

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("latitude", Double.valueOf(latitude));
            obj.put("longitude", Double.valueOf(longitude));
            obj.put("altitude", Double.valueOf(altitude));
            obj.put("speed", Double.valueOf(speed));
            obj.put("time", Long.valueOf(time));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
