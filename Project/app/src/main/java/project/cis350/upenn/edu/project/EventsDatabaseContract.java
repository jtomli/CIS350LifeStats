package com.example.jamietomlinson.iteration2;

import android.provider.BaseColumns;

/**
 * Created by emmharv on 3/15/17.
 */

public class EventsDatabaseContract {

    private EventsDatabaseContract() {}

    public static class EventsDB implements BaseColumns {

        public static final String TABLE_NAME = "GOALS_TABLE";
        public static final String COL_USERNAME = "username";
        public static final String COL_GOALNAME = "goalname";
        public static final String COL_YEAR = "year";
        public static final String COL_MONTH = "month";
        public static final String COL_DAY = "day";
        public static final String COL_STARTHOUR = "starthour";
        public static final String COL_STARTMIN = "startmin";
        public static final String COL_ENDHOUR = "endhour";
        public static final String COL_ENDMIN = "endmin";
        public static final String COL_ALLDAY = "allday";
    }
}
