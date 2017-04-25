package project.cis350.upenn.edu.project;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateGoalActivity extends SideMenuActivity implements AdapterView.OnItemSelectedListener {

    // layout elements
    private static TextView startTimeText;
    private static TextView endTimeText;
    private static TextView startDateText;
    private static TextView endDateText;
    protected static boolean startTimePressed = false;
    protected static boolean endTimePressed = false;
    protected static boolean startDatePressed = false;
    protected static boolean endDatePressed = false;

    // for GoalsDB
    private boolean allDay = false;
    List<String> daysChecked;
    private String frequencySelection;
    private String reminderSelection;
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

    // from UserDB
    String username;
    ArrayList<String> reasons;

    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, CreateGoalActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.activity_goal);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar c = Calendar.getInstance();

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");


        // populate "reasons" spinner with user-defined reasons
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

        String selection = UserDatabaseContract.UserDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                UserDatabaseContract.UserDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        reasons = new ArrayList<String>();

        // access list of reasons for each row
        while(cursor.moveToNext()) {
            for (int i = 0; i < 11; i++) {
                String item = cursor.getString(cursor.getColumnIndex("reasons" + i));
                if (!item.isEmpty()) {
                    reasons.add(item);

                    if (i == 0) {
                        reasonSelection = item;
                    }
                }
            }
        }
        cursor.close();
        db.close();
        dbHelper.close();

        // setting labels at start up for startTime
        String currTime = hourConverter(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        startTimeText = (TextView) findViewById(R.id.setStartTime);
        startTimeText.setText(currTime);

        // set default values: startHour & startMin
        startHour = "" + c.get(Calendar.HOUR_OF_DAY);
        startMin = "" + c.get(Calendar.MINUTE);
        startAmPm = "PM";

        // setting labels at start up for endTime
        String currEndTime = hourConverter(c.get(Calendar.HOUR_OF_DAY) + 1, c.get(Calendar.MINUTE));
        endTimeText = (TextView) findViewById(R.id.setEndTime);
        endTimeText.setText(currEndTime);

        // set default values: endHour & EndMin
        endHour = "" + (c.get(Calendar.HOUR_OF_DAY) + 1);
        endMin = "" + c.get(Calendar.MINUTE);
        endAmPm = "PM";

        //setting labels at start up for DATE
        c.add(Calendar.DATE, 0);
        SimpleDateFormat oldFormat = new SimpleDateFormat("MM-dd-yyyy");
        String currDate = oldFormat.format(c.getTime());
        startDateText = (TextView) findViewById(R.id.setStartDate);
        startDateText.setText(currDate);
        endDateText = (TextView) findViewById(R.id.setEndDate);
        endDateText.setText(currDate);

        startYear = currDate.substring(6);
        startMonth = currDate.substring(0, 2);
        startDay = currDate.substring(3, 5);
        endYear = currDate.substring(6);
        endMonth = currDate.substring(0, 2);
        endDay = currDate.substring(3, 5);

        frequencySelection = "One time only";
        reminderSelection = "Never";

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
        reasonsSpinner.setOnItemSelectedListener(this);

        // goal name
        final EditText goalInput = (EditText) findViewById(R.id.goalNameInput);

        daysChecked = new ArrayList<String>();

        final Button addGoal = (Button) findViewById(R.id.addGoalButton);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (checkEvents() == 0) {
                    Toast.makeText(CreateGoalActivity.this,
                            "Your goal does not occur between your start and end dates.", Toast.LENGTH_LONG).show();
                } else {
                    goalName = goalInput.getText().toString();
                    addGoal();
                    createEvents();
                    PopupMenu popup = new PopupMenu(CreateGoalActivity.this, addGoal);
                    popup.getMenuInflater().inflate(R.menu.add_goal_popup_menu, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();
                            if (itemId == R.id.addAnother) {
                                CreateGoalActivity.openActivity(CreateGoalActivity.this, username);
                            } else if (itemId == R.id.mainMenu) {
                                AllGoalsActivity.openActivity(CreateGoalActivity.this, username);
                            }
                            return true;
                        }
                    });
                }
            }
        });
    }

    /**
     * Adds the created goal to the GoalsDB
     * If the created goal has the same name as an existing goal,
     * updates the existing goal instead
     */
    public void addGoal() {

        GoalsDatabaseOpenHelper dbHelper = new GoalsDatabaseOpenHelper(this);
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

        String selection = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " = ? AND " +
                GoalsDatabaseContract.GoalsDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { goalName, username };

        Cursor cursor = db.query(
                GoalsDatabaseContract.GoalsDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
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
            String selectionTwo = GoalsDatabaseContract.GoalsDB.COL_GOALNAME + " LIKE ? AND " +
                    GoalsDatabaseContract.GoalsDB.COL_USERNAME + " LIKE ?";
            String[] selectionArgsTwo = {goalName, username};

            int count = db.update(
                    GoalsDatabaseContract.GoalsDB.TABLE_NAME,
                    values,
                    selectionTwo, selectionArgsTwo);

        }

        cursor.close();
        db.close();
        dbHelper.close();

        createNotification();
    }

    /**
     * Determines the dates all of a Goal's Events that occur on the specified day of the week
     * Adds the Events to the EventsDB
     *
     * @param cal the start date of the goal
     * @param end the end data of the goal
     * @param dayOfWeek the day of week that the event should occur
     *                  method is called once per day of week selected by the user
     */
    public void increment(Calendar cal, Calendar end, int dayOfWeek) {

        EventsDatabaseOpenHelper dbHelper = new EventsDatabaseOpenHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        while (cal.compareTo(end) <= 0){
            while (!(cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            ContentValues values = new ContentValues();
            values.put(EventsDatabaseContract.EventsDB.COL_USERNAME, username);
            values.put(EventsDatabaseContract.EventsDB.COL_GOALNAME, goalName);
            values.put(EventsDatabaseContract.EventsDB.COL_YEAR, cal.get(Calendar.YEAR));
            values.put(EventsDatabaseContract.EventsDB.COL_MONTH, cal.get(Calendar.MONTH));
            values.put(EventsDatabaseContract.EventsDB.COL_DAY, cal.get(Calendar.DAY_OF_MONTH));
            values.put(EventsDatabaseContract.EventsDB.COL_LOG, "no");

            long newRowId = db.insert(EventsDatabaseContract.EventsDB.TABLE_NAME, null, values);

            if (frequencySelection.equals("Weekly")) {
                cal.add(Calendar.DAY_OF_MONTH, 7);
            } else if (frequencySelection.equals("Biweekly")) {
                cal.add(Calendar.DAY_OF_MONTH, 14);
            } else if (frequencySelection.equals("Monthly")) {
                cal.add(Calendar.MONTH, 1);
            } else {
                break;
            }
        }

        db.close();
        dbHelper.close();
    }

    /**
     * Determines the first Event occurring on each day of the week that the Goal is to be logged
     * Calls increment() to add Events to the EventsDB
     */
    public void createEvents() {

        Calendar end = Calendar.getInstance();
        end.set(Integer.parseInt(endYear), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));

        for (int i = 0; i < daysChecked.size(); i++) {

            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(startYear), Integer.parseInt(startMonth) - 1, Integer.parseInt(startDay));

            if (daysChecked.get(i).equals("Sunday")) {
                increment(cal, end, Calendar.SUNDAY);
            } else if (daysChecked.get(i).equals("Monday")) {
                increment(cal, end, Calendar.MONDAY);
            } else if (daysChecked.get(i).equals("Tuesday")) {
                increment(cal, end, Calendar.TUESDAY);
            } else if (daysChecked.get(i).equals("Wednesday")) {
                increment(cal, end, Calendar.WEDNESDAY);
            } else if (daysChecked.get(i).equals("Thursday")) {
                increment(cal, end, Calendar.THURSDAY);
            } else if (daysChecked.get(i).equals("Friday")) {
                increment(cal, end, Calendar.FRIDAY);
            } else if (daysChecked.get(i).equals("Saturday")){
                increment(cal, end, Calendar.SATURDAY);
            }
        }
    }

    /**
     * Returns the number of a Goal's Events that occur on the specified day of the week
     *
     * @param cal the start date of the goal
     * @param end the end data of the goal
     * @param dayOfWeek the day of week that the event should occur
     *                  method is called once per day of week selected by the user
     *
     * @return the number of Events occurring on the specified day between the start and end date
     */
    public int checkIncrement(Calendar cal, Calendar end, int dayOfWeek) {

        int numEvents = 0;

        while (cal.compareTo(end) <= 0){
            while (!(cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            if (cal.compareTo(end) <= 0){
                numEvents++;
            }

            if (frequencySelection.equals("Weekly")) {
                cal.add(Calendar.DAY_OF_MONTH, 7);
            } else if (frequencySelection.equals("Biweekly")) {
                cal.add(Calendar.DAY_OF_MONTH, 14);
            } else if (frequencySelection.equals("Monthly")) {
                cal.add(Calendar.MONTH, 1);
            } else {
                break;
            }
        }

        return numEvents;
    }

    /**
     * Returns the number Events that a Goal has
     * Users may not create a Goal with zero events
     *
     * @return the number of Events occurring for the specified Goal
     */
    public int checkEvents() {

        int numEvents = 0;

        Calendar end = Calendar.getInstance();
        end.set(Integer.parseInt(endYear), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));

        for (int i = 0; i < daysChecked.size(); i++) {

            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(startYear), Integer.parseInt(startMonth) - 1, Integer.parseInt(startDay));

            if (daysChecked.get(i).equals("Sunday")) {
                numEvents += checkIncrement(cal, end, Calendar.SUNDAY);
            } else if (daysChecked.get(i).equals("Monday")) {
                numEvents += checkIncrement(cal, end, Calendar.MONDAY);
            } else if (daysChecked.get(i).equals("Tuesday")) {
                numEvents += checkIncrement(cal, end, Calendar.TUESDAY);
            } else if (daysChecked.get(i).equals("Wednesday")) {
                numEvents += checkIncrement(cal, end, Calendar.WEDNESDAY);
            } else if (daysChecked.get(i).equals("Thursday")) {
                numEvents += checkIncrement(cal, end, Calendar.THURSDAY);
            } else if (daysChecked.get(i).equals("Friday")) {
                numEvents += checkIncrement(cal, end, Calendar.FRIDAY);
            } else if (daysChecked.get(i).equals("Saturday")){
                numEvents += checkIncrement(cal, end, Calendar.SATURDAY);
            }
        }

        return numEvents;
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

    private void createNotification() {
        if (!reminderSelection.equals("Never")) {
            Integer hour = Integer.valueOf(startHour);
            Integer min = Integer.valueOf(startMin);

            if (startAmPm.equals("PM")) {
                hour += 12;
            }

            if (reminderSelection.equals("1 hour before")) {
                hour--;
            } else if (reminderSelection.equals("15 minutes before")) {
                min -= 15;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);

            Intent intent1 = new Intent(CreateGoalActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateGoalActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) CreateGoalActivity.this.getSystemService(CreateGoalActivity.this.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            System.out.println("Alarm set for: "+calendar.getTimeInMillis() + " :: "+ hour + " :: "+min);
        }
    }
}


