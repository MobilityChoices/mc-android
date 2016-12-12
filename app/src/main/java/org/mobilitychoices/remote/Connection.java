package org.mobilitychoices.remote;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.entities.Entity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Connection<T extends Entity> {

    public Response request(String urlString, JSONObject object, List<Pair<String, String>> headers, Class<T> cls) {
        return request(urlString, object, headers, cls, "POST");
    }

    public Response request(String urlString, JSONObject object, List<Pair<String, String>> headers, Class<T> cls, String requestMethod) {
        Response<T> response = null;

        try {
            URL url = new URL("http://app.mobility-choices.org/mobility" + urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("User-Agent", "MC-Android");
            conn.setRequestProperty("Content-Type", "application/json");
            if (headers != null) {
                for (Pair<String, String> p : headers) {
                    conn.setRequestProperty(p.first, p.second);
                }
            }
            if (!requestMethod.equals("GET")) {
                conn.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());

                if (object != null) {
                    try {
                        System.out.println(object.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dataOutputStream.write(object.toString().getBytes());
                }

                dataOutputStream.flush();
                dataOutputStream.close();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("ResponseCode: " + responseCode);

            BufferedReader bufferedReader;
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            bufferedReader.close();
            String result = sb.toString();
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(result);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            if (jsonResult != null) {
                System.out.println(jsonResult.toString(4));
            }

            T data = null;
            ResponseError error = null;
            if (200 <= responseCode && responseCode <= 299) {
                data = (T) cls.newInstance();
                data.fromJSON(jsonResult);
            } else {
                error = new ResponseError("", "", "");
            }

            response = new Response<>(responseCode, data, error);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return response;
    }
}
