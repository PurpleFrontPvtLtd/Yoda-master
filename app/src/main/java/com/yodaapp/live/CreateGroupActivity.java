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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.ParentDetail_Model;
import com.yodaapp.live.model.Staff_Details_Model;
import com.yodaapp.live.parsers.Staff_Details_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends Activity {
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    int min_role = 0;
    String staff_id = "";
    List<Staff_Details_Model> feedslist;
    List<ParentDetail_Model> feedslist22=new ArrayList<>();
    Button inactivate_staff;
    Activity thisActivity=this;
    ExpandableListView listview;
    LinearLayout layout;
    ArrayList<String> roleList=new ArrayList<>();
    ArrayList<String> roleList1=new ArrayList<>();

    ArrayList<String> myarray_branch = new ArrayList<>();
    ArrayList<String> myarray2_branch = new ArrayList<>();

    ArrayList<String> myarray_section = new ArrayList<>();
    ArrayList<String> myarray2_section = new ArrayList<>();

    ArrayList<String> myarray_subject = new ArrayList<>();
    ArrayList<String> myarray2_subject = new ArrayList<>();

    ArrayList<String> myarray_grade= new ArrayList<>();
    ArrayList<String> myarray2_grade = new ArrayList<>();
    Spinner spnr2,spnr_role,spnr_branch,spnr_grade,spnr_section,spnr_members;
    String status="";
    String staff_id_set = "", role_id_set = "",branch_id_set = "";
    String school_id = "",user_id = "",user_email = "";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "",redirection="";
    String selection_branch_id="";
    String grade_id_set = "";
    String Section_id_set = "";
    String subject_id_set = "";

    String tag_string_req_category3 = "string_req_category_branch";

    String tag_string_req_category4 = "string_req_category_grade";

    String tag_string_req_category5 = "string_req_category_section";

    String tag_string_req_category6 = "string_req_category_subject";

    String staff_branch,staff_section,staff_grade,userRole="";
    EditText group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        progress = new ProgressDialog(thisActivity);
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


        if (isonline()) {
            progress.show();
            updatedata(getResources().getString(R.string.url_reference) + "home/staff_details.php");
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
        spnr_members=(Spinner) findViewById(R.id.role_assign_branch);
        spnr_branch=(Spinner) findViewById(R.id.select_branch);
        group_name=(EditText) findViewById(R.id.group_name);

        spnr_grade=(Spinner) findViewById(R.id.spnr_grade);
        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);


        myarray_branch.add("Select");
        myarray2_branch.add("Select");

        myarray_grade.add("Select");
        myarray2_grade.add("Select");

        myarray_section.add("Select");
        myarray2_section.add("Select");

        myarray_subject.add("Select");
        myarray2_subject.add("Select");

        final Button next=(Button)findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  String groupName=group_name.getText().toString();


                    Intent intent = new Intent(thisActivity, MembersSelectActivity.class);
                    intent.putExtra("staff_section", staff_section);
                    intent.putExtra("staff_grade", staff_grade);
                    intent.putExtra("staff_branch", staff_branch);
                 //   intent.putExtra("group_name", groupName);
                   // intent.putExtra("role", spnr_members.getSelectedItem().toString());
                    intent.putExtra("role", userRole);

                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    startActivity(intent);
                    finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


            }
        });

/*

        spnr_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_grade.get(position));
//                Log.d("myaray2", myarray2_grade.get(position));

                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                    // spnr_section.setVisibility(View.GONE);


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
                    if (status.contains("clear")) {
                        progress.show();
                    }                    int pos = spnr_branch.getSelectedItemPosition();
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



        spnr_section =(Spinner) findViewById(R.id.select_section);
        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_section);
        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_section.setAdapter(adapter3_section);
        // spnr_section.setFocusableInTouchMode(true);



        spnr_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));

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
*/

         spnr_members.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));

                try {
                     staff_branch = feedslist22.get(position).getBranch();
                     staff_grade = feedslist22.get(position).getGrade();
                     staff_section = feedslist22.get(position).getSection();
                    userRole=feedslist22.get(position).getRole();
              //      spinnerfun_branch();




                    Log.d("branch", staff_branch);
                    Log.d("grade", staff_grade);
                    Log.d("section", staff_section);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (spnr_members.getSelectedItem().toString().contains("Branch Head")){

                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


/*
        spnr_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_branch.get(position));
//                Log.d("myaray2", myarray2_branch.get(position));
                spnr_section.setSelection(0);
                if (status.contains("clear")) {
                    progress.show();
                }
                spinnerfun_grade(myarray2_branch.get(position));
                if(myarray_branch.get(position).equals("Select") || myarray_branch.get(position).equals("")) {
                    //  spnr_grade.setVisibility(View.GONE);
                    //  spnr_section.setVisibility(View.GONE);
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
        });*/


    }

    public void spinnerfun_grade(final String branch_idd) {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/grade_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        if (status.contains("clear")) {
                            progress.hide();
                        }
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
                        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
                        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_grade.setAdapter(adapter3_grade);


                        try {
                            int i = -1;
                            for (String id : myarray_grade) {
                                i++;
                                if (id.equals(staff_grade)) {
                                    spnr_grade.setSelection(i);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            spnr_grade.setSelection(0);
                        }


                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        thisActivity.finish();
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
                        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_section);
                        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_section.setAdapter(adapter3_section);

                      try {

                            int i = -1;
                            for (String section : myarray_section) {
                                i++;
                                if (section.equals(staff_section)) {
                                    spnr_section.setSelection(i);
                                    status = "clear";
                                }
                            }
                        }catch (Exception e){
                          e.printStackTrace();
                          spnr_section.setSelection(0);

                      }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        thisActivity.finish();
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
    public void spinnerfun_branch() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);

                        myarray_branch.clear();
                        myarray2_branch.clear();
                        myarray_branch.add("Select");
                        myarray2_branch.add("Select");

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

                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_branch.setAdapter(adapter3_branch);

                            int i = -1;
                            for (String branch1 : myarray_branch) {
                                i++;
                                if (branch1.equals(staff_branch)) {
                                    spnr_branch.setSelection(i);

                                }
                            }
                            //  spnr_branch.setSelection(1);
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
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        thisActivity.finish();
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

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    void updatedata(final String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response",arg0);
                        feedslist = Staff_Details_JSONParser.parserFeed(arg0);

                        String branch_grade = feedslist.get(0).getDetails();
                        try {
                            JSONArray ar = new JSONArray(branch_grade);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String role = obj.getString("role");
                                String branch_name = obj.getString("branch_name");
                                String grade = obj.getString("grade");
                                String section = obj.getString("section");
                                String subject = obj.getString("subject");

                              /*  String role1=obj.getString("role");
                                if (role1.equals("Branch Head")){
                                    role1=role1+" - Bodhi Kidds-"+branch_name;
                                }
                                if (role1.equals("Grade Head")){
                                    role1=role1+" - Bodhi Kidds-"+branch_name+"-"+grade;
                                }
                                if (role1.equals("Section Head")){
                                    role1=role1+" - Bodhi Kidds-"+branch_name+"-"+grade+"-"+section;
                                }
                                if (role1.equals("Owner")||role1.equals("Principal")||role1.equals("Administrator")){
                                    role1=role1+" - Bodhi Kidds";
                                }
                                if (role1.equals("Teacher")){
                                    role1=role1+" - Bodhi Kidds-"+branch_name+"-"+grade+"-"+section;
                                }*/


                                String role1=obj.getString("role");
                                if (role1.equals("Branch Head")){
                                    role1=role1+" -"+branch_name;
                                }
                                if (role1.equals("Grade Head")){
                                    role1=role1+" -"+branch_name+"-"+grade;
                                }
                                if (role1.equals("Section Head")){
                                    role1=role1+" -"+branch_name+"-"+grade+"-"+section;
                                }
                                if (role1.equals("Owner")||role1.equals("Principal")||role1.equals("Administrator")){
                                    role1=role1;
                                }
                                if (role1.equals("Teacher")){
                                    role1=role1+" -"+branch_name+"-"+grade+"-"+section;
                                }


                                roleList.add(role);
                                roleList1.add(role1);

                                ParentDetail_Model model=new ParentDetail_Model();
                                model.setBranch(branch_name);
                                model.setRole(role);
                                model.setGrade(grade);
                                model.setSection(section);
                                model.setSubject(subject);

                                feedslist22.add(model);



                            }
                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,roleList1);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_members.setAdapter(adapter3_branch);
                        } catch (JSONException e) {
//                    e.printStackTrace();
                        }
                      //  updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Staff_Details.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        thisActivity.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id",StaticVariable.user_id);
                params.put("user_email",StaticVariable.email);
                params.put("staff_id",StaticVariable.user_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }
    public void onBack(){
        Intent intent=new Intent(CreateGroupActivity.this,MainActivity.class);
        intent.putExtra("redirection","Communications");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        onBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
                onBack();
                return true;
            }
            default:
                return true;
        }
    }
}
