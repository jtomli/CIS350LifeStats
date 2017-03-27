package project.cis350.upenn.edu.project;

import android.content.Context;
import android.database.sqlite.*;
/**
 * Created by AK47 on 2/23/17.
 */

class DiaryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + DiaryDatabaseContract.DiaryDB.TABLE_NAME + "(" +
                    DiaryDatabaseContract.DiaryDB._ID + " INTEGER PRIMARY KEY," +
                    DiaryDatabaseContract.DiaryDB.COL_USERNAME + " TEXT," +
                    DiaryDatabaseContract.DiaryDB.COL_DATE + " TEXT," +
                    DiaryDatabaseContract.DiaryDB.COL_ENTRY + " TEXT);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiaryDatabaseContract.DiaryDB.TABLE_NAME;

    public DiaryDatabaseHelper(Context context) {
        super(context, DiaryDatabaseContract.DiaryDB.TABLE_NAME, null, DATABASE_VERSION);
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
