package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 2/22/17.
 *
 * Helper functions to generate common SQLite commands for UserDB
 */

public class UserDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + UserDatabaseContract.UserDB.TABLE_NAME + "(" +
                    UserDatabaseContract.UserDB._ID + " INTEGER PRIMARY KEY," +
                    UserDatabaseContract.UserDB.COL_USERNAME + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_1 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_2 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_3 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_4 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_5 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_6 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_7 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_8 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_9 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_10 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS_11 + " TEXT," +
                    UserDatabaseContract.UserDB.COL_SENTIMENT + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserDatabaseContract.UserDB.TABLE_NAME;

    /**
     * Creates a DatabaseOpenHelper that can open the table in the specified Activity
     *
     * @param context the Activity in which to open the DB
     */
    public UserDatabaseOpenHelper(Context context) {
        super(context, UserDatabaseContract.UserDB.TABLE_NAME, null, DATABASE_VERSION);
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