package org.mobilitychoices.remote;

import android.os.AsyncTask;
import android.util.Pair;

import org.mobilitychoices.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class MeTask extends AsyncTask<Void,Void,Response<Object>> {

    private String token;
    private IMeCallback meCallback;

    public MeTask(IMeCallback callback, String token){
        this.meCallback = callback;
        this.token = token;
    }

    @Override
    protected Response<Object> doInBackground(Void... voids) {
        String urlString = "/me";

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("Authorization", token));

        return new Connection().request(urlString, null, headers, Entity.class, "GET");
    }

    @Override
    protected void onPostExecute(Response<Object> result) {
        meCallback.done(result);
    }

    @FunctionalInterface
    public interface IMeCallback{
        void done(Response<Object> success);
    }
}
