package org.mobilitychoices.remote;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class UploadTrackTask extends AsyncTask<JSONArray, Void, Void> {
    @Override
    protected Void doInBackground(JSONArray... jsonArrays) {
        JSONObject object = new JSONObject();
        JSONArray tracks = jsonArrays[0];
        String urlString = "http://172.22.13.195:3000/track";

        try {
            object.put("data", tracks);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "MC-Android");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            System.out.println(Arrays.toString(object.toString().getBytes()));
            dataOutputStream.write(object.toString().getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();
            int responseCode = conn.getResponseCode();
            System.out.println("ResponseCode: " + responseCode);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
