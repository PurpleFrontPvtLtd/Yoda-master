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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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


public class Role_Assign extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    List<Createadmin> feedslist;
    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();

    ArrayList<String> myarray_role= new ArrayList<>();
    ArrayList<String> myarray2_role= new ArrayList<>();

    ArrayList<String> myarray_branch = new ArrayList<>();
    ArrayList<String> myarray2_branch = new ArrayList<>();

    ArrayList<String> myarray_section = new ArrayList<>();
    ArrayList<String> myarray2_section = new ArrayList<>();

    ArrayList<String> myarray_subject = new ArrayList<>();
    ArrayList<String> myarray2_subject = new ArrayList<>();

    ArrayList<String> myarray_grade= new ArrayList<>();
    ArrayList<String> myarray2_grade = new ArrayList<>();
    String tag_string_req_category = "string_req_category";

    String tag_string_req_category2 = "string_req_category_role";

    String tag_string_req_category3 = "string_req_category_branch";

    String tag_string_req_category4 = "string_req_category_grade";

    String tag_string_req_category5 = "string_req_category_section";

    String tag_string_req_category6 = "string_req_category_subject";

    Spinner spnr_role,spnr_branch,spnr_grade,spnr_section,spnr_subject;
    String staff_id_set = "", role_id_set = "",branch_id_set = "";
    String grade_id_set = "";
    String Section_id_set = "";
    String subject_id_set = "";
    String staff_name = "",scopeId="",staff_email = "",staff_phone = "",staff_password = "",staff_confirm_password = "", staff_userid = "",redirection="";
    Activity thisActivity=this;
    TextView tv_branch,tv_grade,tv_section,tv_subject;
    LinearLayout sp_branch,sp_grade,sp_section,sp_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_assign1);

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

        progress = new ProgressDialog(Role_Assign.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

       try {
           school_id=StaticVariable.school_id;
           user_id=StaticVariable.user_id;
           user_email=StaticVariable.email;
           staff_userid = getIntent().getExtras().getString("staff_id");
            redirection = getIntent().getExtras().getString("redirection");
            staff_name = getIntent().getExtras().getString("staff_name");
            staff_phone = getIntent().getExtras().getString("staff_phone");

            staff_password = getIntent().getExtras().getString("staff_password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_branch=(TextView) findViewById(R.id.tv_branch);
        tv_grade=(TextView) findViewById(R.id.tv_grade);
        tv_section=(TextView) findViewById(R.id.tv_section);
        tv_subject=(TextView) findViewById(R.id.tv_subject);
        sp_branch=(LinearLayout)findViewById(R.id.sp_select_branch);
        sp_grade=(LinearLayout)findViewById(R.id.sp_select_grade);
        sp_section=(LinearLayout)findViewById(R.id.sp_select_section);
        sp_subject=(LinearLayout)findViewById(R.id.sp_select_subject);


        myarray.add("Select");
        myarray2.add("Select");

        myarray_role.add("Select");
        myarray2_role.add("Select");

        myarray_branch.add("Select");
        myarray2_branch.add("Select");

        myarray_grade.add("Select");
        myarray2_grade.add("Select");

        myarray_section.add("Select");
        myarray2_section.add("Select");

        myarray_subject.add("Select");
        myarray2_subject.add("Select");
             ArrayAdapter<String> adapter3=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        progress.show();
        spinnerfun_role();
        spnr_role=(Spinner) findViewById(R.id.role_assign_role);
        ArrayAdapter<String> adapter3_role=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_role);
        adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_role.setAdapter(adapter3_role);
        //spnr_role.setFocusableInTouchMode(true);

        spnr_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_role.get(position));
//                Log.d("myaray2", myarray2_role.get(position));
                if (myarray_role.get(position).equals("Select") || myarray_role.get(position).equals("")) {
                    role_id_set = "";
                    branch_id_set = "";
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    role_id_set = myarray2_role.get(position);
                }



                if (spnr_role.getSelectedItem().toString().contains("Owner")||spnr_role.getSelectedItem().toString().contains("Principal")||spnr_role.getSelectedItem().toString().contains("Administrator")){
                    scopeId="1";
                    sp_branch.setVisibility(View.GONE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);
                    sp_subject.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.GONE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    tv_subject.setVisibility(View.GONE);
                }
                else if (spnr_role.getSelectedItem().toString().contains("Branch Head")){
                    scopeId="2";
                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);
                    sp_subject.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    tv_subject.setVisibility(View.GONE);

                } else if (spnr_role.getSelectedItem().toString().contains("Grade Head")){
                    scopeId="3";

                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.VISIBLE);
                    sp_section.setVisibility(View.GONE);
                    sp_subject.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.VISIBLE);
                    tv_subject.setVisibility(View.GONE);

                }
                else if (spnr_role.getSelectedItem().toString().contains("Section Head")){
                    scopeId="4";

                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.VISIBLE);
                    sp_section.setVisibility(View.VISIBLE);
                    sp_subject.setVisibility(View.GONE);
                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    tv_subject.setVisibility(View.GONE);

                }
                else if (spnr_role.getSelectedItem().toString().contains("Teacher")){
                    scopeId="4";
                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.VISIBLE);
                    sp_section.setVisibility(View.VISIBLE);
                    sp_subject.setVisibility(View.VISIBLE);
                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    tv_subject.setVisibility(View.VISIBLE);

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                role_id_set = "";
                branch_id_set = "";
                grade_id_set = "";
                Section_id_set = "";
                subject_id_set = "";
            }
        });

       // progress.show();
        spinnerfun_branch();
        spnr_branch=(Spinner) findViewById(R.id.role_assign_branch);
        ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
        adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_branch.setAdapter(adapter3_branch);
       // spnr_branch.setFocusableInTouchMode(true);

        spnr_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_branch.get(position));
//                Log.d("myaray2", myarray2_branch.get(position));
                if (!spnr_branch.getSelectedItem().toString().contains("Select")) {
                    progress.show();
                }
                spinnerfun_grade(myarray2_branch.get(position));
                if(myarray_branch.get(position).equals("Select") || myarray_branch.get(position).equals("")) {
               //     spnr_grade.setVisibility(View.GONE);
               //     spnr_section.setVisibility(View.GONE);
                //    spnr_subject.setVisibility(View.GONE);
                } else
                {
                    spnr_grade.setVisibility(View.VISIBLE);
                }
                if(myarray_branch.get(position).equals("Select") || myarray_branch.get(position).equals("")) {
                    branch_id_set = "";
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    branch_id_set = myarray2_branch.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                branch_id_set = "";
                grade_id_set = "";
                Section_id_set = "";
                subject_id_set = "";
            }
        });


        spnr_grade=(Spinner) findViewById(R.id.role_assign_grade);
        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);
        //spnr_grade.setFocusableInTouchMode(true);

        spnr_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_grade.get(position));
//                Log.d("myaray2", myarray2_grade.get(position));


                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                  //  spnr_section.setVisibility(View.GONE);
                   // spnr_subject.setVisibility(View.GONE);
                } else
                {
                    spnr_section.setVisibility(View.VISIBLE);
                }

                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    grade_id_set = myarray2_grade.get(position);
                    progress.show();
                    int pos = spnr_branch.getSelectedItemPosition();
                    spinnerfun_section(myarray2_branch.get(pos), myarray2_grade.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                grade_id_set = "";
                Section_id_set = "";
                subject_id_set = "";
            }
        });



        spnr_section =(Spinner) findViewById(R.id.role_assign_section);
        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_section);
        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_section.setAdapter(adapter3_section);
       // spnr_section.setFocusableInTouchMode(true);

        spnr_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));
                if (myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                  //  spnr_subject.setVisibility(View.GONE);
                } else {
                    spnr_subject.setVisibility(View.VISIBLE);
                }
                if (myarray_section.get(position).equals("Select") || myarray_section.get(position).equals("")) {
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    Section_id_set = myarray2_section.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Section_id_set = "";
                subject_id_set = "";
            }
        });

        progress.show();
        spinnerfun_subject();
        spnr_subject=(Spinner) findViewById(R.id.role_assign_subject);
        ArrayAdapter<String> adapter3_subject=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_subject);
        adapter3_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_subject.setAdapter(adapter3_subject);
        //spnr_subject.setFocusableInTouchMode(true);

        spnr_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_subject.get(position));
//                Log.d("myaray2", myarray2_subject.get(position));
                if (myarray_subject.get(position).equals("Select") || myarray_subject.get(position).equals("")) {
                    subject_id_set = "";
                } else {
                    subject_id_set = myarray2_subject.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subject_id_set = "";
            }
        });

        Button b = (Button) findViewById(R.id.role_assign_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isonline()) {
                    String group_type=spnr_role.getSelectedItem().toString();
                    String branch_type=spnr_branch.getSelectedItem().toString();
                    String grade_type=spnr_grade.getSelectedItem().toString();
                    String section_type=spnr_section.getSelectedItem().toString();
                    String subject_type=spnr_subject.getSelectedItem().toString();

                    if (spnr_role.getSelectedItem().toString().contains("Select")) {
                        Toast.makeText(Role_Assign.this, "Please select Role", Toast.LENGTH_LONG).show();

                    }
                    else   if (spnr_role.getSelectedItem().toString().contains("Owner")||spnr_role.getSelectedItem().toString().contains("Principal")||spnr_role.getSelectedItem().toString().contains("Administrator")) {

                        progress.show();

                        //  createStaffRole(getResources().getString(R.string.url_reference) + "home/staff_creation1.php");
                        updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");

                    }
                        else if (group_type.contains("Branch Head")){
                        if (branch_type.equals("")||branch_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                        }

                        else {

                            progress.show();

                            //  createStaffRole(getResources().getString(R.string.url_reference) + "home/staff_creation1.php");
                            updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");
                        }


                    }

                    else if (group_type.contains("Grade Head")){
                        if (branch_type.equals("")||branch_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                        }
                        else  if (grade_type.equals("")||grade_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select grade",Toast.LENGTH_SHORT).show();

                        }
                        else {

                            progress.show();

                            //  createStaffRole(getResources().getString(R.string.url_reference) + "home/staff_creation1.php");
                            updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");
                        }


                    }
                    else if (group_type.contains("Section Head")){
                        if (branch_type.equals("")||branch_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                        }
                        else  if (grade_type.equals("")||grade_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select grade",Toast.LENGTH_SHORT).show();

                        }
                        else  if (section_type.equals("")||section_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select section",Toast.LENGTH_SHORT).show();

                        }
                        else {


                            progress.show();

                            //  createStaffRole(getResources().getString(R.string.url_reference) + "home/staff_creation1.php");
                            updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");
                            // Toast.makeText(thisActivity,"Succss",Toast.LENGTH_SHORT).show();

                        }


                    }



                    else if (group_type.contains("Teacher")){
                        if (branch_type.equals("")||branch_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                        }
                        else  if (grade_type.equals("")||grade_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select grade",Toast.LENGTH_SHORT).show();

                        }
                        else  if (section_type.equals("")||section_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select section",Toast.LENGTH_SHORT).show();

                        }
                        else  if (subject_type.equals("")||subject_type.equals("Select")) {
                            Toast.makeText(thisActivity,"Please select subject",Toast.LENGTH_SHORT).show();

                        }
                        else {


                            progress.show();

                            //  createStaffRole(getResources().getString(R.string.url_reference) + "home/staff_creation1.php");
                            updateonlinedata(getResources().getString(R.string.url_reference) + "home/role_assign.php");
                            // Toast.makeText(thisActivity,"Succss",Toast.LENGTH_SHORT).show();

                        }


                    }



                }

            }
        });

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
//                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray);
//                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spnr2.setAdapter(adapter3);

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
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
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

                            if (redirection.equals("staff_details")){
                                // for show only first 3 role to assign role


                            for(int i=0; i< 4; i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("role");

                                myarray_role.add(category);
                                myarray2_role.add(id);
//                                Log.d("res", category);
                            }

                            }
                            else {

                                for(int i=0; i< ar.length(); i++)
                                {
                                    JSONObject obj = ar.getJSONObject(i);
                                    String id = obj.getString("id");
                                    String category = obj.getString("role");

                                    myarray_role.add(category);
                                    myarray2_role.add(id);
//                                Log.d("res", category);
                                }
                            }



                            ArrayAdapter<String> adapter3_role=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_role);
                            adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_role.setAdapter(adapter3_role);


                            if (redirection.equals("staff_details")){
                               // for show only first 3 role to assign role
                                for (int i=4;i<=7;i++) {
                                    myarray_role.remove(i);
                                    myarray2_role.remove(i);
                                }
                            }



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
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category2);
    }

    public void spinnerfun_branch() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("branch_name");
                                myarray_branch.add(category);
                                myarray2_branch.add(id);
//                                Log.d("res", category);
                            }

                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_branch.setAdapter(adapter3_branch);
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
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void spinnerfun_subject() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/subject_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("subject");
                                myarray_subject.add(category);
                                myarray2_subject.add(id);
//                                Log.d("res", category);
                            }

                            ArrayAdapter<String> adapter3_subject=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_subject);
                            adapter3_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_subject.setAdapter(adapter3_subject);
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
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category4);
    }

    public void spinnerfun_grade(final String branch_idd) {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/grade_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_grade.clear();
                        myarray2_grade.clear();
                        myarray_grade.add("Select");
                        myarray2_grade.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray_grade.add(category);
                                myarray2_grade.add(id);
//                                Log.d("res", category);
                            }


                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
                        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_grade.setAdapter(adapter3_grade);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("branch_id", branch_idd);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category5);
    }

    public void spinnerfun_section(final String branch, final String grade) {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/section_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_section.clear();
                        myarray2_section.clear();
                        myarray_section.add("Select");
                        myarray2_section.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("section");
                                myarray_section.add(category);
                                myarray2_section.add(id);
//                                Log.d("res", category);
                            }

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(Role_Assign.this,android.R.layout.simple_spinner_dropdown_item,myarray_section);
                        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_section.setAdapter(adapter3_section);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("grade_id", grade);
                params.put("branch_id", branch);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category6);
    }

    void createStaffRole(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(thisActivity, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    thisActivity.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                } else if(flower.getSucess().equals("existing userid")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.userid_existing_message), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(thisActivity, flower.getId(), Toast.LENGTH_LONG).show();
                                }
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
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Staff_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        thisActivity.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("staff_name", staff_name);
                params.put("staff_email", staff_email);
                params.put("staff_phone", staff_phone);
                params.put("staff_password", staff_password);
                params.put("staff_id",staff_userid);
                if(!role_id_set.equals("")) {
                    params.put("role_id",role_id_set);
//                    Log.d("role_id", role_id_set);


                        if(!branch_id_set.equals("")) {
                            params.put("branch_id",branch_id_set);
//                            Log.d("branch_id", branch_id_set);

                            if(!grade_id_set.equals("")) {
                                params.put("grade_id",grade_id_set);
//                                Log.d("grade_id", grade_id_set);

                                if(!Section_id_set.equals("")) {
                                    params.put("section_id",Section_id_set);
//                                    Log.d("section_id", Section_id_set);

                                    if(!subject_id_set.equals("")) {
                                        params.put("subject_id",subject_id_set);
//                                        Log.d("subject_id", subject_id_set);
                                    }
                                }
                            }
                        }

                }

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Role_Assign.this);
                                    builder.setMessage(getResources().getString(R.string.role_assign_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Intent intent = new Intent(Role_Assign.this, MainActivity.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("email", user_email);
                                                    Role_Assign.this.finish();
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
                        Toast.makeText(Role_Assign.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Role_Assign.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Role_Assign.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id",school_id);
                params.put("user_id",user_id);
                params.put("user_email",user_email);
                params.put("scope_id",scopeId);
                if(!role_id_set.equals("")) {
                    params.put("role_id",role_id_set);
//                    Log.d("role_id", role_id_set);

                    if(!staff_userid.equals("")) {
                        params.put("staff_id",staff_userid);
//                        Log.d("staff_id", staff_id_set);

                        if(!branch_id_set.equals("")) {
                            params.put("branch_id",branch_id_set);
//                            Log.d("branch_id", branch_id_set);

                            if(!grade_id_set.equals("")) {
                                params.put("grade_id",grade_id_set);
//                                Log.d("grade_id", grade_id_set);

                                if(!Section_id_set.equals("")) {
                                    params.put("section_id",Section_id_set);
//                                    Log.d("section_id", Section_id_set);

                                    if(!subject_id_set.equals("")) {
                                        params.put("subject_id",subject_id_set);
//                                        Log.d("subject_id", subject_id_set);
                                    }
                                }
                            }
                        }
                    }
                }



                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category6);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (redirection.equals("staff_details")){
         //   super.onBackPressed();
            Intent intent = new Intent(Role_Assign.this, Staff_Details.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            intent.putExtra("staff_id", staff_userid);
            Role_Assign.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
        else {
            Toast.makeText(thisActivity, getString(R.string.role_back), Toast.LENGTH_SHORT).show();
        }
      /*  super.onBackPressed();
        Intent intent = new Intent(Role_Assign.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        Role_Assign.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);*/
    }
}
