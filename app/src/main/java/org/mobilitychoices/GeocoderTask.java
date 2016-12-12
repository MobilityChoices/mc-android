package org.mobilitychoices;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.mobilitychoices.entities.Track;
import org.mobilitychoices.remote.GetAllTracksTask;
import org.mobilitychoices.remote.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeocoderTask extends AsyncTask<ArrayList<Track>, Object, ArrayList<Track>> {

    private IGeocoderCallback iGeocoderCallback;
    private Context context;
    public GeocoderTask(IGeocoderCallback callback, Context c) {
        iGeocoderCallback = callback;
        context = c;
    }

    @Override
    protected ArrayList<Track> doInBackground(ArrayList<Track>... arrayLists) {
        ArrayList<Track> tracks = arrayLists[0];

        for (Track t :
                tracks) {
            System.out.println("Geocoder loop: " + t.getId());
            Geocoder geocoder;
            List<Address> addresses1;
            List<Address> addresses2;
            geocoder = new Geocoder(context);
            try {
                addresses1 = geocoder.getFromLocation(t.getStart().getLatitude(),
                        t.getStart().getLongitude(), 1);
                String address = addresses1.get(0).getThoroughfare();
                String city = addresses1.get(0).getLocality();
                System.out.println(addresses1.get(0).toString());

                t.getStart().setAddress(address + ", " + city);

                addresses2 = geocoder.getFromLocation(t.getEnd().getLatitude(),
                        t.getEnd().getLongitude(), 1);
                String address2 = addresses2.get(0).getThoroughfare();
                String city2 = addresses2.get(0).getLocality();

                t.getEnd().setAddress(address2 + ", " + city2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tracks;
    }

    @Override
    protected void onPostExecute(ArrayList<Track> result) {
        iGeocoderCallback.done(result);
    }

    @FunctionalInterface
    public interface IGeocoderCallback {
        void done(ArrayList<Track> success);
    }
}
