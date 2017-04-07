package project.cis350.upenn.edu.project;

import project.cis350.upenn.edu.project.CustomAdapter;
import project.cis350.upenn.edu.project.EventsDatabaseContract;
import project.cis350.upenn.edu.project.EventsDatabaseOpenHelper;
import project.cis350.upenn.edu.project.Goal;
import project.cis350.upenn.edu.project.GoalsDatabaseContract;
import project.cis350.upenn.edu.project.GoalsDatabaseOpenHelper;
import project.cis350.upenn.edu.project.R;
import project.cis350.upenn.edu.project.SideMenuActivity;
import project.cis350.upenn.edu.project.SingleGoalActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nkeen_000 on 2/23/2017.
 */

public class AllGoalsActivity extends SideMenuActivity  {
    Set<Goal> allGoals;
    String username;

    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, AllGoalsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.all_goals_layout);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        username = i.getExtras().getString("username");

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
            String startH = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTHOUR));
            String endH = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDHOUR));
            String startM = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMIN));
            String endM = cursorGoals.getString(cursorGoals.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMIN));

            int startHour = Integer.parseInt(startH);
            int startMin = Integer.parseInt(startM);
            int endHour = Integer.parseInt(endH);
            int endMin = Integer.parseInt(endM);

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
                    cal.set(year, month, day, startHour, startMin);
                    Calendar end = Calendar.getInstance();
                    cal.set(year, month, day, endHour, endMin);

                    Event event = new Event(cal, end);

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