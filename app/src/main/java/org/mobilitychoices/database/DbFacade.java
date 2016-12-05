package org.mobilitychoices.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mobilitychoices.entities.Location;

import java.util.ArrayList;

public class DbFacade {

    private SQLiteDatabase db;
    TracksOpenHelper helper;

    public static DbFacade getInstance(Context context){
        return new DbFacade(context);
    }

    private DbFacade(Context context) {
        helper = new TracksOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public long saveLocation(Location location, long currentTrack) {
        ContentValues values = new ContentValues();
        values.put(TrackContract.LocationEntry.COLUMN_TRACK_ID, currentTrack);
        values.put(TrackContract.LocationEntry.COLUMN_LATITUDE, location.getLatitude());
        values.put(TrackContract.LocationEntry.COLUMN_LONGITUDE, location.getLongitude());
        values.put(TrackContract.LocationEntry.COLUMN_ALTITUDE, location.getAltitude());
        values.put(TrackContract.LocationEntry.COLUMN_SPEED, location.getSpeed());
        values.put(TrackContract.LocationEntry.COLUMN_TIME, location.getTime());

        return db.insert(TrackContract.LocationEntry.LOCATION_TABLE_NAME, null, values);
    }

    public long saveTrack(long time) {
        ContentValues values = new ContentValues();
        values.put(TrackContract.TrackEntry.COLUMN_TIME, time);
        return db.insert(TrackContract.TrackEntry.TRACKS_TABLE_NAME, null, values);
    }

    public ArrayList<Location> getTrack(long currentTrack){
        String query = "Select * from " + TrackContract.LocationEntry.LOCATION_TABLE_NAME + " WHERE "
                + TrackContract.LocationEntry.COLUMN_TRACK_ID + " = " + currentTrack + ";";

        ArrayList<Location> arraylist = new ArrayList<>();
        SQLiteDatabase sql= helper.getReadableDatabase();

        Cursor c = sql.rawQuery(query, null);
        while(c.moveToNext()) {
            Location location = new Location(c.getDouble(2), c.getDouble(3), c.getDouble(4), c.getDouble(5), c.getLong(6));
            arraylist.add(location);
        }
        c.close();
        return arraylist;
    }
}
