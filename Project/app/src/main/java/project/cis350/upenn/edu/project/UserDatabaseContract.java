package project.cis350.upenn.edu.project;

import android.provider.BaseColumns;

/**
 * Created by emmharv on 2/22/17.
 */

public class UserDatabaseContract {

    private UserDatabaseContract() {}

    public static class UserDB implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
        public static final String COL_REASONS = "reasons";
        public static final String COL_SENTIMENT = "sentiment";
    }

}
