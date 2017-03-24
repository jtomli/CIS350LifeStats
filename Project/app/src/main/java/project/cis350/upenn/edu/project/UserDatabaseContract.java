package com.example.jamietomlinson.iteration2;

import android.provider.BaseColumns;

/**
 * Created by emmharv on 2/22/17.
 */

public class UserDatabaseContract {

    private UserDatabaseContract() {}

    public static class UserDB implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COL_USERNAME = "username";
        public static final String COL_REASONS_1 = "reasons0";
        public static final String COL_REASONS_2 = "reasons1";
        public static final String COL_REASONS_3 = "reasons2";
        public static final String COL_REASONS_4 = "reasons3";
        public static final String COL_REASONS_5 = "reasons4";
        public static final String COL_REASONS_6 = "reasons5";
        public static final String COL_REASONS_7 = "reasons6";
        public static final String COL_REASONS_8 = "reasons7";
        public static final String COL_REASONS_9 = "reasons8";
        public static final String COL_REASONS_10 = "reasons9";
        public static final String COL_REASONS_11 = "reasons10";
        public static final String COL_SENTIMENT = "sentiment";
    }

}
