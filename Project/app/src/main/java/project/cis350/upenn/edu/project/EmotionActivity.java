package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;
import android.graphics.*;
/**
 * Created by AK47 on 3/23/17.
 */

public class EmotionActivity extends AppCompatActivity{
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotions);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        List<String[]> contactList = new ArrayList<String[]>();

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


        DataPoint[] anger = new DataPoint[cursor.getCount()];
        DataPoint[] disgust = new DataPoint[cursor.getCount()];
        DataPoint[] fear = new DataPoint[cursor.getCount()];
        DataPoint[] joy = new DataPoint[cursor.getCount()];
        DataPoint[] sadness = new DataPoint[cursor.getCount()];

        // looping through all rows and adding to list
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                anger[i] = new DataPoint(i, cursor.getDouble(1));
                disgust[i] = new DataPoint(i, cursor.getDouble(2));
                fear[i] = new DataPoint(i, cursor.getDouble(3));
                joy[i] = new DataPoint(i, cursor.getDouble(4));
                sadness[i] = new DataPoint(i, cursor.getDouble(5));

                i++;
            } while (cursor.moveToNext());

        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        //anger
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(anger);
        series1.setTitle("Anger");
        series1.setColor(Color.RED);
        graph.addSeries(series1);

        //disgust
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(disgust);
        series2.setTitle("Disgust");
        series2.setColor(Color.BLACK);
        graph.addSeries(series2);

        //fear
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(fear);
        series3.setTitle("Fear");
        series3.setColor(Color.GREEN);
        graph.addSeries(series3);

        //joy
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(joy);
        series4.setTitle("Joy");
        series4.setColor(Color.MAGENTA);
        graph.addSeries(series4);

        //sadness
        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(sadness);
        series5.setTitle("Sadness");
        series5.setColor(Color.BLUE);
        graph.addSeries(series5);

        graph.getLegendRenderer().setVisible(true);
    }
    public void onMenuButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}
