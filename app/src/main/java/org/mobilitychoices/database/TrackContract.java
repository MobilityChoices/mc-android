package org.mobilitychoices.database;

import android.provider.BaseColumns;

final class TrackContract {
    private TrackContract(){}

    static class TrackEntry implements BaseColumns{
        static final String TRACKS_TABLE_NAME = "Tracks";
        static final String COLUMN_TIME = "time";
    }

    static class LocationEntry implements BaseColumns{
        static final String LOCATION_TABLE_NAME = "Location";
        static final String COLUMN_TRACKID = "track";
        static final String COLUMN_LATITUDE = "latitude";
        static final String COLUMN_LONGITUDE = "longitude";
        static final String COLUMN_ALTITUDE = "altitude";
        static final String COLUMN_SPEED = "speed";
        static final String COLUMN_TIME = "time";
    }
}
