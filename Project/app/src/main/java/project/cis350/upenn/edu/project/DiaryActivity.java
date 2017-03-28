package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryActivity extends AppCompatActivity{

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        Intent i = getIntent();
        username = i.getExtras().getString("username");

        List<String[]> contactList = new ArrayList<String[]>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + DiaryDatabaseContract.DiaryDB.TABLE_NAME;

        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(findViewById(R.id.diary_entries).getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        String[] projection = {
                DiaryDatabaseContract.DiaryDB.COL_USERNAME,
                DiaryDatabaseContract.DiaryDB.COL_DATE,
                DiaryDatabaseContract.DiaryDB.COL_ENTRY
        };

        // Filter results WHERE COL_USERNAME = username
        String selection = DiaryDatabaseContract.DiaryDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                DiaryDatabaseContract.DiaryDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        String diary = ((TextView) findViewById(R.id.diary_entries)).getText().toString();

        if (cursor.moveToFirst()) {
            do {
                diary = diary + cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_DATE))
                        + "\n" + cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_ENTRY))
                        + "\n" + "\n";

            } while (cursor.moveToNext());
            ((TextView) findViewById(R.id.diary_entries)).setText(diary);
        }

        cursor.close();

    }
    public void onMenuButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}