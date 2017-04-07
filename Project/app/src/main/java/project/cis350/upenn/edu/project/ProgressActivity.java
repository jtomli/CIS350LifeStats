package project.cis350.upenn.edu.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by jamietomlinson on 4/4/17.
 */

public class ProgressActivity extends AppCompatActivity {

    private String username;
    private String goalname;
    private TextView setTime;
    private TextView setDate;
    private boolean completed;
    private Event currEvent;

    int year;
    int month;
    int day;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        username = i.getExtras().getString("username");
        goalname = i.getExtras().getString("goalName");

        setContentView(R.layout.log_progress);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //TODO get from events database
        //get start calendar to get these values, not worrying about end since irrelevant for only
        //this indidivual event


        //setTime = (TextView) findViewById(R.id.setTime);
        //setTime.setText();
        setDate = (TextView) findViewById(R.id.setDate);
        setDate.setText(day + "/" + (month + 1) + "/" + year);
        TextView goalNAME = (TextView) findViewById(R.id.goalName);
        goalNAME.setText(goalname);

        final Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (completed) {
                    EventsDatabaseOpenHelper dbEHelper = new EventsDatabaseOpenHelper(v.getContext());
                    SQLiteDatabase dbE = dbEHelper.getWritableDatabase();
                    String selectionE = EventsDatabaseContract.EventsDB.COL_USERNAME + " LIKE ? AND " +
                            EventsDatabaseContract.EventsDB.COL_GOALNAME + " LIKE ? AND " +
                            EventsDatabaseContract.EventsDB.COL_YEAR + " LIKE ? AND " +
                            EventsDatabaseContract.EventsDB.COL_MONTH + " LIKE ? AND " +
                            EventsDatabaseContract.EventsDB.COL_DAY + " LIKE ?";

                    String[] selectionArgsE = {username, goalname, year + "", month + "", day + ""};
                    ContentValues values = new ContentValues();
                    values.put(EventsDatabaseContract.EventsDB.COL_USERNAME, username);
                    values.put(EventsDatabaseContract.EventsDB.COL_GOALNAME, goalname);
                    values.put(EventsDatabaseContract.EventsDB.COL_YEAR, year);
                    values.put(EventsDatabaseContract.EventsDB.COL_MONTH, month);
                    values.put(EventsDatabaseContract.EventsDB.COL_DAY, day);
                    values.put(EventsDatabaseContract.EventsDB.COL_LOG, "yes");
                    int count = dbE.update(
                            EventsDatabaseContract.EventsDB.TABLE_NAME,
                            values,
                            selectionE,
                            selectionArgsE);
                }

                PopupMenu popup = new PopupMenu(ProgressActivity.this, save);
                popup.getMenuInflater().inflate(R.menu.add_goal_popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.addAnother) {
                            CreateGoalActivity.openActivity(ProgressActivity.this, username);
                        } else if (itemId == R.id.mainMenu) {
                            AllGoalsActivity.openActivity(ProgressActivity.this, username);
                        }
                        return true;
                    }
                });
            }
        });


    }

    public void onCompletedClick(View v) {
        completed = ((CheckBox) v).isChecked();
    }

}
