package com.example.admin.mobilitychoicestest;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 25/10/2016.
 */

public class UploadTrackTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        JSONObject object = new JSONObject();
        JSONArray tracks = (JSONArray) objects[0];
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
            System.out.println(object.toString().getBytes());
            dataOutputStream.write(object.toString().getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();
            int responseCode = conn.getResponseCode();
            System.out.println("ResponseCode: " + responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
