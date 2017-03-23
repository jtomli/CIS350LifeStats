package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nkeen_000 on 2/23/2017.
 */

public class AllGoalsActivity extends Activity  {
    Set<Goal> allGoals;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_goals_layout);
        // create a ListView to display all the user's goals into a ListView
        final ListView goals = (ListView) findViewById(R.id.all_goals);
        goals.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // add a listener to the ListView so that specific goals can be selected
        goals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
                String selectedFromList =(goals.getItemAtPosition(pos).toString());
                for (Goal goal : allGoals) {
                    if (goal.toString().equals(selectedFromList)) {
                        // create a new Intent using the current activity and GoalActivity class
                        Intent i = new Intent(getApplicationContext(), SingleGoalActivity.class);
                        // pass the goal to GoalAtivity
                        i.putExtra("Goal", goal);
                        // start the game activity
                        startActivityForResult(i, 1);
                    }
                }
            }
        });

        allGoals = new TreeSet<>();
        // TODO: Retrieve set of Goals from database
        Goal test1 = new Goal("Test Goal 1");
        test1.addReason("Get a good grade on this assignment");
        test1.addEvent(new Event(2017, 2, 24, 11, 30, 12, 30, false));
        test1.addEvent(new Event(2017, 2, 25, 11, 30, 12, 30, false));
        test1.addEvent(new Event(2017, 2, 26, 11, 30, 12, 30, false));
        test1.addEvent(new Event(2017, 2, 27, 11, 30, 12, 30, false));
        Event eCompl1 = new Event(2017, 2, 28, 11, 30, 12, 30, false);
        eCompl1.markCompleted(true);
        test1.addEvent(eCompl1);
        Goal test2 = new Goal("Test Goal 2");
        test2.addReason("Test my code");
        test2.addEvent(new Event(2017, 2, 24, 11, 30, 12, 30, false));
        test2.addEvent(new Event(2017, 2, 25, 11, 30, 12, 30, false));
        test2.addEvent(new Event(2017, 2, 26, 11, 30, 12, 30, false));
        test2.addEvent(eCompl1);
        Event eCompl2 = new Event(2017, 2, 29, 11, 30, 12, 30, false);
        eCompl2.markCompleted(true);
        test2.addEvent(eCompl1);
        Goal test3 = new Goal("Test Goal 3");
        test3.addReason("Is there a reason for anything? Does life have any meaning? Why are we here?");
        test3.addEvent(new Event(2017, 2, 24, 11, 30, 12, 30, false));
        test3.addEvent(new Event(2017, 2, 25, 11, 30, 12, 30, false));
        test3.addEvent(eCompl1);
        test3.addEvent(eCompl2);
        Event eCompl3 = new Event(2017, 2, 30, 11, 30, 12, 30, false);
        eCompl3.markCompleted(true);
        test3.addEvent(eCompl1);
        Goal test4 = new Goal("Test Goal 4");
        test4.addReason("42");
        test4.addEvent(new Event(2017, 2, 24, 11, 30, 12, 30, false));
        test4.addEvent(new Event(2017, 2, 25, 11, 30, 12, 30, false));
        test4.addEvent(eCompl1);
        test4.addEvent(eCompl2);
        Event eCompl4 = new Event(2017, 2, 31, 11, 30, 12, 30, false);
        eCompl3.markCompleted(true);
        test4.addEvent(eCompl1);

        allGoals.add(test1);
        allGoals.add(test2);
        allGoals.add(test3);
        allGoals.add(test4);

        // populate the ListView with all of the user's goals
        List<String> list = new ArrayList<>();
        for (Goal goal : allGoals) {
            list.add(goal.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.goals_layout, list);
        goals.setAdapter(adapter);
    }
}