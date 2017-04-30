package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.*;
/**
 * Created by AK47 on 2/23/17.
 *
 * Helper functions to generate common SQLite commands for DiaryDB
 */

class DiaryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + DiaryDatabaseContract.DiaryDB.TABLE_NAME + "(" +
                    DiaryDatabaseContract.DiaryDB._ID + " INTEGER PRIMARY KEY," +
                    DiaryDatabaseContract.DiaryDB.COL_USERNAME + " TEXT," +
                    DiaryDatabaseContract.DiaryDB.COL_DATE + " TEXT," +
                    DiaryDatabaseContract.DiaryDB.COL_ENTRY + " TEXT);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiaryDatabaseContract.DiaryDB.TABLE_NAME;

    /**
     * Creates a DatabaseOpenHelper that can open the table in the specified Activity
     *
     * @param context the Activity in which to open the DB
     */
    public DiaryDatabaseHelper(Context context) {
        super(context, DiaryDatabaseContract.DiaryDB.TABLE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the UserDB table when the table is called for the first time
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
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
