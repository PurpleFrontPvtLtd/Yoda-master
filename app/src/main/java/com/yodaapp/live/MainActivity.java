package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yodaapp.live.Parent.MainActivity_Parent;
import com.yodaapp.live.model.MainPage_Model;

import java.util.List;
import java.util.Stack;


public class MainActivity extends FragmentActivity
{


    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    static String school_id = "";
    static String user_id = "";
    static String user_email = "";
    List<MainPage_Model> feedslist;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private int mPreviousBackStackCount = 0;
    Stack<String> mFragPositionTitleDisplayed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragPositionTitleDisplayed = new Stack<>();
        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //        Log.d("planet","point 1");
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

//        Log.d("planet","point 2");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

//        Log.d("planet","point 3");

        // set a custom shadow that overlays the main content when the drawer opens
        try {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[7];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.school_icon1, "School");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.communication_icon, "Communications");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.calendar_icon, "Calendar");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.profile_icon1, "View/Edit Profile");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.password_icon2, "Change Password");
        drawerItem[5] = new ObjectDrawerItem(R.drawable.logout_icon1, "Logout");
        drawerItem[6] = new ObjectDrawerItem(R.drawable.exit_icon1, "Exit");


//        Log.d("planet","point 33");

        // set up the drawer's list view with items and click listener
//        mDrawerList.setAdapter(new ArrayAdapter<>(MainActivity.this,R.layout.drawer_list_item, mPlanetTitles));
//        Log.d("planet","point 4");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

//        Log.d("planet","point 5");

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,                  /* host Activity */
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
//        Log.d("planet", "point 6");
        mDrawerLayout.setDrawerListener(mDrawerToggle);


//        Log.d("planet","point 7");

        // Redirecting user to page requested in intent after clicking on menu drawer activity
        String redirection = "";
        try {
            redirection = getIntent().getExtras().getString("redirection");
            switch (redirection) {
                case "Communications": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new GroupActivity1();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);
/*
                    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
           */         fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Communications");

                   /* Intent intent=new Intent(MainActivity.this,GroupActivity1.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("school_id", school_id);
                    startActivity(intent);
                    finish();*/


                    break;
                }
                case "Calender": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Calender_Home();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);

                /*    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
              */      fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Events");
                    break;
                }
                case "View/Edit Profile": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new ViewEditprofile();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);
/*
                    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
          */          fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("View/Edit Profile");
                    break;
                }
            }
        }
        catch(Exception ignored)
        {
            ignored.printStackTrace();
            if (savedInstanceState == null) {
                selectItem(0);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        /*case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String planet = getResources().getStringArray(R.array.planets_array)[position];
//            Log.d("planet",planet);
            //        selectItem(position);
            mDrawerLayout.closeDrawer(mDrawerList);
            switch (planet) {
                case "School":
                    selectItem(0);
                    break;
                case "Communications": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new GroupActivity1();

/*
                    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
           */         fragmentTransaction.replace(R.id.content_frame, test);
                  //  fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Communications");
/*

                    Intent intent=new Intent(MainActivity.this,GroupActivity1.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("school_id", school_id);
                    startActivity(intent);
                    finish();
*/


                    break;
                }
                case "Calendar": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Calender_Home();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);

//                    fragmentTransaction.setCustomAnimations(
//                            R.animator.fragment_slide_left_enter,
//                            R.animator.fragment_slide_left_exit,
//                            R.animator.fragment_slide_right_enter,
//                            R.animator.fragment_slide_right_exit);
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Events");
                    break;
                }
                case "View/Edit Profile": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new ViewEditprofile();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);
//
//                    fragmentTransaction.setCustomAnimations(
//                            R.animator.fragment_slide_left_enter,
//                            R.animator.fragment_slide_left_exit,
//                            R.animator.fragment_slide_right_enter,
//                            R.animator.fragment_slide_right_exit);
                   fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("View/Edit Profile");
                    break;
                }
                case "Change Password": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new ChangePassword();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    test.setArguments(args);
              /*      fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
        */            fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Change Password");
                    break;
                }
                case "Logout": {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getResources().getString(R.string.logout_warning))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dbhelp entry= new dbhelp(MainActivity.this);
                                    entry.open();
                                    entry.logout_user();
                                    entry.close();
                                    Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                                    MainActivity.this.finish();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                case "Exit": {
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
                }
            }
        }
    }

    private void selectItem(int position) {

        // update the main content by replacing fragments
        Fragment test;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        test = new PlanetFragment();
        Bundle args = new Bundle();
        args.putString("user_id", user_id);
        args.putString("email", user_email);
        args.putString("school_id", school_id);
        test.setArguments(args);

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

       /* fragmentTransaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);*/
        fragmentTransaction.replace(R.id.content_frame, test);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
            getActionBar().setTitle("School");
        }

        else {
            super.onBackPressed();
            MainActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }


    }

  /*  @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if(mPreviousBackStackCount >= backStackEntryCount) {
            mFragPositionTitleDisplayed.pop();
            if (backStackEntryCount == 0)
                getActionBar().setTitle(R.string.app_name);
            else if (backStackEntryCount > 0) {
                getActionBar().setTitle(mFragPositionTitleDisplayed.peek());
            }
            mPreviousBackStackCount--;
        }
        else{
            mFragPositionTitleDisplayed.push("test");
            mPreviousBackStackCount++;
        }
    }*/
}
