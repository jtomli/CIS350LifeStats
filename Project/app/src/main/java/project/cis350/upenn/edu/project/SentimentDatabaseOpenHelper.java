package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 3/15/17.
 *
 *  Helper functions to generate common SQLite commands for SentimentDB
 */

public class SentimentDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + SentimentDatabaseContract.SentimentDB.TABLE_NAME + "(" +
                    SentimentDatabaseContract.SentimentDB.COL_USERNAME + " TEXT," +
                    SentimentDatabaseContract.SentimentDB.COL_DATE + " TEXT," +
                    SentimentDatabaseContract.SentimentDB.COL_ANGER + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_DISGUST + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_FEAR + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_JOY + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_SADNESS + " DOUBLE);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SentimentDatabaseContract.SentimentDB.TABLE_NAME;

    /**
     * Creates a DatabaseOpenHelper that can open the table in the specified Activity
     *
     * @param context the Activity in which to open the DB
     */
    public SentimentDatabaseOpenHelper(Context context) {
        super(context, SentimentDatabaseContract.SentimentDB.TABLE_NAME, null, DATABASE_VERSION);
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