package org.mobilitychoices.remote;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.mobilitychoices.entities.Entity;

public class RegisterTask extends AsyncTask<JSONObject, Void, Response<Object>> {

    private IRegisterCallback registerCallback;

    public RegisterTask(IRegisterCallback callback) {
        registerCallback = callback;
    }

    @Override
    protected Response<Object> doInBackground(JSONObject... jsonObject) {
        JSONObject object = jsonObject[0];
        String urlString = "/auth/register";
        return new Connection().request(urlString, object, null, Entity.class);
    }

    @Override
    protected void onPostExecute(Response<Object> result) {
        registerCallback.done(result);
    }

    @FunctionalInterface
    public interface IRegisterCallback {
        void done(Response<Object> success);
    }
}

