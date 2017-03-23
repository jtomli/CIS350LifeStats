package project.cis350.upenn.edu.project;

import android.content.Intent;
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
        if (!ugrCheckbox.isEmpty()) {
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