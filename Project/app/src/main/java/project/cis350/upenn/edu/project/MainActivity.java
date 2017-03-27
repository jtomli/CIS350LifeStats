package project.cis350.upenn.edu.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.content.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import java.util.ArrayList;

import static project.cis350.upenn.edu.project.R.id.reasons;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int GameActivity_ID = 1;

    private GoogleApiClient mGoogleApiClient;
    User user;
    String username;
    ArrayList<String> reasons;
    String sentiment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //go to Emotions
    public void onEmotionButtonClick(View v) {
        Intent i = new Intent(this, EmotionActivity.class);
        i.putExtra("username", username);
        startActivity(i);

    }

    //go to Calendar
    public void calendarButtonClick(View v) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //go to Goals
    public void createGoal(View v) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //go to Goals
    public void viewGoalsButtonClick(View v) {
        Intent intent = new Intent(this, AllGoalsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //go to Diary Log
    public void onDiaryLogButtonClick(View v) {
        Intent intent = new Intent(this, DiaryLogActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }

    //go to Diary Log
    public void onDiaryButtonClick(View v) {

        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }

    //go to setup
    public void setup(View view) {
        Intent intent = new Intent(this, SetupActivityReasons.class);
        intent.putExtra("username", username);
        intent.putExtra("fromSetupButton", "yes");
        startActivity(intent);
    }

    //sign out
    public void signOut(View v) {
        Toast.makeText(this, "You are now signed out.", Toast.LENGTH_LONG).show();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Connection to Google APIs failed. (Not our fault!)", Toast.LENGTH_LONG).show();
    }
}