package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
<<<<<<< HEAD
import android.widget.*;
=======
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

>>>>>>> master
/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryActivity extends AppCompatActivity{
<<<<<<< HEAD
    String username;
=======
    User user;
    String username;
    ArrayList<String> reasons;
    String sentiment;

>>>>>>> master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

<<<<<<< HEAD
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
=======
        Gson gson = new Gson();
        String serializedUser = getIntent().getStringExtra("user");
        user = gson.fromJson(serializedUser, User.class);
        username = user.getID();
        reasons = user.getReasons();
        sentiment = user.getSentiment();
>>>>>>> master

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
<<<<<<< HEAD
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}

=======
        Intent intent = new Intent(this, MainActivity.class);
        Gson gson = new Gson();
        intent.putExtra("user", gson.toJson(user));
        startActivity(intent);
    }
}
>>>>>>> master
