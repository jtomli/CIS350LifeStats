package com.example.jamietomlinson.iteration2;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        List<String[]> contactList = new ArrayList<String[]>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DiaryDatabaseContract.DiaryDB.TABLE_NAME;

        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(findViewById(R.id.diary_entries).getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String diary = ((TextView) findViewById(R.id.diary_entries)).getText().toString();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                diary = diary + cursor.getString(1) + "\n" + cursor.getString(2) + "\n" + "\n";

            } while (cursor.moveToNext());
            ((TextView) findViewById(R.id.diary_entries)).setText(diary);
        }
    }
    public void onMenuButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
