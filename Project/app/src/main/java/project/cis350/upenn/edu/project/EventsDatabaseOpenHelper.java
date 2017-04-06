package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 3/15/17.
 */

public class EventsDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

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

    public EventsDatabaseOpenHelper(Context context) {
        super(context, EventsDatabaseContract.EventsDB.TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENTS_TABLE_CREATE);
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
