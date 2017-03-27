package project.cis350.upenn.edu.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkeen_000 on 2/23/2017.
 */

public class GoalActivity extends AppCompatActivity {
    Goal goal;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.goal = (Goal) getIntent().getSerializableExtra("Goal");
        setContentView(R.layout.goal_view);

        // Make the top TextView display the name of the goal
        TextView goalName = (TextView) findViewById(R.id.goal_name);
        goalName.setText(goal.getName());

        // Populate the reasons TextView with all of the reasons associated with this goal
        TextView reasons = (TextView) findViewById(R.id.reasons);
        StringBuilder s = new StringBuilder();
        for (String reason : goal.getReasons()) {
            s.append(reason);
            s.append(", ");
        }
        reasons.setText("Reasons: " + s.toString());

        // Display current goal progress
        TextView totalProgress = (TextView) findViewById(R.id.total_progress);
        totalProgress.setText("Total Progress: " + goal.getTotalCompletionPercent());

        // populate the listView from the set of events in the goal
        ListView events = (ListView) findViewById(R.id.events);
        List<String> list = new ArrayList<>();
        for (Event e : goal.getEvents()) {
            list.add(e.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.events_layouts, list);
        events.setAdapter(adapter);
    }

    // creates a menu with edit and delete options
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.goal_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem i) {
        int id = i.getItemId();
        switch (id) {
            case R.id.edit_goal_button:
                // TODO: Take to menu to edit the current goal
                return true;
            case R.id.delete_goal_button:
                // TODO: Show confirmation prompt
                return true;
        }
        return false;
    }
}
