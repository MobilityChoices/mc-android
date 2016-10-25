package com.example.admin.mobilitychoicestest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.admin.mobilitychoicestest.TrackContract.LocationEntry.LOCATION_TABLE_NAME;
import static com.example.admin.mobilitychoicestest.TrackContract.TrackEntry.TRACKS_TABLE_NAME;

/**
 * Created by admin on 25/10/2016.
 */

public class TracksOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String TRACKS_TABLE_CREATE =
            "CREATE TABLE " + TRACKS_TABLE_NAME + " (" + TrackContract.TrackEntry._ID +" INTEGER PRIMARY KEY, " +
                    TrackContract.TrackEntry.COLUMN_TIME +" INTEGER);";

    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE " + LOCATION_TABLE_NAME + " (" + TrackContract.LocationEntry._ID +
                    " INTEGER PRIMARY KEY, " + TrackContract.LocationEntry.COLUMN_TRACKID +" INTEGER," +
                    TrackContract.LocationEntry.COLUMN_LATITUDE +
                    " DOUBLE, " + TrackContract.LocationEntry.COLUMN_LONGITUDE +
                    " DOUBLE, " + TrackContract.LocationEntry.COLUMN_ALTITUDE +" DOUBLE, " +
                    TrackContract.LocationEntry.COLUMN_SPEED + " DOUBLE, " +
                    TrackContract.LocationEntry.COLUMN_TIME +" INTEGER);";

    public TracksOpenHelper(Context context) {
        super(context, "TrackDatabase.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRACKS_TABLE_CREATE);
        db.execSQL(LOCATION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
