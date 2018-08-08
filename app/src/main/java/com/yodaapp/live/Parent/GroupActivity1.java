package com.yodaapp.live.Parent;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yodaapp.live.*;
import com.yodaapp.live.AllGroupsFragment;
import com.yodaapp.live.GroupNameFragment;
import com.yodaapp.live.controller.AppController;

import java.util.HashMap;
import java.util.Map;


public class GroupActivity1 extends Fragment implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    ProgressDialog progress;

    String user_id = "";
    String school_id = "";
    String user_email = "";
    String redirection = "";
    View rootView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.section_student_assign, container, false);
        setHasOptionsMenu(true);
      //  super.onCreate(savedInstanceState);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getChildFragmentManager());

        // Set up the action bar.
        getActivity().getActionBar().removeAllTabs();
        getActivity().invalidateOptionsMenu();
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


        // Set up the action bar.
        final ActionBar actionBar = getActivity().getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        /// Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
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

        Log.v("getcount",mAppSectionsPagerAdapter.getCount()+"");

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();

        return rootView;
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroupActivity1 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yodaapp.live/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroupActivity1 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yodaapp.live/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
                    Fragment fragment = new com.yodaapp.live.Parent.GroupNameFragment();
                    Bundle args = new Bundle();

                    fragment.setArguments(args);
                    return fragment;
                }
                case 1: {
                    Fragment fragment = new com.yodaapp.live.Parent.AllGroupsFragment();
                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    return fragment;
                }
                default:
                    Fragment fragment = new com.yodaapp.live.Parent.GroupNameFragment();
                    Bundle args = new Bundle();
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
            if (position == 0) {
                return "My Groups";
            }
            return "All Groups";
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        inflater.inflate(R.menu.group_menu, menu);
        Drawable drawable = menu.findItem(R.id.create_group).getIcon();
        Drawable drawable1 = menu.findItem(R.id.refresh).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        drawable1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.blue));
        DrawableCompat.setTint(drawable1, ContextCompat.getColor(getActivity(), R.color.blue));
        menu.findItem(R.id.create_group).setIcon(drawable);
        menu.findItem(R.id.refresh).setIcon(drawable1);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void batch_job() {
        progress.show();
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/batch_job_check.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                       /* Intent intent=new Intent(getActivity(),GroupActivity1.class);
                        startActivity(intent);
                        finish();
*/
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
/*

                        Intent intent=new Intent(GroupActivity1.this,GroupActivity1.class);
                        startActivity(intent);
                        finish();
*/


                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",StaticVariable.user_id);
                params.put("school_id",StaticVariable.school_id);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
              //  onBack();
                return true;
            }
            case R.id.refresh: {

                batch_job();

                return true;
            }
            case R.id.create_group: {
try {

    Integer roleId = Integer.parseInt(StaticVariable.role_id);

    if (roleId <= 7) {
        Intent intent = new Intent(getActivity(), com.yodaapp.live.Parent.MembersSelectActivity.class);
        intent.putExtra("role","parent");
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }
    else {
        Toast.makeText(getActivity(), "You dont have enough permission to create group", Toast.LENGTH_LONG).show();

    }
    }catch(Exception e){
    Toast.makeText(getActivity(), getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();

    e.printStackTrace();
    }


                return true;
            }
            default:
                return true;
        }
    }
}
