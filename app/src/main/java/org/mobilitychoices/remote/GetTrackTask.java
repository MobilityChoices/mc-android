package org.mobilitychoices.remote;

import android.os.AsyncTask;
import android.util.Pair;

import org.mobilitychoices.entities.FullTrack;
import org.mobilitychoices.entities.TrackList;

import java.util.ArrayList;
import java.util.List;

public class GetTrackTask extends AsyncTask<Void, Void, Response<Object>> {

    private IGetTrackCallback iGetTracksCallback;
    private String id;
    private String token;

    public GetTrackTask(IGetTrackCallback callback, String token, String id) {
        iGetTracksCallback = callback;
        this.token = token;
        this.id = id;
    }

    @Override
    protected Response<Object> doInBackground(Void... voids) {
        String urlString = "/tracks/"+ id;
        System.out.println("GetTrackTask in Background...");
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("Authorization", token));

        return new Connection().request(urlString, null, headers, FullTrack.class, "GET");
    }

    @Override
    protected void onPostExecute(Response<Object> result) {
        iGetTracksCallback.done(result);
    }

    @FunctionalInterface
    public interface IGetTrackCallback {
        void done(Response<Object> success);
    }
}
