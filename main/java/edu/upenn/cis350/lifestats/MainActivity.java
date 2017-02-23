package edu.upenn.cis350.lifestats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

// mLab API key: 6_E5Xb0hVk7YmUiokdA8ttam_Fowi3EY


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void setup(View view) {
        Intent intent = new Intent(this, SetupActivityReasons.class);
        intent.putExtra("username", "username");
        intent.putExtra("password", "password");
        startActivity(intent);
    }
}
