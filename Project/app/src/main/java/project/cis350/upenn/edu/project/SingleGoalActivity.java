package project.cis350.upenn.edu.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by nkeen_000 on 2/23/2017.
 */

public class SingleGoalActivity extends AppCompatActivity {
    Goal goal;
    String username;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.goal = (Goal) getIntent().getSerializableExtra("Goal");
        this.username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_single_goal);

        // Make the top TextView display the name of the goal
        final TextView goalName = (TextView) findViewById(R.id.goal_name);
        goalName.setText(goal.getName());

        // Populate the reasons TextView with all of the reasons associated with this goal
        TextView reasons = (TextView) findViewById(R.id.reason);
        reasons.setText("Reasons: " + goal.getReason());

        // Display current goal's total progress
        TextView totalProgressText = (TextView) findViewById(R.id.progress_total_text);
        totalProgressText.setText("Total Progress: " + goal.getTotalCompletionPercent());

        ProgressBar totalProgressBar = (ProgressBar) findViewById(R.id.progress_bar_total);
        totalProgressBar.setProgress((int) (goal.getTotalCompletion()*100));

        // Display current goal's monthly progress
        TextView monthProgressText = (TextView) findViewById(R.id.progress_month_text);
        monthProgressText.setText("This Month's Progress: " + goal.getMonthlyCompletionPercent(Calendar.getInstance().get(Calendar.MONTH)));

        ProgressBar monthProgressBar = (ProgressBar) findViewById(R.id.progress_bar_month);
        monthProgressBar.setProgress((int) (goal.getMonthlyCompletion(Calendar.getInstance().get(Calendar.MONTH))*100));

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
                Intent intent = new Intent(this, EditGoalActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("goalName", goal.getName());
                intent.putExtra("Goal", goal);
                startActivity(intent);
                break;
            case R.id.delete_goal_button:
                GoalsDatabaseOpenHelper dbHelper = new GoalsDatabaseOpenHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String selection = GoalsDatabaseContract.GoalsDB.COL_USERNAME + " LIKE ? AND " +
                        GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " LIKE ?";

                String[] selectionArgs = { username, goal.getName() };
                db.delete(GoalsDatabaseContract.GoalsDB.TABLE_NAME, selection, selectionArgs);

                EventsDatabaseOpenHelper dbEHelper = new EventsDatabaseOpenHelper(this);
                SQLiteDatabase dbE = dbEHelper.getWritableDatabase();
                String selectionE = EventsDatabaseContract.EventsDB.COL_USERNAME + " LIKE ? AND " +
                        EventsDatabaseContract.EventsDB.COL_GOALNAME + " LIKE ?";

                String[] selectionArgsE = { username, goal.getName() };
                dbE.delete(EventsDatabaseContract.EventsDB.TABLE_NAME, selectionE, selectionArgsE);

                AllGoalsActivity.openActivity(SingleGoalActivity.this, username);
                break;
        }
        return false;
    }

    public void onProgressClick(View v) {

    }

    // update progress bars when an event is checked on the goals page
    public void update() {
        // Make the top TextView display the name of the goal
        final TextView goalName = (TextView) findViewById(R.id.goal_name);
        goalName.setText(goal.getName());

        // Populate the reasons TextView with all of the reasons associated with this goal
        TextView reasons = (TextView) findViewById(R.id.reason);
        reasons.setText("Reasons: " + goal.getReason());

        // Display current goal's total progress
        TextView totalProgressText = (TextView) findViewById(R.id.progress_total_text);
        totalProgressText.setText("Total Progress: " + goal.getTotalCompletionPercent());

        ProgressBar totalProgressBar = (ProgressBar) findViewById(R.id.progress_bar_total);
        totalProgressBar.setProgress((int) (goal.getTotalCompletion()*100));

        // Display current goal's monthly progress
        TextView monthProgressText = (TextView) findViewById(R.id.progress_month_text);
        monthProgressText.setText("This Month's Progress: " + goal.getMonthlyCompletionPercent(Calendar.getInstance().get(Calendar.MONTH)));

        ProgressBar monthProgressBar = (ProgressBar) findViewById(R.id.progress_bar_month);
        monthProgressBar.setProgress((int) (goal.getMonthlyCompletion(Calendar.getInstance().get(Calendar.MONTH))*100));

    }
}