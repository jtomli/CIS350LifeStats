package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryActivity extends SideMenuActivity {

    String username;

    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, DiaryActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.diary);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.diary);

        Intent i = getIntent();
        username = i.getExtras().getString("username");

        List<String[]> contactList = new ArrayList<String[]>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + DiaryDatabaseContract.DiaryDB.TABLE_NAME;

        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(findViewById(R.id.SCROLLER_ID).getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);



        String[] projection = {
                DiaryDatabaseContract.DiaryDB.COL_USERNAME,
                DiaryDatabaseContract.DiaryDB.COL_DATE,
                DiaryDatabaseContract.DiaryDB.COL_ENTRY
        };

        // Filter results WHERE COL_USERNAME = username
        String selection = DiaryDatabaseContract.DiaryDB.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                DiaryDatabaseContract.DiaryDB.TABLE_NAME,         // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        SentimentDatabaseOpenHelper emdbh = new SentimentDatabaseOpenHelper(this);
        SQLiteDatabase emdb = emdbh.getWritableDatabase();


        String[] emProjection = {
                SentimentDatabaseContract.SentimentDB.COL_DATE,
                SentimentDatabaseContract.SentimentDB.COL_ANGER,
                SentimentDatabaseContract.SentimentDB.COL_DISGUST,
                SentimentDatabaseContract.SentimentDB.COL_FEAR,
                SentimentDatabaseContract.SentimentDB.COL_JOY,
                SentimentDatabaseContract.SentimentDB.COL_SADNESS,


        };
        String emSelection = SentimentDatabaseContract.SentimentDB.COL_USERNAME + " = ?";
        String[] emSelectionArgs = { username };

        Cursor emCursor = emdb.query(
                SentimentDatabaseContract.SentimentDB.TABLE_NAME,         // The table to query
                emProjection,                                     // The columns to return
                emSelection,                                      // The columns for the WHERE clause
                emSelectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );


        ScrollView sv = (ScrollView) findViewById(R.id.SCROLLER_ID);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);


        if (cursor.moveToFirst() && emCursor.moveToFirst()) {
            do {
                int maxIdx = 0;
                Double max = 0.0;
                for (int m = 1; m < 6; m++) {
                    Double curr = emCursor.getDouble(m);
                    if (curr > max) {
                        max = curr;
                        maxIdx = m;
                    }
                }

                TextView tv = new TextView(this);
                tv.setText(cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_DATE)) +
                        "\n" + cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_ENTRY))
                           + "\n" + "\n");
                switch (maxIdx) {
                    case 1:
                        tv.setTextColor(Color.RED);
                        break;
                    case 2:
                        tv.setTextColor(Color.BLACK);
                        break;
                    case 3:
                        tv.setTextColor(Color.GREEN);
                        break;
                    case 4:
                        tv.setTextColor(Color.MAGENTA);
                        break;
                    case 5:
                        tv.setTextColor(Color.BLUE);
                        break;
                }

                ll.addView(tv);
//                diary = diary + cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_DATE))
//                        + "\n" + cursor.getString(cursor.getColumnIndex(DiaryDatabaseContract.DiaryDB.COL_ENTRY))
//                        + "\n" + "\n";

            } while (cursor.moveToNext() && emCursor.moveToNext());

        }
        sv.addView(ll);

        cursor.close();

    }
    /*public void onMenuButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }*/
}