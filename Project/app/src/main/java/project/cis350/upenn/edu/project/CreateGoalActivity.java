package project.cis350.upenn.edu.project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateGoalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static TextView startTimeText;
    private static TextView endTimeText;
    private static TextView startDateText;
    private static TextView endDateText;
    protected static boolean startTimePressed = false;
    protected static boolean endTimePressed = false;
    protected static boolean startDatePressed = false;
    protected static boolean endDatePressed = false;
    private boolean allDay = false;
    List<String> daysChecked;
    private String frequencySelection;
    private String reminderSelection;

    User user;
    String userID;
    ArrayList<String> reasons;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        Calendar c = Calendar.getInstance();

        Gson gson = new Gson();
        String serializedUser = getIntent().getStringExtra("user");
        user = gson.fromJson(serializedUser, User.class);
        userID = user.getID();
        reasons = user.getReasons();
        Toast.makeText(this, user.getName(), Toast.LENGTH_LONG).show();

        UserDatabaseOpenHelper dbHelper = new UserDatabaseOpenHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                UserDatabaseContract.UserDB.COL_USERNAME,
                UserDatabaseContract.UserDB.COL_REASONS_1,
                UserDatabaseContract.UserDB.COL_REASONS_2,
                UserDatabaseContract.UserDB.COL_REASONS_3,
                UserDatabaseContract.UserDB.COL_REASONS_4,
                UserDatabaseContract.UserDB.COL_REASONS_5,
                UserDatabaseContract.UserDB.COL_REASONS_6,
                UserDatabaseContract.UserDB.COL_REASONS_7,
                UserDatabaseContract.UserDB.COL_REASONS_8,
                UserDatabaseContract.UserDB.COL_REASONS_9,
                UserDatabaseContract.UserDB.COL_REASONS_10,
                UserDatabaseContract.UserDB.COL_REASONS_11,
                UserDatabaseContract.UserDB.COL_SENTIMENT

        };

        // Filter results WHERE COL_USERNAME = username
        String selection = UserDatabaseContract.UserDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { userID };

        Cursor cursor = db.query(
                UserDatabaseContract.UserDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        reasons = new ArrayList<String>();
        // access list of reasons for each row
        while(cursor.moveToNext()) {
            for (int i = 0; i < 11; i++) {
                String item = cursor.getString(cursor.getColumnIndex("reasons" + i));
                if (!item.isEmpty()) {
                    reasons.add(item);
                }
            }
        }
        cursor.close();
        // reasons are saved in reason
        // reason can now populate a spinner

        //setting labels at start up for TIME
        String currTime = hourConverter(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        String currEndTime = hourConverter(c.get(Calendar.HOUR_OF_DAY) + 1, c.get(Calendar.MINUTE));
        startTimeText = (TextView) findViewById(R.id.setStartTime);
        startTimeText.setText(currTime);
        endTimeText = (TextView) findViewById(R.id.setEndTime);
        endTimeText.setText(currEndTime);

        //setting labels at start up for DATE
        c.add(Calendar.DATE, 0);
        SimpleDateFormat oldFormat = new SimpleDateFormat("MM-dd-yyyy");
        String currDate = oldFormat.format(c.getTime());
        startDateText = (TextView) findViewById(R.id.setStartDate);
        startDateText.setText(currDate);
        endDateText = (TextView) findViewById(R.id.setEndDate);
        endDateText.setText(currDate);

        //creating reminder spinner
        Spinner reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        ArrayAdapter<CharSequence> adapterOne = ArrayAdapter.createFromResource(this,
                R.array.reminder_options, android.R.layout.simple_spinner_item);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(adapterOne);
        reminderSpinner.setOnItemSelectedListener(this);

        //creating frequency spinner
        Spinner frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
        ArrayAdapter<CharSequence> adapterTwo = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapterTwo);
        frequencySpinner.setOnItemSelectedListener(this);

        //creating reasons spinner
        Spinner reasonsSpinner = (Spinner) findViewById(R.id.reasonspinner);
        ArrayAdapter<String> adapterThree = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, reasons);
        adapterThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonsSpinner.setAdapter(adapterThree);

        //GOAL NAME
        final EditText goalInput = (EditText) findViewById(R.id.goalNameInput);


        //TODO: NEED TO GATHER ALL INFO FOR GOAL OBJECT HERE
        //should also switch intent here
        final Button addGoal = (Button) findViewById(R.id.addGoalButton);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                PopupMenu popup = new PopupMenu(CreateGoalActivity.this, addGoal);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.addAnother) {
                            Intent i = new Intent(view.getContext(), GoalActivity.class);
                            Gson gson = new Gson();
                            i.putExtra("user", gson.toJson(user));
                            startActivity(i);
                        } else if (itemId == R.id.mainMenu) {
                            Intent i = new Intent(view.getContext(), MainActivity.class);
                            Gson gson = new Gson();
                            i.putExtra("user", gson.toJson(user));
                            startActivity(i);
                        }
                        return true;
                    }
                });

                String goalName = goalInput.getText().toString();

            }
        });

        daysChecked = new ArrayList<String>();

    }

    public void allDayCheckBox(View v) {
        allDay = ((CheckBox) v).isChecked();
        System.out.println(allDay);
    }

    public void dayCheckBoxes(View v) {
        int value = v.getId();
        if (((CheckBox) v).isChecked()) {
            if (value == R.id.sundayBox) {
                daysChecked.add("Sunday");
            } else if (value == R.id.mondayBox) {
                daysChecked.add("Monday");
            } else if (value == R.id.tuesdayBox) {
                daysChecked.add("Tuesday");
            } else if (value == R.id.wednesdayBox) {
                daysChecked.add("Wednesday");
            } else if (value == R.id.thursdayBox) {
                daysChecked.add("Thursday");
            } else if (value == R.id.fridayBox) {
                daysChecked.add("Friday");
            } else if (value == R.id.saturdayBox) {
                daysChecked.add("Saturday");
            }
        } else {
            if (value == R.id.sundayBox) {
                daysChecked.remove("Sunday");
            } else if (value == R.id.mondayBox) {
                daysChecked.remove("Monday");
            } else if (value == R.id.tuesdayBox) {
                daysChecked.remove("Tuesday");
            } else if (value == R.id.wednesdayBox) {
                daysChecked.remove("Wednesday");
            } else if (value == R.id.thursdayBox) {
                daysChecked.remove("Thursday");
            } else if (value == R.id.fridayBox) {
                daysChecked.remove("Friday");
            } else if (value == R.id.saturdayBox) {
                daysChecked.remove("Saturday");
            }
        }
        System.out.println(daysChecked);
    }

    private static String hourConverter(int hour, int minute) {
        String hourString = hour + "";
        String minuteString = minute + "";
        String amOrPm = "AM";
        if (hour == 0) {
            hourString = "12";
        } else if (hour >= 13) {
            hour -= 12;
            hourString = hour + "";
            amOrPm = "PM";
        }
        if (minute < 10) {
            minuteString = "0"+ minute;
        }
        return hourString + ":" + minuteString + amOrPm;
    }

    private static String dateConverter(int day, int month, int year) {
        String monthString = month + "";
        String dayString = day + "";
        if (month < 10) {
            monthString = "0" + month;
        }
        if (day < 10) {
            dayString = "0" + day;
        }

        return monthString + "-" + dayString + "-" + year;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner;
        if (parent.getId() == R.id.frequencySpinner) {
            spinner = (Spinner) findViewById(R.id.frequencySpinner);
            frequencySelection = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.reminderSpinner) {
            spinner = (Spinner) findViewById(R.id.reminderSpinner);
            reminderSelection = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onSetStartTimeClick(View v) {
        DialogFragment startTimePicker = new TimePickerFragment();
        startTimePicker.show(getSupportFragmentManager(), "startTimePicker");
        startTimePressed = true;
    }

    public void onSetEndTimeClick(View v) {
        DialogFragment endTimePicker = new TimePickerFragment();
        endTimePicker.show(getSupportFragmentManager(), "endTimePicker");
        endTimePressed = true;
    }

    public void onSetStartDateClick(View v) {
        DialogFragment startDatePicker = new DatePickerFragment();
        startDatePicker.show(getSupportFragmentManager(), "startDatePicker");
        startDatePressed = true;
    }

    public void onSetEndDateClick(View v) {
        DialogFragment endDatePicker = new DatePickerFragment();
        endDatePicker.show(getSupportFragmentManager(), "endDatePicker");
        endDatePressed = true;
    }



    public static void setText(String selection) {
        if (startTimePressed) {
            startTimeText.setText(selection);
            startTimePressed = false;
        } else if (endTimePressed) {
            endTimeText.setText(selection);
            endTimePressed = false;
        } else if (startDatePressed) {
            startDateText.setText(selection);
            startDatePressed = false;
        } else if (endDatePressed) {
            endDateText.setText(selection);
            endDatePressed = false;
        }
    }


    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar currDate = Calendar.getInstance();
            int hour = currDate.get(Calendar.HOUR_OF_DAY);
            int minute = currDate.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setText(hourConverter(hourOfDay, minute));
        }

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setText(dateConverter(dayOfMonth, month+1, year));
        }
    }
}




