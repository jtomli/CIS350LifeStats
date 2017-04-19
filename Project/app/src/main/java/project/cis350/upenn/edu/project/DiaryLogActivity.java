package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AK47 on 2/21/17.
 */

public class DiaryLogActivity extends SideMenuActivity {

    String username;
    String sentiment;


    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, DiaryLogActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.diary_log);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.diary_log);



        username = getIntent().getExtras().getString("username");
    }
    public void onSubmitButton(final View v) {
        new AlertDialog.Builder(this)
                .setTitle("Submit Diary Entry")
                .setMessage("Are you sure you want to submit this entry?")
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Calendar date = Calendar.getInstance();
                        int month = date.get(Calendar.MONTH);
                        String d = "";
                        switch (month) {
                            case 0:
                                d += "January ";
                                break;
                            case 1:
                                d += "Febuary ";
                                break;
                            case 2:
                                d += "March ";
                                break;
                            case 3:
                                d += "April ";
                                break;
                            case 4:
                                d += "May ";
                                break;
                            case 5:
                                d += "June ";
                                break;
                            case 6:
                                d += "July ";
                                break;
                            case 7:
                                d += "August ";
                                break;
                            case 8:
                                d += "September ";
                                break;
                            case 9:
                                d += "October ";
                                break;
                            case 10:
                                d += "November ";
                                break;
                            case 11:
                                d += "December ";
                                break;
                        }
                        d += date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR);




                        //get diary entry
                        String entry = ((EditText) findViewById(R.id.diary_text)).getText().toString();

                        //get mood from api
                        Mood m = new Mood(v.getContext());
                        m.execute(new String[]{entry, username});



                        //store entry
                        DiaryDatabaseHelper dbh = new DiaryDatabaseHelper(v.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DiaryDatabaseContract.DiaryDB.COL_DATE, d + "");
                        values.put(DiaryDatabaseContract.DiaryDB.COL_ENTRY, entry);
                        values.put(DiaryDatabaseContract.DiaryDB.COL_USERNAME, username);
                        db.insert(DiaryDatabaseContract.DiaryDB.TABLE_NAME, null, values);
                        db.close();

                        AllGoalsActivity.openActivity(DiaryLogActivity.this, username);
                    }})
                .setNegativeButton(android.R.string.cancel, null).show();

    }
   /* public void onMenuButton(View v) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }*/
}