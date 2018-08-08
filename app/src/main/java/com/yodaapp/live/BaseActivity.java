package com.yodaapp.live;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BaseActivity extends Activity
{
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected CharSequence mDrawerTitle;
    protected CharSequence mTitle;
    protected String[] mPlanetTitles;
    String school_id = "";
    String user_id = "";
    String user_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);



        // set a custom shadow that overlays the main content when the drawer opens
        try {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        mDrawerList.setAdapter(new ArrayAdapter<>(BaseActivity.this,R.layout.drawer_list_item, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        Log.d("planet", "point 6");
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        Log.d("planet","point 7");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        /*case R.id.action_websearch:
            return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectItem(int position) {

        // update the main content by replacing fragments
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        BaseActivity.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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

    /* The click listner for ListView in the navigation drawer */
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String planet = getResources().getStringArray(R.array.planets_array)[position];
            mDrawerLayout.closeDrawer(mDrawerList);
            switch (planet) {
                case "School":
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    BaseActivity.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
                case "Communications": {
                    Intent intent2 = new Intent(BaseActivity.this, MainActivity.class);
                    intent2.putExtra("user_id", user_id);
                    intent2.putExtra("school_id", school_id);
                    intent2.putExtra("email", user_email);
                    intent2.putExtra("redirection", "Communications");
                    BaseActivity.this.finish();
                    startActivity(intent2);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
                }
                case "Logout": {
                    dbhelp entry= new dbhelp(BaseActivity.this);
                    entry.open();
                    entry.logout_user();
                    entry.close();
                }
                case "Exit": {
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
                }
            }
        }
    }
}

