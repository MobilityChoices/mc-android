package org.mobilitychoices.remote;

import android.os.AsyncTask;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class UploadTrackTask extends AsyncTask<Object, Object, Response> {

    private String token;

    public UploadTrackTask(String token) {
        this.token = token;
    }

    @Override
    protected Response doInBackground(Object... jsonArrays) {
        JSONObject object = new JSONObject();
        Object tracks = jsonArrays[0];
        String urlString = "/tracks";
        try {
            object.put("locations", tracks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("Authorization", token));
        return new Connection().request(urlString, object, headers, Entity.class);
    }

}
