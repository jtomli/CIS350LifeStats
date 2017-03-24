package com.example.jamietomlinson.iteration2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    public static final int GameActivity_ID = 1;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
    }

    //go to Emotions
    public void onEmotionButtonClick(View v) {
        Intent i = new Intent(this, EmotionActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }

    //go to Diary Log
    public void onDiaryLogButtonClick(View v) {
        Intent i = new Intent(this, DiaryLogActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }

    //go to Diary Log
    public void onDiaryButtonClick(View v) {
        Intent i = new Intent(this, DiaryActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }

    //go to setup
    public void setup(View view) {
        Intent intent = new Intent(this, SetupActivityReasons.class);
        intent.putExtra("username", "username");
        intent.putExtra("password", "password");
        startActivity(intent);
    }

    //when View Goals button is clicked, should launch new intent to AllGoalsActivity
    public void viewGoalsButtonClick(View view) {
        Intent i = new Intent(this, AllGoalsActivity.class);
        i.putExtra("username", "username");
        startActivity(i);
    }

    public void calendarButtonClick(View view) {
        Intent i = new Intent(this, CalendarActivity.class);
        i.putExtra("username", "username");
        startActivity(i);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}

