package project.cis350.upenn.edu.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SetupActivitySentiment extends AppCompatActivity {

    String username;
    User user;
    ArrayList<String> reasons;
    String sentiment = "yes";
    int maxReasons = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_sentiment);
        Intent intent = getIntent();

        // get username, password, and reasons to add to database
        Gson gson = new Gson();
        String serializedUser = getIntent().getStringExtra("user");
        user = gson.fromJson(serializedUser, User.class);
        username = user.getID();
        reasons = user.getReasons();


        for (int i = 0; i < reasons.size(); i++) {
            System.out.println(reasons.get(i));
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // check which radio button was clicked
        switch(view.getId()) {
            case R.id.yes:
                if (checked)
                    sentiment = "yes";
                break;
            case R.id.no:
                if (checked)
                    sentiment = "no";
                break;
        }

    }

    public void onContinue(View view) {


        UserDatabaseOpenHelper dbHelper = new UserDatabaseOpenHelper(view.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                UserDatabaseContract.UserDB.COL_USERNAME,
                UserDatabaseContract.UserDB.COL_REASONS_1,
                UserDatabaseContract.UserDB.COL_REASONS_2,
                UserDatabaseContract.UserDB.COL_REASONS_3,
                UserDatabaseContract.UserDB.COL_REASONS_4,
                UserDatabaseContract.UserDB.COL_REASONS_5,
                UserDatabaseContract.UserDB.COL_REASONS_6,
                UserDatabaseContract.UserDB.COL_REASONS_7,
                UserDatabaseContract.UserDB.COL_REASONS_8,
                UserDatabaseContract.UserDB.COL_REASONS_9,
                UserDatabaseContract.UserDB.COL_REASONS_10,
                UserDatabaseContract.UserDB.COL_REASONS_11,
                UserDatabaseContract.UserDB.COL_SENTIMENT

        };

        String selection = UserDatabaseContract.UserDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                UserDatabaseContract.UserDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        ContentValues values = new ContentValues();
        values.put(UserDatabaseContract.UserDB.COL_USERNAME, username);
        for (int i = 0; i < reasons.size(); i++) {
            values.put(("reasons" + i), reasons.get(i));
        }
        for (int i = reasons.size(); i < maxReasons; i++) {
            values.put(("reasons" + i), "");
        }
        values.put(UserDatabaseContract.UserDB.COL_SENTIMENT, sentiment);

        // if username does not have saved reasons, add a new row
        if (cursor.getCount() <= 0) {
            long newRowId = db.insert(UserDatabaseContract.UserDB.TABLE_NAME, null, values);
        }

        // if username does have saved reasons, update the existing row
        else {
            String selectionTwo = UserDatabaseContract.UserDB.COL_USERNAME + " LIKE ?";
            String[] selectionArgsTwo = { username };

            int count = db.update(
                    UserDatabaseContract.UserDB.TABLE_NAME,
                    values,
                    selectionTwo,
                    selectionArgsTwo);
        }

        Gson gson = new Gson();
        Intent i = new Intent(this, MainActivity.class);
        user.setReasons(reasons);
        user.setSentiment(sentiment);
        i.putExtra("user", gson.toJson(user));
        startActivity(i);
    }
}

/*
String[] projection = {
                UserDatabaseContract.UserDB.COL_USERNAME,
                UserDatabaseContract.UserDB.COL_REASONS_1,
                UserDatabaseContract.UserDB.COL_REASONS_2,
                UserDatabaseContract.UserDB.COL_REASONS_3,
                UserDatabaseContract.UserDB.COL_REASONS_4,
                UserDatabaseContract.UserDB.COL_REASONS_5,
                UserDatabaseContract.UserDB.COL_REASONS_6,
                UserDatabaseContract.UserDB.COL_REASONS_7,
                UserDatabaseContract.UserDB.COL_REASONS_8,
                UserDatabaseContract.UserDB.COL_REASONS_9,
                UserDatabaseContract.UserDB.COL_REASONS_10,
                UserDatabaseContract.UserDB.COL_REASONS_11,
                UserDatabaseContract.UserDB.COL_SENTIMENT

        };

        Cursor cursor = db.query(
                UserDatabaseContract.UserDB.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // access list of reasons for each row
        ArrayList<String> items = new ArrayList<String>();
        while(cursor.moveToNext()) {
            for (int i = 0; i < 11; i++) {
                String item = cursor.getString(cursor.getColumnIndex("reasons" + i));
                items.add(item);
            }
        }
        cursor.close();
        // items are in "items.get(i)"
 */
