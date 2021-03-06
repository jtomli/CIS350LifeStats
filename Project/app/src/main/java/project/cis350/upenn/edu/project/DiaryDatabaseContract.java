package project.cis350.upenn.edu.project;

import android.provider.BaseColumns;
/**
 * Created by AK47 on 2/23/17.
 *
 * Column names for DiaryDB
 */

public class DiaryDatabaseContract {
    private DiaryDatabaseContract() {}

    public static class DiaryDB implements BaseColumns {
        public static final String TABLE_NAME = "diary";
        public static final String COL_USERNAME = "username";
        public static final String COL_DATE = "date";
        public static final String COL_ENTRY = "entry";
    }

}
