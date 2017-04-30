package project.cis350.upenn.edu.project;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import com.google.gson.Gson;
        import com.jjoe64.graphview.*;
        import com.jjoe64.graphview.series.*;
        import android.graphics.*;
        import java.util.Date;
        import java.util.Calendar;
        import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
/**
 * Created by AK47 on 3/23/17.
 * This class shows statistics on the users moods.  It handles the graph which shows how the user's
 * mood has changes over time and it also gets the average of each emotion over the past week,
 * month, or year depending on the user's request.
 */


public class EmotionActivity extends SideMenuActivity implements AdapterView.OnItemSelectedListener {

    String username;
    String emotionSelection = "past week";

    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, EmotionActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.emotions);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.emotions);

        username = getIntent().getExtras().getString("username");

       String[] arrSpinner = new String[]{"past week", "past month", "past year"};

        Spinner emotionSpinner = (Spinner) findViewById(R.id.emotion_spinner);
        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrSpinner);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(adapterOne);
        emotionSpinner.setOnItemSelectedListener(this);

        SentimentDatabaseOpenHelper dbh = new SentimentDatabaseOpenHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        String[] projection = {
                SentimentDatabaseContract.SentimentDB.COL_DATE,
                SentimentDatabaseContract.SentimentDB.COL_ANGER,
                SentimentDatabaseContract.SentimentDB.COL_DISGUST,
                SentimentDatabaseContract.SentimentDB.COL_FEAR,
                SentimentDatabaseContract.SentimentDB.COL_JOY,
                SentimentDatabaseContract.SentimentDB.COL_SADNESS,


        };
        String selection = SentimentDatabaseContract.SentimentDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                SentimentDatabaseContract.SentimentDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        String[] dates = new String[cursor.getCount()];
        DataPoint[] anger = new DataPoint[cursor.getCount()];
        DataPoint[] disgust = new DataPoint[cursor.getCount()];
        DataPoint[] fear = new DataPoint[cursor.getCount()];
        DataPoint[] joy = new DataPoint[cursor.getCount()];
        DataPoint[] sadness = new DataPoint[cursor.getCount()];

        // looping through all rows and adding to list
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                String d = cursor.getString(0);
                String[] mdy = d.split(", ");
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(mdy[2]), i, Integer.parseInt(mdy[1]));
               // c.set(Integer.parseInt(mdy[2]), Integer.parseInt(mdy[0]), Integer.parseInt(mdy[1]));
                Date date = c.getTime();
                dates[i] = d;
                anger[i] = new DataPoint(date, cursor.getDouble(1));
                disgust[i] = new DataPoint(date, cursor.getDouble(2));
                fear[i] = new DataPoint(date, cursor.getDouble(3));
                joy[i] = new DataPoint(date, cursor.getDouble(4));
                sadness[i] = new DataPoint(date, cursor.getDouble(5));
//                anger[i] = new DataPoint(i, cursor.getDouble(1));
//                disgust[i] = new DataPoint(i, cursor.getDouble(2));
//                fear[i] = new DataPoint(i, cursor.getDouble(3));
//                joy[i] = new DataPoint(i, cursor.getDouble(4));
//                sadness[i] = new DataPoint(i, cursor.getDouble(5));

                i++;
            } while (cursor.moveToNext());

        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        //anger
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(anger);
        series1.setTitle("Anger");
        series1.setColor(Color.RED);
        series1.setDrawDataPoints(true);
        series1.setDataPointsRadius(10);
        graph.addSeries(series1);

        //disgust
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(disgust);
        series2.setTitle("Disgust");
        series2.setColor(Color.BLACK);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        graph.addSeries(series2);

        //fear
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(fear);
        series3.setTitle("Fear");
        series3.setColor(Color.GREEN);
        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(10);
        graph.addSeries(series3);

        //joy
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(joy);
        series4.setTitle("Joy");
        series4.setColor(Color.MAGENTA);
        series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(10);
        graph.addSeries(series4);

        //sadness
        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(sadness);
        series5.setTitle("Sadness");
        series5.setColor(Color.BLUE);
        series5.setDrawDataPoints(true);
        series5.setDataPointsRadius(10);
        graph.addSeries(series5);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(2);
        graph.getViewport().setScalable(true);
        //graph.getViewport().setScalableY(true);


        Calendar curr = Calendar.getInstance();
        int day = curr.get(Calendar.DAY_OF_MONTH);
        int month = curr.get(Calendar.MONTH);
        int year = curr.get(Calendar.YEAR);

        //anger, disgust, fear, joy, sadness
        Double[] tots = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        switch (emotionSelection) {
            case "past week":
                for (int x = 0; x < dates.length; x++) {
                    String[] mdy = dates[x].split(", ");
                    if (Integer.parseInt(mdy[2]) == year) {
                        if (Integer.parseInt(mdy[0]) == month) {
                            if (Integer.parseInt(mdy[1]) <= day && Integer.parseInt(mdy[1]) > day - 7) {
                                tots[0] += anger[x].getY();
                                tots[1] += disgust[x].getY();
                                tots[2] += fear[x].getY();
                                tots[3] += joy[x].getY();
                                tots[4] += sadness[x].getY();

                            }
                        }
                    }
                };
                break;
            case "past month":
                for (int x = 0; x < dates.length; x++) {
                    String[] mdy = dates[x].split(", ");
                    if (Integer.parseInt(mdy[2]) == year) {
                        if (Integer.parseInt(mdy[0]) == month) {

                            tots[0] += anger[x].getY();
                            tots[1] += disgust[x].getY();
                            tots[2] += fear[x].getY();
                            tots[3] += joy[x].getY();
                            tots[4] += sadness[x].getY();
                        }
                    }
                };
                break;
            case "past year":
                for (int x = 0; x < dates.length; x++) {
                    String[] mdy = dates[x].split(", ");
                    if (Integer.parseInt(mdy[2]) == year) {

                        tots[0] += anger[x].getY();
                        tots[1] += disgust[x].getY();
                        tots[2] += fear[x].getY();
                        tots[3] += joy[x].getY();
                        tots[4] += sadness[x].getY();

                    }
                };
                break;
        }

        TextView tv1 = (TextView) findViewById(R.id.anger);
        tv1.setText("Anger: " + Math.round(tots[0] / dates.length * 100.0) + "%");
        tv1.setTextColor(Color.RED);
        TextView tv2 = (TextView) findViewById(R.id.disgust);
        tv2.setText("Disgust: " + Math.round(tots[1] / dates.length * 100.0) + "%");
        tv2.setTextColor(Color.BLACK);
        TextView tv3 = (TextView) findViewById(R.id.fear);
        tv3.setText("Fear: " + Math.round(tots[2] / dates.length * 100.0) + "%");
        tv3.setTextColor(Color.GREEN);
        TextView tv4 = (TextView) findViewById(R.id.joy);
        tv4.setText("Joy: " + Math.round(tots[3] / dates.length * 100.0) + "%");
        tv4.setTextColor(Color.MAGENTA);
        TextView tv5 = (TextView) findViewById(R.id.sadness);
        tv5.setText("Sadness: " + Math.round(tots[4] / dates.length * 100.0) + "%");
        tv5.setTextColor(Color.BLUE);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner;
        if (parent.getId() == R.id.emotion_spinner) {
            spinner = (Spinner) findViewById(R.id.emotion_spinner);
            emotionSelection = parent.getItemAtPosition(position).toString();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}