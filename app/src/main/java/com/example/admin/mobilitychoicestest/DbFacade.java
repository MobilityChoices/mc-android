package com.example.admin.mobilitychoicestest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 25/10/2016.
 */

public class DbFacade {

    SQLiteDatabase db;

    public DbFacade(Context context) {
        TracksOpenHelper helper = new TracksOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public long saveLocation(MCLocation location, long currentTrack) {
        ContentValues values = new ContentValues();
        values.put(TrackContract.LocationEntry.COLUMN_TRACKID, currentTrack);
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
}
