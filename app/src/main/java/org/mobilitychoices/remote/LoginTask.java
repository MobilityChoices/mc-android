package org.mobilitychoices.remote;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.mobilitychoices.entities.Entity;
import org.mobilitychoices.entities.Token;

public class LoginTask extends AsyncTask<JSONObject,Void,Response<Object>> {

    private ILoginCallback loginCallback;

    public LoginTask(ILoginCallback callback) {
       loginCallback = callback;
    }

    @Override
    protected Response<Object> doInBackground(JSONObject... jsonObjects) {
        JSONObject object = jsonObjects[0];
        String urlString = "/auth/login";
        return new Connection().request(urlString, object, null, Token.class);
    }
    @Override
    protected void onPostExecute(Response<Object> result) {
        loginCallback.done(result);
    }

    @FunctionalInterface
    public interface ILoginCallback{
        void done(Response<Object> success);
    }
}
