package project.cis350.upenn.edu.project;

import android.provider.BaseColumns;

/**
 * Created by emmharv on 3/15/17.
 *
 * Column names for GoalsDB
 */

public class GoalsDatabaseContract {

    private GoalsDatabaseContract() {}

    public static class GoalsDB implements BaseColumns {

        public static final String TABLE_NAME = "GOALS_TABLE";
        public static final String COL_USERNAME = "username";
        public static final String COL_GOALNAME = "goalname";
        public static final String COL_REASON = "reason";
        public static final String COL_ALLDAY = "allday";
        public static final String COL_STARTYEAR = "startyear";
        public static final String COL_STARTMONTH = "startmonth";
        public static final String COL_STARTDAY = "startday";
        public static final String COL_STARTHOUR = "starthour";
        public static final String COL_STARTMIN = "startmin";
        public static final String COL_STARTAMPM = "startampm";
        public static final String COL_ENDYEAR = "endyear";
        public static final String COL_ENDMONTH = "endmonth";
        public static final String COL_ENDDAY = "endday";
        public static final String COL_ENDHOUR = "endhour";
        public static final String COL_ENDMIN = "endmin";
        public static final String COL_ENDAMPM = "endampm";
        public static final String COL_REPEAT = "repeat";
        public static final String COL_FREQUENCY = "frequency";
        public static final String COL_REMINDME = "remindme";

    }
}