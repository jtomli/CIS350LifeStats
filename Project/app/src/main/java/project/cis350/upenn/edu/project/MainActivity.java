package project.cis350.upenn.edu.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.content.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    public static final int GameActivity_ID = 1;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainApplication.getGoogleApiHelper().connect();
        if (MainApplication.getGoogleApiHelper().isConnected()) {
            mGoogleApiClient = MainApplication.getGoogleApiHelper().getGoogleApiClient();
        }
    }

    //go to Diary Log
    public void onDiaryLogButtonClick(View v) {
        Intent i = new Intent(this, DiaryLogActivity.class);
        startActivity(i);
    }

    //go to Diary Log
    public void onDiaryButtonClick(View v) {
        Intent i = new Intent(this, DiaryActivity.class);
        startActivity(i);
    }

    //go to setup
    public void setup(View view) {
        Intent intent = new Intent(this, SetupActivityReasons.class);
        intent.putExtra("username", "username");
        intent.putExtra("password", "password");
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

}

