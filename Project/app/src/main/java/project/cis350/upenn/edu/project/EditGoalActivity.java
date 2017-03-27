package project.cis350.upenn.edu.project;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditGoalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    //TODO info to be sent to DB!!!!!
    private String goalName;
    private String reasonSelection;
    private static String startMonth;
    private static String startDay;
    private static String startYear;
    private static String endMonth;
    private static String endDay;
    private static String endYear;
    private static String startHour;
    private static String startMin;
    private static String startAmPm;
    private static String endHour;
    private static String endMin;
    private static String endAmPm;

    String username;
    ArrayList<String> reasons;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);


        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        goalName = intent.getExtras().getString("goalName");

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
        String[] selectionArgs = {username};

        Cursor cursorUser = db.query(
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
        while (cursorUser.moveToNext()) {
            for (int i = 0; i < 11; i++) {
                String item = cursorUser.getString(cursorUser.getColumnIndex("reasons" + i));
                if (!item.isEmpty()) {
                    reasons.add(item);
                }
            }
        }
        cursorUser.close();

        // reasons are saved in reason
        // reason can now populate a spinner


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

        daysChecked = new ArrayList<String>();

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
        String[] selectionArgsGoals = {goalName};

        Cursor cursor = dbGoals.query( //TODO getting error here "GOALS_TABLE does not exist"
                GoalsDatabaseContract.GoalsDB.TABLE_NAME,         // The table to query
                projectionGoals,                                     // The columns to return
                selectionGoals,                                      // The columns for the WHERE clause
                selectionArgsGoals,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        //setting all info on EditGoal screen to match the previous goals info
        while (cursor.moveToNext()) {
            startTimeText = (TextView) findViewById(R.id.setStartTime);
            startTimeText.setText(timePrinter(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTHOUR)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMIN)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTAMPM))));
            endTimeText = (TextView) findViewById(R.id.setEndTime);
            endTimeText.setText(timePrinter(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDHOUR)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMIN)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDAMPM))));

            //setting labels at start up for DATE
            startDateText = (TextView) findViewById(R.id.setStartDate);
            startDateText.setText(datePrinter(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMONTH)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTDAY)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTYEAR))));
            endDateText = (TextView) findViewById(R.id.setEndDate);
            endDateText.setText(datePrinter(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMONTH)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDDAY)),
                    cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDYEAR))));

            reminderSpinner.setPrompt(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REMINDME)));
            frequencySpinner.setPrompt(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_FREQUENCY)));
            reasonsSpinner.setPrompt(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REASON)));
            goalInput.setText(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_GOALNAME)));

            checkDayBoxes(cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REPEAT)));

            startYear = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTYEAR));
            startMonth = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMONTH));
            startDay = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTDAY));
            startHour = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTHOUR));
            startMin = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTMIN));
            startAmPm = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_STARTAMPM));
            endYear = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDYEAR));
            endMonth = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMONTH));
            endDay = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDDAY));
            endHour = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDHOUR));
            endMin = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDMIN));
            endAmPm = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_ENDAMPM));
            reasonSelection = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REASON));
            frequencySelection = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_FREQUENCY));
            reminderSelection = cursor.getString(cursor.getColumnIndex(GoalsDatabaseContract.GoalsDB.COL_REMINDME));
        }



        //should also switch intent here
        final Button updateGoal = (Button) findViewById(R.id.addGoalButton);
        updateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                goalName = goalInput.getText().toString();
                updateGoal(v);
                PopupMenu popup = new PopupMenu(EditGoalActivity.this, updateGoal);
                popup.getMenuInflater().inflate(R.menu.update_goal_popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.allGoalsView) {
                            Intent i = new Intent(v.getContext(), AllGoalsActivity.class);
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




    //TODO 3/24 add to database
    public void updateGoal(View v) {
        GoalsDatabaseOpenHelper dbHelper = new GoalsDatabaseOpenHelper(v.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
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

        String selection = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " = ?";
        String[] selectionArgs = { goalName };

        Cursor cursor = db.query(
                GoalsDatabaseContract.GoalsDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        ContentValues values = new ContentValues();
        values.put(GoalsDatabaseContract.GoalsDB.COL_USERNAME, username);
        values.put(GoalsDatabaseContract.GoalsDB.COL_GOALNAME, goalName);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTYEAR, startYear);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTMONTH, startMonth);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTDAY, startDay);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTHOUR, startHour);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTMIN, startMin);
        values.put(GoalsDatabaseContract.GoalsDB.COL_STARTAMPM, startAmPm);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDYEAR, endYear);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDMONTH, endMonth);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDDAY, endDay);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDHOUR, endHour);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDMIN, endMin);
        values.put(GoalsDatabaseContract.GoalsDB.COL_ENDAMPM, endAmPm);
        values.put(GoalsDatabaseContract.GoalsDB.COL_REPEAT, daysChecked.toString());
        values.put(GoalsDatabaseContract.GoalsDB.COL_ALLDAY, allDay);
        values.put(GoalsDatabaseContract.GoalsDB.COL_REMINDME, reminderSelection);
        values.put(GoalsDatabaseContract.GoalsDB.COL_FREQUENCY, frequencySelection);
        values.put(GoalsDatabaseContract.GoalsDB.COL_REASON, reasonSelection);

        if (cursor.getCount() <= 0) {
            long newRowId = db.insert(GoalsDatabaseContract.GoalsDB.TABLE_NAME, null, values);
        } else {
            String selectionTwo = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " LIKE ?";
            String[] selectionArgsTwo = {goalName};

            int count = db.update(
                    GoalsDatabaseContract.GoalsDB.TABLE_NAME,
                    values,
                    selectionTwo, selectionArgsTwo);
        }

    }

    private String datePrinter(String month, String day, String year) {
        return month + "-" + day + "-" + year;
    }

    private String timePrinter(String hour, String minute, String amPm) {
        return hour + ":" + minute + amPm;
    }


    private void checkDayBoxes(String checkedDays) {
        checkedDays = checkedDays.substring(1, checkedDays.length()-1);
        String[] daysArr = new String[1];
        if (checkedDays.contains(",")) {
            daysArr = checkedDays.split(", ");
        } else {
            daysArr[0] = checkedDays;
        }
        for (int i = 0; i < daysArr.length; i++) {
            if (daysArr[i].equals("Sunday")) {
                CheckBox x = (CheckBox) findViewById(R.id.sundayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Monday")) {
                CheckBox x = (CheckBox) findViewById(R.id.mondayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Tuesday")) {
                CheckBox x = (CheckBox) findViewById(R.id.tuesdayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Wednesday")) {
                CheckBox x = (CheckBox) findViewById(R.id.wednesdayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Thursday")) {
                CheckBox x = (CheckBox) findViewById(R.id.thursdayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Friday")) {
                CheckBox x = (CheckBox) findViewById(R.id.fridayBox);
                x.setChecked(true);
            } else if (daysArr[i].equals("Saturday")) {
                CheckBox x = (CheckBox) findViewById(R.id.saturdayBox);
                x.setChecked(true);
            }
        }
    }

    public void allDayCheckBox(View v) {
        allDay = ((CheckBox) v).isChecked();
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
        } else if (parent.getId() == R.id.reasonspinner) {
            spinner = (Spinner) findViewById(R.id.reasonspinner);
            reasonSelection = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onSetStartTimeClick(View v) {
        DialogFragment startTimePicker = new CreateGoalActivity.TimePickerFragment();
        startTimePicker.show(getSupportFragmentManager(), "startTimePicker");
        startTimePressed = true;
    }

    public void onSetEndTimeClick(View v) {
        DialogFragment endTimePicker = new CreateGoalActivity.TimePickerFragment();
        endTimePicker.show(getSupportFragmentManager(), "endTimePicker");
        endTimePressed = true;
    }

    public void onSetStartDateClick(View v) {
        DialogFragment startDatePicker = new CreateGoalActivity.DatePickerFragment();
        startDatePicker.show(getSupportFragmentManager(), "startDatePicker");
        startDatePressed = true;
    }

    public void onSetEndDateClick(View v) {
        DialogFragment endDatePicker = new CreateGoalActivity.DatePickerFragment();
        endDatePicker.show(getSupportFragmentManager(), "endDatePicker");
        endDatePressed = true;
    }



    public static void setText(String selection) {
        if (startTimePressed) {
            int index = 0;
            if (selection.contains("A")) {
                index = selection.indexOf("A");
                startAmPm = "AM";
            } else if (selection.contains("P")) {
                index = selection.indexOf("P");
                startAmPm = "PM";
            }
            String sub = selection.substring(0, index);
            String[] hourMin = sub.split(":");
            startHour = hourMin[0] ;
            startMin = hourMin[1];
            startTimeText.setText(selection);
            startTimePressed = false;
        } else if (endTimePressed) {
            int index = 0;
            if (selection.contains("A")) {
                index = selection.indexOf("A");
                endAmPm = "AM";
            } else if (selection.contains("P")) {
                index = selection.indexOf("P");
                endAmPm = "PM";
            }
            String sub = selection.substring(0, index);
            String[] hourMin = sub.split(":");
            endHour = hourMin[0] ;
            endMin = hourMin[1];
            endTimeText.setText(selection);
            endTimePressed = false;
        } else if (startDatePressed) {
            String[] monthDayYear = selection.split("-");
            startMonth = monthDayYear[0];
            startDay = monthDayYear[1];
            startYear = monthDayYear[2];
            startDateText.setText(selection);
            startDatePressed = false;
        } else if (endDatePressed) {
            String[] monthDayYear = selection.split("-");
            endMonth = monthDayYear[0];
            endDay = monthDayYear[1];
            endYear = monthDayYear[2];
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