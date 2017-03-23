package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.util.Date;
/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryLogActivity extends AppCompatActivity{
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_log);
    }
    public void onSubmitButton(View v) {

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        Date date = new Date();

        Intent i = new Intent(this, DiaryActivity.class);
        i.putExtra("username", username);

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
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
    
}
