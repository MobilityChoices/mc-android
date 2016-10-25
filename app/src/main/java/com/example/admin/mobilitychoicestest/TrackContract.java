package com.example.admin.mobilitychoicestest;

import android.provider.BaseColumns;

/**
 * Created by admin on 25/10/2016.
 */

public final class TrackContract {
    private TrackContract(){}

    public static class TrackEntry implements BaseColumns{
        public static final String TRACKS_TABLE_NAME = "Tracks";
        public static final String COLUMN_TIME = "time";
    }

    public static class LocationEntry implements BaseColumns{
        public static final String LOCATION_TABLE_NAME = "Location";
        public static final String COLUMN_TRACKID = "track";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ALTITUDE = "altitude";
        public static final String COLUMN_SPEED = "speed";
        public static final String COLUMN_TIME = "time";
    }


}
