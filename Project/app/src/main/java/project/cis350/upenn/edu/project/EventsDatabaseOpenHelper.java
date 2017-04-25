package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 3/15/17.
 *
 * Helper functions to generate common SQLite commands for EventsDB
 */

public class EventsDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String EVENTS_TABLE_CREATE =
            "CREATE TABLE " + EventsDatabaseContract.EventsDB.TABLE_NAME + "(" +
                    EventsDatabaseContract.EventsDB.COL_USERNAME + " TEXT," +
                    EventsDatabaseContract.EventsDB.COL_GOALNAME + " TEXT," +
                    EventsDatabaseContract.EventsDB.COL_YEAR + " INT," +
                    EventsDatabaseContract.EventsDB.COL_MONTH + " INT," +
                    EventsDatabaseContract.EventsDB.COL_DAY + " INT," +
                    EventsDatabaseContract.EventsDB.COL_LOG + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventsDatabaseContract.EventsDB.TABLE_NAME;

    /**
     * Creates a DatabaseOpenHelper that can open the table in the specified Activity
     *
     * @param context the Activity in which to open the DB
     */
    public EventsDatabaseOpenHelper(Context context) {
        super(context, EventsDatabaseContract.EventsDB.TABLE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the UserDB table when the table is called for the first time
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENTS_TABLE_CREATE);
    }

    /**
     * If the new version of the table is greater than the old version, deletes the old table and
     * re-executes onCreate()
     * @param db
     * @param oldVersion the version number of the old table
     * @param newVersion the version number of the new table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * NOT IMPLEMENTED
     * calls onUpgrade()
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
