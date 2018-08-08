package com.yodaapp.live;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


public class Section_Student_Assign extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static String user_id = "";
    static String user_email = "";
    static String school_id = "";
    static String branch_id = "";
    static String section_id = "";
    static String grade_id = "";
    static String grade_name = "";
    static String section_name = "";
    static String branch_name = "";
    static int min_role = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_student_assign);

        user_id = getIntent().getExtras().getString("user_id");
        user_email = getIntent().getExtras().getString("email");
        school_id = getIntent().getExtras().getString("school_id");
        branch_id =  getIntent().getExtras().getString("branch_id");
        section_id = getIntent().getExtras().getString("section_id");
        grade_id = getIntent().getExtras().getString("grade_id");
        grade_name = getIntent().getExtras().getString("grade_name");
        section_name = getIntent().getExtras().getString("section_name");
        branch_name = getIntent().getExtras().getString("branch_name");
        min_role = getIntent().getExtras().getInt("min_role");
                                // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(true);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {


            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_back);
/*

            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.md_white_1000));
       */     getActionBar().setIcon(drawable);
            getActionBar().setHomeButtonEnabled(true);
        }
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
               case 0: {
                   Fragment fragment = new Section_Students_Unassigned();
                   Bundle args = new Bundle();
                   args.putString("user_id", user_id);
                   args.putString("email", user_email);
                   args.putString("school_id", school_id);
                   args.putString("section_id",section_id);
                   args.putString("branch_id",branch_id);
                   args.putString("grade_id",grade_id);
                   args.putString("section_name",section_name);
                   args.putString("branch_name",branch_name);
                   args.putString("grade_name",grade_name);
                   args.putInt("min_role", min_role);
                   fragment.setArguments(args);
                   return fragment;
               }
                case 1: {
                    Fragment fragment2 = new Section_Students_Assigned();
                    Bundle args2 = new Bundle();
                    args2.putString("user_id", user_id);
                    args2.putString("email", user_email);
                    args2.putString("school_id", school_id);
                    args2.putString("section_id",section_id);
                    args2.putString("branch_id",branch_id);
                    args2.putString("grade_id",grade_id);
                    args2.putString("section_name",section_name);
                    args2.putString("branch_name",branch_name);
                    args2.putString("grade_name",grade_name);
                    args2.putInt("min_role", min_role);
                    fragment2.setArguments(args2);
                    return fragment2;
                }
                default:
                    Fragment fragment = new Section_Students_Unassigned();
                    Bundle args = new Bundle();
                    args.putString("user_id", user_id);
                    args.putString("email", user_email);
                    args.putString("school_id", school_id);
                    args.putString("section_id",section_id);
                    args.putString("branch_id",branch_id);
                    args.putString("grade_id",grade_id);
                    args.putString("section_name",section_name);
                    args.putString("branch_name",branch_name);
                    args.putString("grade_name",grade_name);
                    args.putInt("min_role", min_role);
                    fragment.setArguments(args);
                    return fragment;
            }
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "Unassigned Students";
            }
            return "Assigned Students";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                Intent intent = new Intent(Section_Student_Assign.this,Grade_Section_view.class);
                intent.putExtra("branch_id", getIntent().getExtras().getString("branch_id"));
                intent.putExtra("branch_name", getIntent().getExtras().getString("branch_name"));
                intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                intent.putExtra("school_id", getIntent().getExtras().getString("school_id"));
                intent.putExtra("email", getIntent().getExtras().getString("email"));
                intent.putExtra("grade_id", getIntent().getExtras().getString("grade_id"));
                intent.putExtra("grade_name", getIntent().getExtras().getString("grade_name"));
                intent.putExtra("section_id",getIntent().getExtras().getString("section_id"));
                intent.putExtra("section_name",getIntent().getExtras().getString("section_name"));
                intent.putExtra("min_role",getIntent().getExtras().getInt("min_role"));
                Section_Student_Assign.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Section_Student_Assign.this,Grade_Section_view.class);
        intent.putExtra("branch_id", getIntent().getExtras().getString("branch_id"));
        intent.putExtra("branch_name", getIntent().getExtras().getString("branch_name"));
        intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
        intent.putExtra("school_id", getIntent().getExtras().getString("school_id"));
        intent.putExtra("email", getIntent().getExtras().getString("email"));
        intent.putExtra("grade_id", getIntent().getExtras().getString("grade_id"));
        intent.putExtra("grade_name", getIntent().getExtras().getString("grade_name"));
        intent.putExtra("section_id",getIntent().getExtras().getString("section_id"));
        intent.putExtra("section_name",getIntent().getExtras().getString("section_name"));
        intent.putExtra("min_role",getIntent().getExtras().getInt("min_role"));
        Section_Student_Assign.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
