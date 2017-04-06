package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SetupActivityReasons extends AppCompatActivity {

    // layout elements
    LinearLayout layoutCheckBox; // stores programmatically added "Other" checkboxes
    ArrayList<CheckBox> ugrCheckbox; // stores Checkbox for user generated reasons
    ArrayList<RelativeLayout> ugrLayout; // stores Layout for user generated reasons
    ArrayList<EditText> ugrText; // stores TextEdit for user generated reasons

    final int idForAdditionalCheckboxLayout = 1;

    // database elements
    String username;
    ArrayList<String> reasons;
    int maxReasons = 11; // the maximum number of reasons that can be selected
    int maxOther = 8; // the maximum number of user-generated reasons that can be added


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // start activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_reasons);

        // get username and password
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        layoutCheckBox = (LinearLayout) findViewById(R.id.otherCheckBoxLayout);
        ugrCheckbox = new ArrayList<CheckBox>(maxOther);
        ugrText = new ArrayList<EditText>(maxOther);
        ugrLayout = new ArrayList<RelativeLayout>(maxOther);
        reasons = new ArrayList<String>(maxReasons);

        UserDatabaseOpenHelper dbHelper = new UserDatabaseOpenHelper(this);
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

        Intent i;
        if (cursor.getCount() > 0 && !getIntent().hasExtra("fromSetupButton")) {

            i = new Intent(this, MainActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        } else if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                for (int j = 0; j < 11; j++) {
                    String reason = cursor.getString(cursor.getColumnIndex("reasons" + j));
                    if (reason.equals("improve health")) {
                        CheckBox x = (CheckBox) findViewById(R.id.health);
                        x.setChecked(true);
                        reasons.add("improve health");
                    } else if (reason.equals("reduce stress")) {
                        CheckBox x = (CheckBox) findViewById(R.id.stress);
                        x.setChecked(true);
                        reasons.add("reduce stress");
                    } else if (reason.equals("improve grades")) {
                        CheckBox x = (CheckBox) findViewById(R.id.grades);
                        x.setChecked(true);
                        reasons.add("improve grades");
                    } else if (reason != null && !reason.equals("")){
                        CheckBox other = (CheckBox) findViewById(R.id.other);
                        if (!other.isChecked()) {
                            other.setChecked(true);
                            EditText otherText = (EditText) findViewById(R.id.otherText);
                            otherText.setText(reason);
                        } else {
                            int layout = ugrLayout.size();
                            layoutCheckBox.addView(createNewRelativeLayout());
                            CheckBox x = createNewCheckBox();
                            x.setChecked(true);
                            ugrLayout.get(layout).addView(x);
                            EditText y = createNewTextBox();
                            y.setText(reason);
                            ugrLayout.get(layout).addView(y);
                        }
                    }
                }
                CheckBox other = (CheckBox) findViewById(R.id.other);
                if (other.isChecked() && ugrCheckbox.isEmpty()) {
                    int layout = ugrLayout.size();
                    layoutCheckBox.addView(createNewRelativeLayout());
                    ugrLayout.get(layout).addView(createNewCheckBox());
                    ugrLayout.get(layout).addView(createNewTextBox());
                } else if (!ugrCheckbox.isEmpty() && ugrCheckbox.size() < maxOther) {
                    int layout = ugrLayout.size();
                    layoutCheckBox.addView(createNewRelativeLayout());
                    ugrLayout.get(layout).addView(createNewCheckBox());
                    ugrLayout.get(layout).addView(createNewTextBox());
                }
            }
        }
    }


    // go to next step of setup
    public void onContinue(View view) {

        Checkable other = (Checkable) findViewById(R.id.other);
        if (other.isChecked()) {

            EditText otherText = (EditText) findViewById(R.id.otherText);
            reasons.add(otherText.getText().toString());

        }

        for (int i = 0; i < ugrText.size(); i++) {
            String newReason = ugrText.get(i).getText().toString();
            if (ugrCheckbox.get(i).isChecked()) {
                if (!reasons.contains(newReason)) {
                    reasons.add(newReason);
                }
            }
        }

        if (!reasons.isEmpty()) {
            Intent intent = new Intent(this, SetupActivitySentiment.class);
            intent.putExtra("username", username);
            intent.putExtra("reasons", reasons);
            if (getIntent().hasExtra("fromSetupButton")) {
                intent.putExtra("fromSetupButton", "true");
            }
            startActivity(intent);
        } else {
            Toast.makeText(this, "Select at least one reason.", Toast.LENGTH_LONG).show();
        }
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((Checkable) view).isChecked();

        // get which checkbox is clicked
        switch(view.getId()) {
            case R.id.health:
                if (checked) {
                    // add "health" to "reasons" in database
                    reasons.add("improve health");
                } else {
                    reasons.remove("improve health");
                }
                break;
            case R.id.stress:
                if (checked) {
                    reasons.add("reduce stress");
                } else {
                    reasons.remove("reduce stress");
                }
                break;
            case R.id.grades:
                if (checked) {
                    reasons.add("improve grades");
                } else {
                    reasons.remove("improve grades");
                }
                break;
            case R.id.other:
                onSelectOther(view);
        }
    }

    public void onSelectOther(View view) {

        boolean checked = ((Checkable) view).isChecked();

        // user is checking "Other"
        if (checked) {
            if (ugrCheckbox.size() < maxOther) {
                // display new "other" checkbox
                int layout = ugrLayout.size();
                layoutCheckBox.addView(createNewRelativeLayout());
                ugrLayout.get(layout).addView(createNewCheckBox());
                ugrLayout.get(layout).addView(createNewTextBox());
            }
        }

        // user is un-checking "Other"
        else {
            deleteLastCheckBox();
        }
    }

    private RelativeLayout createNewRelativeLayout() {
        RelativeLayout other = new RelativeLayout(this);
        ugrLayout.add(other);
        return other;
    }

    private CheckBox createNewCheckBox() {

        // create checkbox
        CheckBox other = new CheckBox(this);

        // set LayoutParams
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        other.setLayoutParams(params);

        other.setText("Other:");
        other.setOnClickListener(onClick());
        other.setId(idForAdditionalCheckboxLayout);
        ugrCheckbox.add(other);
        return other;
    }

    private EditText createNewTextBox() {

        EditText other = new EditText(this);
        int last = ugrCheckbox.size() - 1;

        // set LayoutParams
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, idForAdditionalCheckboxLayout);
        params.addRule(RelativeLayout.ALIGN_BASELINE, idForAdditionalCheckboxLayout);
        other.setLayoutParams(params);

        other.setTextSize(14);
        ugrText.add(other);
        return other;
    }

    private void deleteLastCheckBox() {
        if (!ugrCheckbox.isEmpty() && !ugrLayout.isEmpty() && !ugrText.isEmpty()) {
            int last = ugrCheckbox.size() - 1;

            // remove from view
            ugrCheckbox.get(last).setVisibility(View.GONE);
            ugrLayout.get(last).setVisibility(View.GONE);
            ugrText.get(last).setVisibility(View.GONE);

            // remove from records
            ugrCheckbox.remove(last);
            ugrLayout.remove(last);
            ugrText.remove(last);
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                onSelectOther(view);
            }
        };
    }
}