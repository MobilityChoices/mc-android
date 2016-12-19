package org.mobilitychoices.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FullTrack extends Entity implements Serializable {
    private String owner;
    private ArrayList<Location> locations;
    private String created;

    public FullTrack() {
        locations = new ArrayList<>();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("owner", owner);
            jsonObject.put("creted", created);
            JSONArray locationsJson = new JSONArray();
            for (Location l :
                    locations) {
                JSONObject loc = new JSONObject();
                loc.put("time", l.getTime());
                JSONObject loc2 = new JSONObject();
                loc2.put("lat", l.getLatitude());
                loc2.put("lng", l.getLongitude());
                loc.put("location", loc2);
                locationsJson.put(loc);
            }
            jsonObject.put("locations", locationsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            owner = jsonObject.getString("owner");
            created = jsonObject.getString("created");
            JSONArray locations1 = jsonObject.getJSONArray("locations");
            for (int i = 0; i < locations1.length(); i++) {
                JSONObject loc = (JSONObject) locations1.get(i);
                long time = loc.getLong("time");
                JSONObject loc2 = loc.getJSONObject("location");
                double lat = loc2.getDouble("lat");
                double lng = loc2.getDouble("lon");
                Location location = new Location(lat, lng, 0, 0, time);
                locations.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}
