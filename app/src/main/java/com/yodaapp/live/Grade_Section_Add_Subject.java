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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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


public class Grade_Section_Add_Subject extends Activity {
    String user_id = "";
    String user_email = "";
    String school_id = "";
    String branch_id = "";
    String section_id = "";
    String grade_id = "";
    String grade_name = "";
    String section_name = "";
    String branch_name = "";
    int min_role = 0;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String tag_string_req_category = "string_req_category";
    ProgressDialog progress;
    List<Createadmin> feedslist;
    ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();
    Spinner spnr2;

    ArrayList<String> myarray3 = new ArrayList<>();
    ArrayList<String> myarray4 = new ArrayList<>();
    Spinner spnr3;
    String subject_selected_id = "", subject_head_role_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_section_add_subject);

        user_id = getIntent().getExtras().getString("user_id");
        user_email = getIntent().getExtras().getString("email");
        school_id = getIntent().getExtras().getString("school_id");
        branch_id = getIntent().getExtras().getString("branch_id");
        section_id = getIntent().getExtras().getString("section_id");
        grade_id = getIntent().getExtras().getString("grade_id");
        grade_name = getIntent().getExtras().getString("grade_name");
        section_name = getIntent().getExtras().getString("section_name");
        branch_name = getIntent().getExtras().getString("branch_name");
        min_role = getIntent().getExtras().getInt("min_role");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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

        progress = new ProgressDialog(Grade_Section_Add_Subject.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        myarray.add("Select");
        myarray2.add("Select");
        spinnerfun();
        spnr2 = (Spinner) findViewById(R.id.grade_section_add_subject_teacher_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Grade_Section_Add_Subject.this, android.R.layout.simple_spinner_dropdown_item, myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
       // spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                subject_head_role_id = myarray2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        myarray3.add("Select");
        myarray4.add("Select");
        spinnerfun_subject();
        spnr3 = (Spinner) findViewById(R.id.grade_section_add_subject_subject_spinner);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(Grade_Section_Add_Subject.this, android.R.layout.simple_spinner_dropdown_item, myarray3);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr3.setAdapter(adapter4);
        spnr3.setFocusableInTouchMode(true);

        spnr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray3", myarray3.get(position));
//                Log.d("myaray4", myarray4.get(position));
                subject_selected_id = myarray4.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button b = (Button) findViewById(R.id.grade_section_add_subject_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject_selected_id.equals("")) {
                    Toast.makeText(Grade_Section_Add_Subject.this, getResources().getString(R.string.select_subject_first), Toast.LENGTH_LONG).show();
                } else {
                    if (subject_selected_id.equals("Select")) {
                        Toast.makeText(Grade_Section_Add_Subject.this, getResources().getString(R.string.select_subject), Toast.LENGTH_LONG).show();
                    }
                    else if (subject_head_role_id.equals("Select")) {
                        Toast.makeText(Grade_Section_Add_Subject.this, "Select Teacher", Toast.LENGTH_LONG).show();
                    }

                    else {
                        progress.show();
                        update_data(getResources().getString(R.string.url_reference) + "home/subject_grade_selection.php");
                    }
                }
            }
        });
    }

    void update_data(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progress.hide();
//                        Log.d("response", s);
                        feedslist = CreateadminJSONParsers.parserFeed(s);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(Grade_Section_Add_Subject.this, Grade_Section_view.class);
                                    intent.putExtra("branch_id", branch_id);
                                    intent.putExtra("branch_name", branch_name);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("grade_id", grade_id);
                                    intent.putExtra("grade_name", grade_name);
                                    intent.putExtra("section_id", section_id);
                                    intent.putExtra("section_name", section_name);
                                    intent.putExtra("min_role", min_role);
                                    Grade_Section_Add_Subject.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
//                                else {
//                                    Toast.makeText(Grade_Section_Add_Subject.this, flower.getId(), Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.hide();
//                        Log.d("error", "" + volleyError.getMessage());
                        Toast.makeText(Grade_Section_Add_Subject.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Section_Add_Subject.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Section_Add_Subject.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("school_id", school_id);
                params.put("branch_id", branch_id);
                params.put("section_id", section_id);
                params.put("grade_id", grade_id);
                params.put("subject_selected_id", subject_selected_id);
                params.put("subject_head_role_id", subject_head_role_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_email);
//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_id);
//                Log.d("section_id", section_id);
//                Log.d("grade_id", grade_id);
//                Log.d("subject_selected_id", subject_selected_id);
//                Log.d("subject_head_role_id", subject_head_role_id);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }

    public void spinnerfun() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/branch_master_data.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray.add(category);
                                myarray2.add(id);
//                                Log.d("res", category);
                            }

                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Grade_Section_Add_Subject.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Section_Add_Subject.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Section_Add_Subject.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
//                Log.d("school_id", school_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }


    public void spinnerfun_subject() {
        progress.show();
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/subject_view.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray3.add(category);
                                myarray4.add(id);
//                                Log.d("res", category);
                            }

                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Grade_Section_Add_Subject.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Section_Add_Subject.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Section_Add_Subject.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
//                Log.d("school_id", school_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                Intent intent = new Intent(Grade_Section_Add_Subject.this, Grade_Section_view.class);
                intent.putExtra("branch_id", branch_id);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("grade_id", grade_id);
                intent.putExtra("grade_name", grade_name);
                intent.putExtra("section_id", section_id);
                intent.putExtra("section_name", section_name);
                intent.putExtra("min_role", min_role);
                Grade_Section_Add_Subject.this.finish();
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
        Intent intent = new Intent(Grade_Section_Add_Subject.this, Grade_Section_view.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("section_id", section_id);
        intent.putExtra("section_name", section_name);
        intent.putExtra("min_role", min_role);
        Grade_Section_Add_Subject.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
