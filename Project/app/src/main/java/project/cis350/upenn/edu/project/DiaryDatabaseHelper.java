package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.*;
/**
 * Created by AK47 on 2/23/17.
 */

class DiaryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + DiaryDatabaseContract.UserDB.TABLE_NAME + "(" +
                    DiaryDatabaseContract.UserDB._ID + " INTEGER PRIMARY KEY," +
                    DiaryDatabaseContract.UserDB.COL_DATE + " TEXT," +
                    DiaryDatabaseContract.UserDB.COL_ENTRY + " TEXT);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiaryDatabaseContract.UserDB.TABLE_NAME;

    public DiaryDatabaseHelper(Context context) {
        super(context, DiaryDatabaseContract.UserDB.TABLE_NAME, null, DATABASE_VERSION);
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
