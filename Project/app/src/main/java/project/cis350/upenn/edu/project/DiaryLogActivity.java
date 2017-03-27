package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import java.util.Date;


import com.google.gson.Gson;

import java.util.ArrayList;

import static project.cis350.upenn.edu.project.R.id.reasons;


/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryLogActivity extends AppCompatActivity{

    User user;
    String username;
    ArrayList<String> reasons;
    String sentiment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_log);

        Gson gson = new Gson();
        String serializedUser = getIntent().getStringExtra("user");
        user = gson.fromJson(serializedUser, User.class);
        username = user.getID();
        reasons = user.getReasons();
        sentiment = user.getSentiment();

    }
    public void onSubmitButton(View v) {

        Date date = new Date();

        Intent i = new Intent(this, DiaryActivity.class);

        Gson gson = new Gson();
        i.putExtra("user", gson.toJson(user));

        //get diary entry
        String entry = ((EditText) findViewById(R.id.diary_text)).getText().toString();

        //get mood from api
        Mood m = new Mood(v.getContext());
        m.execute(new String[]{entry, username});


        //store entry
        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(v.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DiaryDatabaseContract.DiaryDB.COL_DATE, date + "");
        values.put(DiaryDatabaseContract.DiaryDB.COL_ENTRY, entry);
        values.put(DiaryDatabaseContract.DiaryDB.COL_USERNAME, username);
        db.insert(DiaryDatabaseContract.DiaryDB.TABLE_NAME, null, values);
        db.close();

        startActivity(i);
    }
    public void onMenuButton(View v) {

        Intent intent = new Intent(this, MainActivity.class);
        Gson gson = new Gson();
        intent.putExtra("user", gson.toJson(user));
        startActivity(intent);

    }

}
