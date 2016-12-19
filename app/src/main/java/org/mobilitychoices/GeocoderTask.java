package org.mobilitychoices;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.mobilitychoices.entities.Track;

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
        for (Track t : tracks) {
            Geocoder geocoder = new Geocoder(context);
            List<Address> startAddress;
            List<Address> endAddress;
            try {
                startAddress = geocoder.getFromLocation(t.getStart().getLatitude(),
                        t.getStart().getLongitude(), 1);
                String address = startAddress.get(0).getThoroughfare();
                String city = startAddress.get(0).getLocality();

                t.getStart().setAddress(address + ", " + city);
                endAddress = geocoder.getFromLocation(t.getEnd().getLatitude(),
                        t.getEnd().getLongitude(), 1);
                String address2 = endAddress.get(0).getThoroughfare();
                String city2 = endAddress.get(0).getLocality();
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
