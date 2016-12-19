package org.mobilitychoices.entities;

import org.json.JSONObject;

public abstract class Entity {
    public void fromJSON(JSONObject jsonObject) {}
    public JSONObject toJSON() {
        return new JSONObject();
    }
}
