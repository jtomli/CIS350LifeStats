package project.cis350.upenn.edu.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Settings: allows a user to enter whether they would like to use the app's Diary feature
 * Saves a user's Diary preferences and reasons for using the app to the device
 */

public class SetupActivitySentiment extends AppCompatActivity {

    String username;
    ArrayList<String> reasons;
    String sentiment = "yes";
    int maxReasons = 11;

    /**
     * Starts the Activity from SetupActivityReasons
     * @param savedInstanceState information from SetupActivityReasons including the user's
     *                           unique username and the "reasons for using the app" they
     *                           entered on the previous screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_sentiment);
        Intent intent = getIntent();

        // get username, password, and reasons to add to database
        username = intent.getExtras().getString("username");
        reasons = intent.getStringArrayListExtra("reasons");
    }

    /**
     * Sets sentiment to "yes" if user wants to use Diary function and to "no" if user wants to use
     * app for goals only
     *
     * @param view
     */
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

    /**
     * Adds a user's settings (reasons for using the app and whether they want to use the Diary
     * functionality) to the UserDB if a user does not have existing settings
     * Updates a user's settings if they do have existing settings
     *
     * @param view
     */
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

        cursor.close();
        db.close();
        dbHelper.close();

        if (getIntent().hasExtra("fromSetupButton")) {
            AllGoalsActivity.openActivity(SetupActivitySentiment.this, username);
        } else {
            CreateGoalActivity.openActivity(SetupActivitySentiment.this, username);
        }
    }
}