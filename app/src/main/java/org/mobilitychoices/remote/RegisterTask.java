package org.mobilitychoices.remote;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Arrays;

public class RegisterTask extends AsyncTask<JSONObject, Void, Boolean> {

    private IRegisterCallback registerCallback;

    public RegisterTask(IRegisterCallback callback){
        registerCallback = callback;
    }

    @Override
    protected Boolean doInBackground(JSONObject... jsonObject) {
        //TODO
        JSONObject object = jsonObject[0];
        System.out.println(Arrays.toString(object.toString().getBytes()));
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        registerCallback.done(result);
    }

    @FunctionalInterface
    public interface IRegisterCallback{
        void done(boolean success);
    }
}

