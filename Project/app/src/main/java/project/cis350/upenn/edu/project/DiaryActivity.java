package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryActivity extends AppCompatActivity{
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        List<String[]> contactList = new ArrayList<String[]>();

        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(findViewById(R.id.diary_entries).getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        String[] projection = {
                DiaryDatabaseContract.DiaryDB.COL_DATE,
                DiaryDatabaseContract.DiaryDB.COL_ENTRY
        };
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
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                diary = diary + cursor.getString(0) + "\n" + cursor.getString(1) + "\n" + "\n";

            } while (cursor.moveToNext());
            ((TextView) findViewById(R.id.diary_entries)).setText(diary);
        }
    }
    public void onMenuButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}

