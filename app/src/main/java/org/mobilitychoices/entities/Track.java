package org.mobilitychoices.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Track extends Entity {
    private String id;
    private String duration;
    private String created;
    private Point start;
    private Point end;

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("duration", duration);
            jsonObject.put("created", created);
            JSONObject start2 = new JSONObject();
            start2.put("lat", start.getLatitude());
            start2.put("lng", start.getLongitude());
            start2.put("address", start.getAddress());
            jsonObject.put("start", start2);
            JSONObject end2 = new JSONObject();
            end2.put("lat", end.getLatitude());
            end2.put("lng", end.getLongitude());
            end2.put("address", end.getAddress());
            jsonObject.put("end", end2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            JSONObject startJSON = jsonObject.getJSONObject("start");
            JSONObject locationJSON = startJSON.getJSONObject("location");
            start = new Point(locationJSON.getDouble("lat"), locationJSON.getDouble("lon"));
            JSONObject endJSON = jsonObject.getJSONObject("end");
            JSONObject locationEndJSON = endJSON.getJSONObject("location");
            end = new Point(locationEndJSON.getDouble("lat"), locationEndJSON.getDouble("lon"));

            JSONObject strings  = jsonObject.getJSONObject("strings");
            duration = strings.getString("duration");
            created = strings.getString("created");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public String getDuration() {
        return duration;
    }

    public String getCreated() {
        return created;
    }
}
