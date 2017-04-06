package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

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
    String username;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        username = i.getExtras().getString("username");

        setContentView(R.layout.all_goals_layout);
        // create a ListView to display all the user's goals into a ListView
        final ListView goals = (ListView) findViewById(R.id.all_goals);
        goals.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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

        String selectionGoals = GoalsDatabaseContract.GoalsDB.COL_USERNAME + " = ?";
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
            String goalN = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_GOALNAME));
            Goal g = new Goal(goalN);
            String reason = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REASON));
            g.setReason(reason);
            allGoals.add(g);

            for (Goal goal : allGoals) {
                String name = goal.getName();
                EventsDatabaseOpenHelper dbEventsHelper = new EventsDatabaseOpenHelper(this);
                SQLiteDatabase dbEvents = dbEventsHelper.getWritableDatabase();

                String[] projectionEvents = {
                        EventsDatabaseContract.EventsDB.COL_USERNAME,
                        EventsDatabaseContract.EventsDB.COL_GOALNAME,
                        EventsDatabaseContract.EventsDB.COL_YEAR,
                        EventsDatabaseContract.EventsDB.COL_MONTH,
                        EventsDatabaseContract.EventsDB.COL_DAY,
                        EventsDatabaseContract.EventsDB.COL_LOG
                };

                String selectionEvents = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " = ?";
                String[] selectionArgsEvents = {name};

                Cursor cursorEvents = dbEvents.query(
                        EventsDatabaseContract.EventsDB.TABLE_NAME,         // The table to query
                        projectionEvents,                                     // The columns to return
                        selectionEvents,                                      // The columns for the WHERE clause
                        selectionArgsEvents,                                  // The values for the WHERE clause
                        null,                                           // don't group the rows
                        null,                                           // don't filter by row groups
                        null                                            // The sort order
                );

                while (cursorEvents.moveToNext()) {
                    int year = cursorEvents.getInt(cursorEvents.getColumnIndex(EventsDatabaseContract.EventsDB.COL_YEAR));
                    int month = cursorEvents.getInt(cursorEvents.getColumnIndex(EventsDatabaseContract.EventsDB.COL_MONTH));
                    int day = cursorEvents.getInt(cursorEvents.getColumnIndex(EventsDatabaseContract.EventsDB.COL_DAY));

                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, day);

                    Event event = new Event(cal, cal);

                    String completed = cursorEvents.getString(cursorEvents.getColumnIndex(EventsDatabaseContract.EventsDB.COL_LOG));
                    if (completed.equals("yes")) {
                        event.markCompleted(true);
                    }

                    goal.addEvent(event);
                }
            }
        }


        // populate the ListView with all of the user's goals
        List<Goal> list = new ArrayList<>();
        for (Goal goal : allGoals) {
            list.add(goal);
        }
        CustomAdapter adapter = new CustomAdapter(this, list);
        goals.setAdapter(adapter);
    }

    public void goToSingleGoal(Goal goal) {
        // create a new Intent using the current activity and GoalActivity class
        Intent i = new Intent(getApplicationContext(), SingleGoalActivity.class);
        // pass the goal to GoalActivity
        i.putExtra("Goal", goal);
        i.putExtra("username", username);
        // start the game activity
        startActivityForResult(i, 1);
    }
}