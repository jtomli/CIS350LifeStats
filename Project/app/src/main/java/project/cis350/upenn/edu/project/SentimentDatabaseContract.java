package project.cis350.upenn.edu.project;

import android.provider.BaseColumns;

/**
 * Created by emmharv on 3/15/17.
 */

public class SentimentDatabaseContract {

    private SentimentDatabaseContract() {}

    public static class SentimentDB implements BaseColumns {

        public static final String TABLE_NAME = "SENTIMENT_TABLE";
        public static final String COL_USERNAME = "username";
        public static final String COL_YEAR = "year";
        public static final String COL_MONTH = "month";
        public static final String COL_DAY = "day";
        public static final String COL_FEELINGS = "sentiment";
    }
}
