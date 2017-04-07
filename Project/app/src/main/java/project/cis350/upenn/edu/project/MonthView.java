package project.cis350.upenn.edu.project;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MonthView extends TableLayout{

    int day=0,month=0,year=0;
    private TextView btn;
    private TranslateAnimation animSet1,animSet2;
    private Context context;
    private TableRow tr;
    private boolean[] hasEvents = new boolean[32];
    private Map<Event, String /* goal name */> allEvents = new TreeMap<>();
    private Map<Event, String /* goal name */> eventsOnSelected = new TreeMap<>();
    private int[] resDaysSun = {R.string.sunday,R.string.monday,R.string.tuesday,R.string.wednesday,
            R.string.thursday,R.string.friday,R.string.saturday};
    private String[] days;

    private int[] monthIds = {R.string.january,R.string.february,R.string.march,R.string.april,R.string.may,R.string.june,
            R.string.july,R.string.august,R.string.september,R.string.october,R.string.november,R.string.december};

    private String[] months = new String[12];

    String username;

    Calendar cal,prevCal,today;	//today will be used for setting a box around today's date
    //prevCal will be used to display last few dates of previous month in the calendar
    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    public MonthView(Context context){
        super(context);
        init(context);
    }

    private void init(Context contxt)
    {
        Activity source = (Activity) contxt;
        Intent intent = source.getIntent();
        username = intent.getExtras().getString("username");
        context = contxt; //initializing the context variable
        Resources res = getResources();
        for(int i=0;i<12;i++) {
            months[i] = res.getString(monthIds[i]);
        }

        days = new String[7];
        setStretchAllColumns(true); //stretch all columns so that calendar's width fits the screen
        today = Calendar.getInstance();//get current date and time's instance
        today.clear(Calendar.HOUR);//remove the hour,minute,second and millisecond from the today variable
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        cal = (Calendar) today.clone();//create exact copy as today for dates display purpose.

        DisplayMonth(true);//uses cal and prevCal to display the month
    }
    public void GoToDate(Date date)
    {
        cal.setTime(date);
        DisplayMonth(true);
    }
    private boolean animFlag=false;
    //Change month listener called when the user clicks to show next or prev month.
    private OnClickListener ChangeMonthListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            ImageView tv = (ImageView)v;
            //If previous month is to be displayed subtract one from current month.
            if(tv.getTag().equals("<"))
            {
                cal.add(Calendar.MONTH, -1);
                animFlag = false;
            }
            //If next month is to be displayed add one to the current month
            else
            {
                cal.add(Calendar.MONTH, 1);
                animFlag = true;
            }
            selected_day = 0;
            DisplayMonth(true);
        }};
    //Main function for displaying the current selected month
    private void checkForEvents() {

        // clear the map and array because this happens each time the month is changed
        allEvents.clear();
        hasEvents = new boolean[32];

        //load events and goals from database
        EventsDatabaseOpenHelper dbHelper = new EventsDatabaseOpenHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                EventsDatabaseContract.EventsDB.COL_USERNAME,
                EventsDatabaseContract.EventsDB.COL_GOALNAME,
                EventsDatabaseContract.EventsDB.COL_YEAR,
                EventsDatabaseContract.EventsDB.COL_MONTH,
                EventsDatabaseContract.EventsDB.COL_DAY,
                EventsDatabaseContract.EventsDB.COL_LOG
        };

        String selection = EventsDatabaseContract.EventsDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                EventsDatabaseContract.EventsDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        while(cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex(EventsDatabaseContract.EventsDB.COL_YEAR));
            int month = cursor.getInt(cursor.getColumnIndex(EventsDatabaseContract.EventsDB.COL_MONTH));
            int day = cursor.getInt(cursor.getColumnIndex(EventsDatabaseContract.EventsDB.COL_DAY));

            String goalName = cursor.getString(cursor.getColumnIndex(EventsDatabaseContract.EventsDB.COL_GOALNAME));

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);

            Event event = new Event(cal, cal);

            String completed = cursor.getString(cursor.getColumnIndex(EventsDatabaseContract.EventsDB.COL_LOG));
            if (completed.equals("yes")) {
                event.markCompleted(true);
            }

            //if the event is in this month, add it to the map "allEvents"
            if (event.getStart().get(Calendar.MONTH) == this.cal.get(Calendar.MONTH)) {
                allEvents.put(event, goalName);
                hasEvents[event.getStart().get(Calendar.DATE)] = true;
            }
        }
        cursor.close();

    }

    int selected_day=0;
    void DisplayMonth(boolean animationEnabled)
    {
        checkForEvents();
        if(animationEnabled)
        {
            animSet1 = new TranslateAnimation(0,getWidth(),1,1);
            animSet1.setDuration(300);

            animSet2 = new TranslateAnimation(0,-getWidth(),1,1);
            animSet2.setDuration(300);
        }
        Resources r = getResources();
        String tempDay;
        for(int i=0;i<7;i++)
        {
            tempDay = r.getString(resDaysSun[i]);
            days[i] = tempDay.substring(0,3);
        }

        removeAllViews();//Clears the calendar so that a new month can be displayed, removes all child elements (days,week numbers, day labels)

        int firstDayOfWeek,prevMonthDay,nextMonthDay,week;
        cal.set(Calendar.DATE, 1); //Set date = 1st of current month so that we can know in next step which day is the first day of the week.
        firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1; //get which day is on the first date of the month
        week = cal.get(Calendar.WEEK_OF_YEAR)-1; //get which week is the current week.
        if(firstDayOfWeek==0 && cal.get(Calendar.MONTH)==Calendar.JANUARY) //adjustment for week number when january starts with first day of month as sunday
            week = 1;
        if(week==0)
            week = 52;

        prevCal = (Calendar) cal.clone();	//create a calendar item for the previous month by subtracting
        prevCal.add(Calendar.MONTH, -1);	//1 from the current month

        //get the number of days in the previous month to display last few days of previous month
        prevMonthDay = prevCal.getActualMaximum(Calendar.DATE)-firstDayOfWeek+1;
        nextMonthDay = 1;	//set the next month counter to date 1
        android.widget.TableRow.LayoutParams lp;

        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.monthtop, null);

        //create the left arrow button for displaying the previous month

        ImageView btn1 = (ImageView) rl.findViewById(R.id.imgLeft);
        btn1.setTag("<");
        btn1.setOnClickListener(ChangeMonthListener);

        btn = (TextView) rl.findViewById(R.id.txtDay);
        btn.setText(months[cal.get(Calendar.MONTH)]);

        ((TextView)rl.findViewById(R.id.txtYear)).setText(""+cal.get(Calendar.YEAR));

        //create the right arrow button for displaying the next month
        btn1 = (ImageView) rl.findViewById(R.id.imgRight);
        btn1.setTag(">");
        btn1.setOnClickListener(ChangeMonthListener);
        //add the tablerow containing the next and prev views to the calendar
        addView(rl);

        tr = new TableRow(context); //create a new row to add to the tablelayout
        tr.setWeightSum(0.7f);
        lp = new TableRow.LayoutParams();
        lp.weight = 0.1f;
        //Create the day labels on top of the calendar
        for(int i=0;i<7;i++)
        {
            btn = new TextView(context);
            btn.setBackgroundResource(R.drawable.calheader);
            btn.setPadding(10, 3,10, 3);
            btn.setLayoutParams(lp);
            btn.setTextColor(Color.parseColor("#000000"));
            btn.setText(days[i]);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            btn.setGravity(Gravity.CENTER);
            tr.addView(btn);	//add the day label to the tablerow
        }
        if(animationEnabled)
        {
            if(animFlag)
                tr.startAnimation(animSet2);
            else
                tr.startAnimation(animSet1);
        }
        addView(tr); //add the tablerow to the tablelayout (first row of the calendar)

        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		/*initialize the day counter to 1, it will be used to display the dates of the month*/
        int day=1;
        lp = new TableRow.LayoutParams();
        lp.weight = 0.1f;
        for(int i=0;i<6;i++)
        {
            if(day>cal.getActualMaximum(Calendar.DATE))
                break;
            tr = new TableRow(context);
            tr.setWeightSum(0.7f);
            //this loop is used to fill out the days in the i-th row in the calendar
            for(int j=0;j<7;j++)
            {
                btn = new TextView(context);
                btn.setLayoutParams(lp);
                btn.setBackgroundResource(R.drawable.rectgrad);
                btn.setGravity(Gravity.CENTER);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                btn.setTextColor(Color.GRAY);
                if(j<firstDayOfWeek && day==1)  //checks if the first day of the week has arrived or previous month's date should be printed
                    btn.setText(Html.fromHtml(String.valueOf("<b>"+prevMonthDay+++"</b>")));
                else if(day>cal.getActualMaximum(Calendar.DATE)) //checks to see whether to print next month's date
                {
                    btn.setText(Html.fromHtml("<b>"+nextMonthDay+++"</b>"));
                }
                else	//day counter is in the current month
                {
                    try{
                        // draw the individual buttons with appropriate events
                        if(hasEvents[day]) {
                            for (Event e : allEvents.keySet()) {
                                if (e.getStart().get(Calendar.DATE) == day) {
                                    if (e.isCompleted()) {
                                        btn.setBackgroundResource(R.drawable.complete_event);
                                    } else {
                                        btn.setBackgroundResource(R.drawable.incomplete_event);
                                    }
                                    break;
                                }
                            }
                        } else {
                            btn.setBackgroundResource(R.drawable.rectgrad);
                        }
                    }catch(Exception ex)
                    {
                        btn.setBackgroundResource(R.drawable.rectgrad);
                    }
                    cal.set(Calendar.DATE, day);
                    btn.setTag(day); //tag to be used when closing the calendar view
                    btn.setOnClickListener(dayClickedListener);
                    if(cal.equals(today))//if the day is today then set different background and text color
                    {
                        tv = btn;
                        btn.setBackgroundResource(R.drawable.current_day);
                        btn.setTextColor(Color.BLACK);
                        // if current day is selected, populate list view with events for that day
                        if (selected_day==day) {
                            // populate list of events
                            eventsOnSelected.clear();
                            for (Map.Entry<Event, String> entry : allEvents.entrySet()) {
                                if (eventIsOnDay(cal, entry.getKey().getStart())) {
                                    eventsOnSelected.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                    else if(selected_day==day)
                    {
                        tv = btn;
                        btn.setBackgroundResource(R.drawable.selectedgrad);
                        btn.setTextColor(Color.BLACK);
                        // populate list of events
                        eventsOnSelected.clear();
                        for (Map.Entry<Event, String> entry : allEvents.entrySet()) {
                            if (eventIsOnDay(cal, entry.getKey().getStart())) {
                                eventsOnSelected.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                    else
                        btn.setTextColor(Color.BLACK);

                    //set the text of the day
                    btn.setText(Html.fromHtml("<b>"+String.valueOf(day++)+"</b>"));
                    if(j==0)
                        btn.setTextColor(Color.parseColor("#000000"));
                    else if(j==6)
                        btn.setTextColor(Color.parseColor("#000000"));

                    if((day==this.day+1)&&(this.month==cal.get(Calendar.MONTH)+1)&&(this.year==cal.get(Calendar.YEAR)))
                        btn.setBackgroundColor(Color.WHITE);
                }
                btn.setPadding(8,8,8,8);	//maintains proper distance between two adjacent days
                tr.addView(btn);
            }
            if(animationEnabled)
            {
                if(animFlag)
                    tr.startAnimation(animSet2);
                else
                    tr.startAnimation(animSet1);
            }
            //this adds a table row for six times for six different rows in the calendar
            addView(tr);
        }
        for (Map.Entry<Event, String> entry : eventsOnSelected.entrySet()) {
            tr = new TableRow(context);
            tv = new TextView(context);
            tv.setText(entry.getKey().toStringHour() + "\t\t" + entry.getValue());
            tv.setTextSize(20);
            tv.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.BLACK);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = 7;
            tv.setLayoutParams(params);
            tr.setLayoutParams(params);
            tr.addView(tv);
            addView(tr);
        }
    }
    private TextView tv;

    private boolean eventIsOnDay(Calendar cal, Calendar event) {
        if (event.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            if (event.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                if (event.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Called when a day is clicked.
    private OnClickListener dayClickedListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            if(tv!=null)
            {
                try{
                    if(hasEvents[day]) {
                        for (Event e : allEvents.keySet()) {
                            if (eventIsOnDay(cal, e.getStart())) {
                                if (e.isCompleted()) {
                                    tv.setBackgroundResource(R.drawable.complete_event);
                                } else {
                                    tv.setBackgroundResource(R.drawable.incomplete_event);
                                }
                                break;
                            }
                        }
                    }
                    else
                        tv.setBackgroundResource(R.drawable.rectgrad);

                }catch(Exception ex) {
                    tv.setBackgroundResource(R.drawable.rectgrad);
                }
                tv.setPadding(8,8,8,8);
            }
            if(tv.getText().toString().trim().equals(String.valueOf(today.get(Calendar.DATE))))
            {
                tv.setBackgroundResource(R.drawable.selectedgrad);
            }
            day = Integer.parseInt(v.getTag().toString());
            selected_day = day;
            tv = (TextView)v;
            tv.setBackgroundResource(R.drawable.selectedgrad);
            DisplayMonth(false);
			/*save the day,month and year in the public int variables day,month and year
			 so that they can be used when the calendar is closed */

            cal.set(Calendar.DATE, day);
        }
    };

}
