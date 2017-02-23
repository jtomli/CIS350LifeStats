package edu.upenn.cis350.lifestats;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.HashSet;

import static com.mongodb.client.model.Filters.eq;

public class SetupActivityReasons extends AppCompatActivity {

    // layout elements
    LinearLayout layoutCheckBox;
    ArrayList<CheckBox> userGeneratedReasons;
    ArrayList<RelativeLayout> userGeneratedReasonsLayouts;
    ArrayList<EditText> userGeneratedReasonsTextBox;

    final int idForAdditionalCheckboxLayout = 1;

    // database elements
    String username;
    String password;
    ArrayList<String> reasons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_reasons);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");

        layoutCheckBox = (LinearLayout) findViewById(R.id.otherCheckBoxLayout);
        userGeneratedReasons = new ArrayList<CheckBox>();
        userGeneratedReasonsTextBox = new ArrayList<EditText>();
        userGeneratedReasonsLayouts = new ArrayList<RelativeLayout>();
        reasons = new ArrayList<String>();
    }

    // go to next step of setup
    public void onContinue(View view) {

        Object[] temp = reasons.toArray();
        String reasonsTemp = "";
        if (temp.length > 0) {
            reasonsTemp = (String) temp[0];
        }
        for (int i = 1; i < temp.length; i++) {
            reasonsTemp = reasonsTemp + ", " + temp[i];
        }

        for (int i = 0; i < userGeneratedReasonsTextBox.size() - 1; i++) {
            reasonsTemp = reasonsTemp + ", " +
                    userGeneratedReasonsTextBox.get(i).getText().toString();
        }

        Intent intent = new Intent(this, SetupActivitySentiment.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("reasons", reasonsTemp);
        startActivity(intent);
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
        }
    }

    public void onSelectOther(View view) {

        boolean checked = ((Checkable) view).isChecked();

        // user is checking "Other"
        if (checked) {

            // display new "other" checkbox
            layoutCheckBox.addView(createNewRelativeLayout());
            int layout = userGeneratedReasonsLayouts.size() - 1;
            userGeneratedReasonsLayouts.get(layout).addView(createNewCheckBox());
            userGeneratedReasonsLayouts.get(layout).addView(createNewTextBox());

        }

        // user is un-checking "Other"
        else {
            deleteLastCheckBox();
        }
    }

    private RelativeLayout createNewRelativeLayout() {
        RelativeLayout other = new RelativeLayout(this);
        userGeneratedReasonsLayouts.add(other);
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
        userGeneratedReasons.add(other);
        return other;
    }

    private EditText createNewTextBox() {

        EditText other = new EditText(this);
        int last = userGeneratedReasons.size() - 1;

        // set LayoutParams
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, idForAdditionalCheckboxLayout);
        params.addRule(RelativeLayout.ALIGN_BASELINE, idForAdditionalCheckboxLayout);
        other.setLayoutParams(params);

        other.setTextSize(14);
        other.setText("enter your reason...");
        userGeneratedReasonsTextBox.add(other);
        return other;
    }

    private void deleteLastCheckBox() {
        if (!userGeneratedReasons.isEmpty()) {
            int last = userGeneratedReasons.size() - 1;

            // remove from view
            userGeneratedReasons.get(last).setVisibility(View.GONE);
            userGeneratedReasonsLayouts.get(last).setVisibility(View.GONE);
            userGeneratedReasonsTextBox.get(last).setVisibility(View.GONE);

            // remove from records
            userGeneratedReasons.remove(last);
            userGeneratedReasonsLayouts.remove(last);
            userGeneratedReasonsTextBox.remove(last);
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