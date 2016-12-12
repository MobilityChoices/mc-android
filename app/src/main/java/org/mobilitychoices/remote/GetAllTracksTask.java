package org.mobilitychoices.remote;

import android.os.AsyncTask;
import android.util.Pair;

import org.mobilitychoices.entities.TrackList;

import java.util.ArrayList;
import java.util.List;

public class GetAllTracksTask extends AsyncTask<Void, Void, Response<Object>> {
    private IGetAllTracksCallback iGetAllTracksCallback;
    private String token;

    public GetAllTracksTask(IGetAllTracksCallback callback, String token) {
        iGetAllTracksCallback = callback;
        this.token = token;
    }

    @Override
    protected Response<Object> doInBackground(Void... voids) {
        String urlString = "/tracks";

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("Authorization", token));

        return new Connection().request(urlString, null, headers, TrackList.class, "GET");
    }

    @Override
    protected void onPostExecute(Response<Object> result) {
        iGetAllTracksCallback.done(result);
    }

    @FunctionalInterface
    public interface IGetAllTracksCallback {
        void done(Response<Object> success);
    }
}
