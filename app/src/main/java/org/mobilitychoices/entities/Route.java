package org.mobilitychoices.entities;

import org.json.JSONObject;

public class Route {
    private JSONObject routes;
    private String time;
    private String[] modes;

    public Route(JSONObject routes){
        this.routes = routes;
    }

    public Route(String time, String[] modes){
        this.time = time;
        this.modes = modes;
    }

    public String getTitle(String p){
        return p;
    }

    public String[] getModes() {
        return modes;
    }

    public void setModes(String[] modes) {
        this.modes = modes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
