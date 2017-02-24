package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emmharv on 2/22/17.
 */

public class UserDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + UserDatabaseContract.UserDB.TABLE_NAME + "(" +
                    UserDatabaseContract.UserDB._ID + " INTEGER PRIMARY KEY," +
                    UserDatabaseContract.UserDB.COL_USERNAME + " TEXT," +
                    UserDatabaseContract.UserDB.COL_PASSWORD + " TEXT," +
                    UserDatabaseContract.UserDB.COL_REASONS + " TEXT," +
                    UserDatabaseContract.UserDB.COL_SENTIMENT + " Text);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserDatabaseContract.UserDB.TABLE_NAME;

    public UserDatabaseOpenHelper(Context context) {
        super(context, UserDatabaseContract.UserDB.TABLE_NAME, null, DATABASE_VERSION);
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
