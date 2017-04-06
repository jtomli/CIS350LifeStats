package project.cis350.upenn.edu.project;

import android.content.Intent;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        username = i.getExtras().getString("username");
        goalname = i.getExtras().getString("goalname");

        setContentView(R.layout.log_progress);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //TODO get from events database
        //get start calendar to get these values, not worrying about end since irrelevant for only
        //this indidivual event
        EventsDatabaseOpenHelper dbEHelper = new EventsDatabaseOpenHelper(this);
        SQLiteDatabase dbE = dbEHelper.getWritableDatabase();
        String selectionE = EventsDatabaseContract.EventsDB.COL_USERNAME + " LIKE ? AND " +
                EventsDatabaseContract.EventsDB.COL_GOALNAME + " LIKE ?";

        String[] selectionArgsE = { username, goalname };


        setTime = (TextView) findViewById(R.id.setTime);
        //setTime.setText();
        setDate = (TextView) findViewById(R.id.setDate);
        //setDate.setTest();

        final Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //TODO need to update current event in the database to include completed

                PopupMenu popup = new PopupMenu(ProgressActivity.this, save);
                popup.getMenuInflater().inflate(R.menu.add_goal_popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.returnCal) {
                            Intent i = new Intent(v.getContext(), CalendarActivity.class);
                            i.putExtra("username", username);
                            startActivity(i);
                        } else if (itemId == R.id.mainMenu) {
                            Intent i = new Intent(v.getContext(), MainActivity.class);
                            i.putExtra("username", username);
                            startActivity(i);
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
