package project.cis350.upenn.edu.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CalendarActivity extends SideMenuActivity {

    public static void openActivity(Activity from_activity, String username) {
        Intent intent = new Intent(from_activity, CalendarActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.calendar_layout);
        bundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
        intent.putExtra(MainActivity.KEY_MAIN_BUNDLE, bundle);
        intent.putExtra("username", username);
        from_activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.calendar_layout);
    }

}