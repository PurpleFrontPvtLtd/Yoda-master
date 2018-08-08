package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignAdminRole extends Activity {
    Spinner spnr2,spnr_role,spnr_branch,spnr_grade,spnr_section,spnr_subject;
    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();
    String staff_id_set = "", role_id_set = "",branch_id_set = "";

    ArrayList<String> myarray_role= new ArrayList<>();
    ArrayList<String> myarray2_role= new ArrayList<>();
    List<Createadmin> feedslist;

    ProgressDialog progress;
    String owners_json="";

    Activity thisActivity=this;
    int min_role = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_admin_role);

        spnr2=(Spinner) findViewById(R.id.role_assign_staff);
         //spnr2.setFocusableInTouchMode(true);
        myarray.add("Select");
        myarray2.add("Select");

        myarray_role.add("Select");
        myarray2_role.add("Select");


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


        progress = new ProgressDialog(AssignAdminRole.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        spinnerfun();
        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                if (myarray.get(position).equals("Select") || myarray.get(position).equals("")) {
                    staff_id_set = "";
                    role_id_set = "";
                    branch_id_set = "";
                        } else {
                    staff_id_set = myarray2.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                staff_id_set = "";
                role_id_set = "";
                 }
        });

        spnr_role=(Spinner) findViewById(R.id.role_assign_role);
            //spnr_role.setFocusableInTouchMode(true);
        spinnerfun_role();

        spnr_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_role.get(position));
//                Log.d("myaray2", myarray2_role.get(position));
                if (myarray_role.get(position).equals("Select") || myarray_role.get(position).equals("")) {
                    role_id_set = "";
                    branch_id_set = "";
                      } else {
                    role_id_set = myarray2_role.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                role_id_set = "";
                branch_id_set = "";
                 }
        });

        try {
            owners_json = getIntent().getExtras().getString("owners_json");
            min_role = getIntent().getExtras().getInt("min_role");

        }catch (Exception e){
            e.printStackTrace();
        }

            Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isonline()) {

                    if (spnr2.getSelectedItem().toString().contains("Select")) {

                        Toast.makeText(AssignAdminRole.this, "Please select staff", Toast.LENGTH_LONG).show();

                    } else if (spnr_role.getSelectedItem().toString().contains("Select")) {
                        Toast.makeText(AssignAdminRole.this, "Please select Role", Toast.LENGTH_LONG).show();

                    } else
                        progress.show();
                    updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");
                }
            }
        });


    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    void updateonlinedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
                        feedslist = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AssignAdminRole.this);
                                    builder.setMessage(getResources().getString(R.string.role_assign_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Intent intent = new Intent(AssignAdminRole.this, AdminLists.class);
                                                    intent.putExtra("owners_json", owners_json);
                                                    intent.putExtra("min_role", min_role);
                                                    AssignAdminRole.this.finish();
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(Role_Assign.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(AssignAdminRole.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AssignAdminRole.this, SplashScreen.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        AssignAdminRole.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id",StaticVariable.school_id);
                params.put("user_id",StaticVariable.user_id);
                params.put("user_email",StaticVariable.email);
                if(!role_id_set.equals("")) {
                    params.put("role_id",role_id_set);
//                    Log.d("role_id", role_id_set);

                    if(!staff_id_set.equals("")) {
                        params.put("staff_id",staff_id_set);
//                        Log.d("staff_id", staff_id_set);


                    }
                }



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }
    public void spinnerfun() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_master_data.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        // progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray.add(category);
                                myarray2.add(id);
//                                Log.d("res", category);
                            }
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(AssignAdminRole.this,android.R.layout.simple_spinner_dropdown_item,myarray);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr2.setAdapter(adapter3);

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(AssignAdminRole.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AssignAdminRole.this, SplashScreen.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        AssignAdminRole.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    public void spinnerfun_role() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/role_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< 3; i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("role");
                                myarray_role.add(category);
                                myarray2_role.add(id);
//                                Log.d("res", category);
                            }
                            ArrayAdapter<String> adapter3_role=new ArrayAdapter<>(AssignAdminRole.this,android.R.layout.simple_spinner_dropdown_item,myarray_role);
                            adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_role.setAdapter(adapter3_role);

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(AssignAdminRole.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AssignAdminRole.this, SplashScreen.class);
                        intent.putExtra("user_id", StaticVariable.user_id);

                        AssignAdminRole.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AssignAdminRole.this, AdminLists.class);
                intent.putExtra("owners_json", owners_json);
                intent.putExtra("min_role", min_role);
                AssignAdminRole.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;

            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AssignAdminRole.this, AdminLists.class);
        intent.putExtra("owners_json", owners_json);
        intent.putExtra("min_role", min_role);
        AssignAdminRole.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}
