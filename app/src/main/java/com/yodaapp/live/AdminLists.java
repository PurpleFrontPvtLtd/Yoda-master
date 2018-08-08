package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.HomePageList;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.MainPage_Model;
import com.yodaapp.live.parsers.MainPage_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminLists extends Activity {
    ExpandableListView owners_list,principal_list,admin_list,subject_list;

    String owners_json="";
    ArrayList<String> allstaffList=new ArrayList<>();
    ArrayList<String> allStaffIdlist=new ArrayList<>();
    ArrayList<String> princpalIdList=new ArrayList<>();
    ArrayList<String> principalList=new ArrayList<>();
    ArrayList<String> adminList=new ArrayList<>();
    ArrayList<String> adminsIdlist=new ArrayList<>();
    ArrayList<String> ownersList=new ArrayList<>();
    ArrayList<String> ownersId=new ArrayList<>();
    Activity thisActivity=this;
    int min_role = 0;
    List<MainPage_Model> feedslist;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lists);

        owners_list=(ExpandableListView)findViewById(R.id.owners_list);
        principal_list=(ExpandableListView)findViewById(R.id.principal_list);
        admin_list=(ExpandableListView)findViewById(R.id.admin_list);

        LinearLayout addmin=(LinearLayout)findViewById(R.id.add_admin);
        LinearLayout removeAdmin=(LinearLayout)findViewById(R.id.remove_admin);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        progress.show();
        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");

        addmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                owners_json=getIntent().getExtras().getString("owners_json");
                min_role = getIntent().getExtras().getInt("min_role");
                StaticVariable.roleId=min_role;
                Intent intent=new Intent(thisActivity,AssignAdminRole.class);
                intent.putExtra("owners_json",owners_json);
                intent.putExtra("min_role",min_role);
                startActivity(intent);
                finish();

            }
        });

        removeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  owners_json=getIntent().getExtras().getString("owners_json");
                min_role = getIntent().getExtras().getInt("min_role");
                StaticVariable.roleId=min_role;
                Intent intent=new Intent(thisActivity,RemoveStaffRole.class);
                intent.putExtra("owners_json",owners_json);
                intent.putExtra("min_role",min_role);
                startActivity(intent);
                finish();
            }
        });


        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }


        try {
//            owners_json=getIntent().getExtras().getString("owners_json");
            min_role = getIntent().getExtras().getInt("min_role");

            JSONArray ar = new JSONArray(owners_json);
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                String staff_id = obj.getString("id");
                String staff_name = obj.getString("name");
                String role_id = obj.getString("role_id");

                allStaffIdlist.add(staff_id);
                allstaffList.add(staff_name);

                if (role_id.equals("1")) {
                    ownersList.add(staff_name);
                }
                else if (role_id.equals("2")){
                    principalList.add(staff_name);
                }
                else if (role_id.equals("3")){
                    adminList.add(staff_name);

                }
            }


            if (min_role == 0 || min_role >=4) {

                addmin.setVisibility(View.GONE);
                removeAdmin.setVisibility(View.GONE);
                //Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
            }
            else {
                addmin.setVisibility(View.VISIBLE);
                removeAdmin.setVisibility(View.VISIBLE);

            }


        } catch (JSONException e) {
//                        e.printStackTrace();
        }



    }
    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response1", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = MainPage_JSONParser.parserFeed(arg0);
                        updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progress.show();
                                        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                                        thisActivity.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("user_email", StaticVariable.email);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                Intent intent = new Intent(AdminLists.this, MainActivity.class);
                intent.putExtra("user_id", StaticVariable.user_id);
                intent.putExtra("school_id", StaticVariable.school_id);
                intent.putExtra("email", "a");
                startActivity(intent);
                AdminLists.this.finish();

                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;

            default:
                return true;
        }
    }
    void updatedisplay() {
        if (feedslist != null) {
            try {
                for (final MainPage_Model flower : feedslist) {


                    owners_json = flower.getOwners();
                    try {
                        JSONArray ar = new JSONArray(owners_json);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            String staff_id = obj.getString("id");
                            String staff_name = obj.getString("name");
                            String role_id = obj.getString("role_id");

                            allStaffIdlist.add(staff_id);
                            allstaffList.add(staff_name);

                            if (role_id.equals("1")) {
                                ownersList.add(staff_name);
                            }
                            else if (role_id.equals("2")){
                                principalList.add(staff_name);
                            }
                            else if (role_id.equals("3")){
                                adminList.add(staff_name);

                            }

                        }


                        owners_list.setAdapter(new HomePageList(thisActivity,ownersList));
                        principal_list.setAdapter(new HomePageList(thisActivity,principalList));
                        admin_list.setAdapter(new HomePageList(thisActivity,adminList));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(AdminLists.this, MainActivity.class);
        intent.putExtra("user_id", StaticVariable.user_id);
        intent.putExtra("school_id", StaticVariable.school_id);
        intent.putExtra("email", "a");

        startActivity(intent);
        AdminLists.this.finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
