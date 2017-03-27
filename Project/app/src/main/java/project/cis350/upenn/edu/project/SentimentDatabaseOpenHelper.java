package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 3/15/17.
 */

public class SentimentDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + SentimentDatabaseContract.SentimentDB.TABLE_NAME + "(" +
                    SentimentDatabaseContract.SentimentDB.COL_USERNAME + " TEXT," +
                    SentimentDatabaseContract.SentimentDB.COL_DATE + " INT," +
                    SentimentDatabaseContract.SentimentDB.COL_ANGER + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_DISGUST + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_FEAR + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_JOY + " DOUBLE," +
                    SentimentDatabaseContract.SentimentDB.COL_SADNESS + " DOUBLE);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SentimentDatabaseContract.SentimentDB.TABLE_NAME;

    public SentimentDatabaseOpenHelper(Context context) {
        super(context, SentimentDatabaseContract.SentimentDB.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
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