package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nkeen_000 on 2/23/2017.
 */

public class AllGoalsActivity extends Activity  {
    Set<Goal> allGoals;
    User user;
    String username;
    ArrayList<String> reasons;
    String sentiment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        String serializedUser = getIntent().getStringExtra("user");
        user = gson.fromJson(serializedUser, User.class);
        username = user.getID();
        reasons = user.getReasons();
        sentiment = user.getSentiment();

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
                        i.putExtra("username", username);
                        // start the game activity
                        startActivityForResult(i, 1);
                    }
                }
            }
        });

        allGoals = new TreeSet<>();
        GoalsDatabaseOpenHelper dbGoalsHelper = new GoalsDatabaseOpenHelper(this);
        SQLiteDatabase dbGoals = dbGoalsHelper.getWritableDatabase();

        String[] projectionGoals = {
                GoalsDatabaseContract.GoalsDB.COL_USERNAME,
                GoalsDatabaseContract.GoalsDB.COL_GOALNAME,
                GoalsDatabaseContract.GoalsDB.COL_REASON,
                GoalsDatabaseContract.GoalsDB.COL_ALLDAY,
                GoalsDatabaseContract.GoalsDB.COL_STARTYEAR,
                GoalsDatabaseContract.GoalsDB.COL_STARTMONTH,
                GoalsDatabaseContract.GoalsDB.COL_STARTDAY,
                GoalsDatabaseContract.GoalsDB.COL_STARTHOUR,
                GoalsDatabaseContract.GoalsDB.COL_STARTMIN,
                GoalsDatabaseContract.GoalsDB.COL_STARTAMPM,
                GoalsDatabaseContract.GoalsDB.COL_ENDYEAR,
                GoalsDatabaseContract.GoalsDB.COL_ENDMONTH,
                GoalsDatabaseContract.GoalsDB.COL_ENDDAY,
                GoalsDatabaseContract.GoalsDB.COL_ENDHOUR,
                GoalsDatabaseContract.GoalsDB.COL_ENDMIN,
                GoalsDatabaseContract.GoalsDB.COL_ENDAMPM,
                GoalsDatabaseContract.GoalsDB.COL_REPEAT,
                GoalsDatabaseContract.GoalsDB.COL_FREQUENCY,
                GoalsDatabaseContract.GoalsDB.COL_REMINDME
        };

        String selectionGoals = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " = ?";
        String[] selectionArgsGoals = {username};

        Cursor cursorGoals = dbGoals.query(
                GoalsDatabaseContract.GoalsDB.TABLE_NAME,         // The table to query
                projectionGoals,                                     // The columns to return
                selectionGoals,                                      // The columns for the WHERE clause
                selectionArgsGoals,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        while (cursorGoals.moveToNext()) {
            //create an event using Calendar objects
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            startCal.set(Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTYEAR))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMONTH))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTDAY))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTHOUR))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMIN))));
            endCal.set(Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDYEAR))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMONTH))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDDAY))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDHOUR))),
                    Integer.parseInt(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMIN))));
            Event e = new Event(startCal, endCal);
            Goal g = new Goal(cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_GOALNAME)));
            allGoals.add(g);
        }


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