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
        public static final String COL_DATE = "date";
        public static final String COL_ANGER = "anger";
        public static final String COL_DISGUST = "disgust";
        public static final String COL_FEAR = "fear";
        public static final String COL_JOY = "joy";
        public static final String COL_SADNESS = "sadness";

    }
}