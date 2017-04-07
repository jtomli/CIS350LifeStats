package project.cis350.upenn.edu.project;


import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;


/**
 *
 * Created by Colin on 4/3/17.
 *
 */

public class SideMenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private String username;

    public static final String KEY_MAIN_BUNDLE = "project.cis350.upenn.edu.project.MAIN_BUNDLE_KEY";
    public static final String KEY_HAS_DRAWER = "project.cis350.upenn.edu.project.HAS_DRAWER";
    public static final String KEY_LAYOUT_ID = "project.cis350.upenn.edu.project.LAYOUT_ID_KEY";
    public static final String KEY_SECTION_TITLE = "project.cis350.upenn.edu.project.SECTION_TITLE";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getExtras().getString("username");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Bundle mainBundle;
        if (!getIntent().hasExtra(KEY_MAIN_BUNDLE)) {
            // Set defaults
            mainBundle = new Bundle();
            mainBundle.putInt(SideMenuActivity.KEY_LAYOUT_ID, R.layout.activity_main);
            mainBundle.putBoolean(SideMenuActivity.KEY_HAS_DRAWER, true);
            //throw new IllegalArgumentException("Cannot access a subclass of MainActivity without including a main bundle.");
        } else {
            mainBundle = getIntent().getBundleExtra(KEY_MAIN_BUNDLE);
        }

        int contentID = mainBundle.getInt(KEY_LAYOUT_ID);
        setContentView(contentID);

        if (mainBundle.getBoolean(KEY_HAS_DRAWER)) {
            setupDrawer();
        }
    }

    private void setupDrawer() {
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = ((TextView) view).getText().toString();
                System.out.println(option);
                switch(option) {
                    case "View Goals":
                        goToGoals();
                        break;
                    case "Create Goal":
                        goToCreateGoal();
                        break;
                    case "Calendar":
                        goToCalendar();
                        break;
                    case "View Diary":
                        goToDiary();
                        break;
                    case "Add to Diary":
                        goToAddToDiary();
                        break;
                    case "Emotion Graph":
                        goToEmotionGraph();
                        break;
                    case "Settings":
                        goToSettings();
                        break;
                    case "Sign Out":
                        signOut();
                        break;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        Drawable background = getResources().getDrawable(R.drawable.actionbarlogo);
        getSupportActionBar().setBackgroundDrawable(background);

        mActivityTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void goToGoals() {
        AllGoalsActivity.openActivity(SideMenuActivity.this, username);
        //Intent intent = new Intent(this, AllGoalsActivity.class);
        //intent.putExtra("username", username);
        //startActivity(intent);
    }

    private void goToCreateGoal() {
        CreateGoalActivity.openActivity(SideMenuActivity.this, username);
        /*
        Intent intent = new Intent(this, CreateGoalActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        */
    }

    private void goToCalendar() {
        CalendarActivity.openActivity(SideMenuActivity.this, username);
        /*
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        */
    }

    private void goToAddToDiary() {
        DiaryLogActivity.openActivity(SideMenuActivity.this, username);
        /*
        Intent intent = new Intent(this, DiaryLogActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        */
    }

    private void goToEmotionGraph() {
        EmotionActivity.openActivity(SideMenuActivity.this, username);
        /*
        Intent i = new Intent(this, EmotionActivity.class);
        i.putExtra("username", username);
        startActivity(i);
        */
    }

    private void goToDiary() {
        DiaryActivity.openActivity(SideMenuActivity.this, username);
        /*
        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        */
    }

    private void goToSettings() {
        SetupActivityReasons.openActivity(SideMenuActivity.this, username, "yes");
        /*
        Intent intent = new Intent(this, SetupActivityReasons.class);
        intent.putExtra("username", username);
        intent.putExtra("fromSetupButton", "yes");
        startActivity(intent);
        */
    }

    private void signOut() {
        Toast.makeText(this, "You are now signed out.", Toast.LENGTH_LONG).show();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent i = new Intent(SideMenuActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void addDrawerItems() {
        String[] options = { "View Goals", "Create Goal", "Calendar", "View Diary", "Add to Diary", "Emotion Graph", "Settings", "Sign Out" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Connection to Google APIs failed. (Not our fault!)", Toast.LENGTH_LONG).show();
    }
}
