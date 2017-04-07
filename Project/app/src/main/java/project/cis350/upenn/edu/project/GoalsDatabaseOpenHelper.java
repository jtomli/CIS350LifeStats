package project.cis350.upenn.edu.project;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 3/15/17.
 */

public class GoalsDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    private static final String GOALS_TABLE_CREATE =
            "CREATE TABLE " + GoalsDatabaseContract.GoalsDB.TABLE_NAME + "(" +
                    GoalsDatabaseContract.GoalsDB.COL_USERNAME + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_REASON + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ALLDAY + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTYEAR + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTMONTH + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTDAY + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTHOUR + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTMIN + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_STARTAMPM + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDYEAR + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDMONTH + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDDAY + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDHOUR + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDMIN + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_ENDAMPM + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_REPEAT + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_FREQUENCY + " TEXT," +
                    GoalsDatabaseContract.GoalsDB.COL_REMINDME + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GoalsDatabaseContract.GoalsDB.TABLE_NAME;

    public GoalsDatabaseOpenHelper(Context context) {
        super(context, GoalsDatabaseContract.GoalsDB.TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GOALS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}