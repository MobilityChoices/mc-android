package org.mobilitychoices.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Track extends Entity {
    private String id;
    private String time;
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
            start = new Start(startJSON.getString("address"), locationJSON.getDouble("lat"), locationJSON.getDouble("lon"));
            JSONObject endJSON = jsonObject.getJSONObject("end");
            JSONObject locationEndJSON = startJSON.getJSONObject("location");
            end = new End(endJSON.getString("address"), locationEndJSON.getDouble("lat"), locationEndJSON.getDouble("lon"));
            //TODO time
            time = "4722 sec";
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

    public String getTime() {
        return time;
    }
}
