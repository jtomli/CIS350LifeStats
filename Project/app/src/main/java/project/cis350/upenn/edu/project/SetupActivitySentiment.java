package project.cis350.upenn.edu.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SetupActivitySentiment extends AppCompatActivity {

    String username;
    String password;
    String reasons;
    String sentiment = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_sentiment);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        reasons = intent.getExtras().getString("reasons");
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // check which radio button was clicked
        switch(view.getId()) {
            case R.id.yes:
                if (checked)
                    sentiment = "yes";
                    break;
            case R.id.no:                        if (itemId == R.id.addAnother) {

                if (checked)
                    sentiment = "no";
                    break;
        }

    }

    public void onContinue(View view) {

        UserDatabaseOpenHelper dbHelper = new UserDatabaseOpenHelper(view.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDatabaseContract.UserDB.COL_USERNAME, username);
        values.put(UserDatabaseContract.UserDB.COL_PASSWORD, password);
        values.put(UserDatabaseContract.UserDB.COL_REASONS, reasons);
        values.put(UserDatabaseContract.UserDB.COL_SENTIMENT, sentiment);

        // insert new row, returning primary key value of new row
        long newRowId = db.insert(UserDatabaseContract.UserDB.TABLE_NAME, null, values);

        Intent i = new Intent(this, GoalActivity.class);
        startActivity(i);
    }
}
