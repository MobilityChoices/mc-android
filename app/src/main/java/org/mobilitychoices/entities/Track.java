package org.mobilitychoices.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Track extends Entity {
    private String id;
    private String duration;
    private String created;
    private Start start;
    private End end;

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        //TODO
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            JSONObject startJSON = jsonObject.getJSONObject("start");
            JSONObject locationJSON = startJSON.getJSONObject("location");
            start = new Start(locationJSON.getDouble("lat"), locationJSON.getDouble("lon"));
            JSONObject endJSON = jsonObject.getJSONObject("end");
            JSONObject locationEndJSON = endJSON.getJSONObject("location");
            end = new End(locationEndJSON.getDouble("lat"), locationEndJSON.getDouble("lon"));

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

    public Start getStart() {
        return start;
    }

    public End getEnd() {
        return end;
    }

    public String getDuration() {
        return duration;
    }

    public String getCreated() {
        return created;
    }
}
