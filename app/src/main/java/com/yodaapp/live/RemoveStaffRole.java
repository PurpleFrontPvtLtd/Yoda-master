package com.yodaapp.live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yodaapp.live.adapter.DisplayAdapter2;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.RemoveRoleAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Remove_role_model;
import com.yodaapp.live.model.Staff_Details_Model;
import com.yodaapp.live.parsers.IndividualContactsListParser1;
import com.yodaapp.live.parsers.RemoveStaffRolesParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveStaffRole extends Activity {
    ExpandableListView listview;

    List<Remove_role_model> feedslist;

    ArrayList<String> selectedContactList = new ArrayList<>();
    ArrayList<String> selectedScopeList = new ArrayList<>();
    ArrayList<String> selectedRolesList = new ArrayList<>();
    ProgressDialog progress;
    String owners_json="";

    Activity thisActivity=this;
    int min_role = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_staff_role);

        listview = (ExpandableListView) findViewById(R.id.lv);
        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            owners_json = getIntent().getExtras().getString("owners_json");
            min_role = getIntent().getExtras().getInt("min_role");

        }catch (Exception e){
            e.printStackTrace();
        }


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

        Button removeRole=(Button)findViewById(R.id.remove_role);
        removeRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedContactList.clear();
                for (int i = 0; i < feedslist.size(); i++) {

//                        updatedSelectIdList.add(groupFeedlist.get(i).getChecked() + "");

                    if (feedslist.get(i).getChecked() == true) {
                        selectedContactList.add(feedslist.get(i).getId());
                        selectedRolesList.add(feedslist.get(i).getRole_id());
                    }
                }

                Log.v("list",selectedContactList.toString());
                Log.v("role_list",selectedRolesList.toString());
                if (selectedContactList.isEmpty()){
                    Toast.makeText(thisActivity,"Please select atleast one contact",Toast.LENGTH_SHORT).show();
                }

                else {
                    removeStaffsRole();
                }
            }
        });

        if (isonline()) {
            // progress.show();
            // updatedata(getResources().getString(R.string.url_reference) + "home/staff_details.php");
            staffRoles();

        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }    }
    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
    public void removeStaffsRole() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/remove_admin_staff.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response1", response);
                        Intent intent = new Intent(thisActivity, AdminLists.class);
                        intent.putExtra("owners_json", owners_json);
                        intent.putExtra("min_role", min_role);
                        thisActivity.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {

                Gson gson=new Gson();
                String contactId=gson.toJson(selectedContactList);
                String roleids=gson.toJson(selectedRolesList);

                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_ids", contactId);
                params.put("user_role_ids", roleids);

                Log.v("contactId",contactId+","+roleids);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }
    public void staffRoles() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/admin_staffs.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response1", response);


                        feedslist= RemoveStaffRolesParser.parserFeed(response);

                        listview.setAdapter(new RemoveRoleAdapter(thisActivity,feedslist));
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

                case android.R.id.home:
            Intent intent = new Intent(thisActivity, AdminLists.class);
            intent.putExtra("owners_json", owners_json);
            intent.putExtra("min_role", min_role);
            thisActivity.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;

            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(thisActivity, AdminLists.class);
        intent.putExtra("owners_json", owners_json);
        intent.putExtra("min_role", min_role);
        thisActivity.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
