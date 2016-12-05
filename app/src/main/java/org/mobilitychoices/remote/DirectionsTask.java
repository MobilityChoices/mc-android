package org.mobilitychoices.remote;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.entities.Entity;

public class DirectionsTask extends AsyncTask<JSONObject,Void,Response<Object>> {

    private IDirectionsCallback iDirectionsCallback;

    public DirectionsTask(IDirectionsCallback callback){
        iDirectionsCallback = callback;
    }

    @Override
    protected Response<Object> doInBackground(JSONObject... jsonObject) {
        JSONObject object = jsonObject[0];
        String urlString = null;
        try {
            urlString = "/directions?origin=" + object.getString("origin") + "&destination=" + object.getString("destination");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Connection().request(urlString, object,null, Entity.class, "GET");
    }

    @Override
    protected void onPostExecute(Response<Object> result) {
        iDirectionsCallback.done(result);
    }

    @FunctionalInterface
    public interface IDirectionsCallback{
        void done(Response<Object> success);
    }
}
