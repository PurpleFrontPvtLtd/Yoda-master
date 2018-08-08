package com.yodaapp.live;

/**
 * Created by pf-05 on 8/1/2016.
 */

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent.MainActivity_Parent;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Edit_Student_Details extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    List<Createadmin> feedslist;
    String student_user_id = "", student_email = "", student_phone = "", student_password = "", student_confirm_password = "";
    String staff_id_set = "", role_id_set = "",branch_id_set = "";
    String school_id = "",user_id = "",user_email = "";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "",redirection="";
    String selection_branch_id="";
    String grade_id_set = "";
    String Section_id_set = "";
    String subject_id_set = "";
    String allergies = "";
    int min_role = 0;
    boolean entriesValid = true;
    EditText et,et7;
    String picturePathicon = "";
    Button btnSave;
    String status="";

    Boolean isLoadFirst=false;
    String imgString = "",fileName = "";
    private static int RESULT_LOAD_IMAGE = 0;
    byte[] array;
    Activity thisActivity=this;

    String tag_string_req_category2 = "string_req_category_role";

    String tag_string_req_category3 = "string_req_category_branch";

    String tag_string_req_category4 = "string_req_category_grade";

    String tag_string_req_category5 = "string_req_category_section";

    String tag_string_req_category6 = "string_req_category_subject";


    ArrayList<String> myarray_branch = new ArrayList<>();
    ArrayList<String> myarray2_branch = new ArrayList<>();

    ArrayList<String> myarray_section = new ArrayList<>();
    ArrayList<String> myarray2_section = new ArrayList<>();

    ArrayList<String> myarray_subject = new ArrayList<>();
    ArrayList<String> myarray2_subject = new ArrayList<>();

    ArrayList<String> myarray_grade= new ArrayList<>();
    ArrayList<String> myarray2_grade = new ArrayList<>();
    Spinner spnr2,spnr_role,spnr_branch,spnr_grade,spnr_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_students_detail);

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

        et = (EditText) findViewById(R.id.student_add_name);
        et7 = (EditText) findViewById(R.id.student_add_allergies);
        btnSave = (Button) findViewById(R.id.btn_save);

        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        try {

            try {
                branch_id = getIntent().getExtras().getString("branch_id");
                branch_name = getIntent().getExtras().getString("branch_name");
                grade_id = getIntent().getExtras().getString("grade_id");
                grade_name = getIntent().getExtras().getString("grade_name");
                user_id = getIntent().getExtras().getString("user_id");
                school_id = getIntent().getExtras().getString("school_id");
                user_email = getIntent().getExtras().getString("email");
                section_id = getIntent().getExtras().getString("section_id");
                section_name = getIntent().getExtras().getString("section_name");
                student_id = getIntent().getExtras().getString("student_id");
                student_name = getIntent().getExtras().getString("student_name");
                min_role = getIntent().getExtras().getInt("min_role");
                redirection = getIntent().getExtras().getString("redirection");
                allergies = getIntent().getExtras().getString("allergies");
                selection_branch_id=branch_id;
                status="entered";
                Log.d("branch_id","" + branch_id);
          //      Log.d("branch_id","" + branch_id);
                Log.d("section_id","" + section_id);
                Log.d("grade_id","" + grade_id);


            } catch (Exception e) {
//            e.printStackTrace();
            }

            et.setText(student_name);
            et7.setText(allergies);
        } catch (Exception e) {
            e.printStackTrace();
        }


        myarray_branch.add("Select");
        myarray2_branch.add("Select");

        myarray_grade.add("Select");
        myarray2_grade.add("Select");

        myarray_section.add("Select");
        myarray2_section.add("Select");

        myarray_subject.add("Select");
        myarray2_subject.add("Select");


        progress.show();
        spinnerfun_branch();
        spnr_branch=(Spinner) findViewById(R.id.role_assign_branch);
        ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
        adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_branch.setAdapter(adapter3_branch);
    //    spnr_branch.setFocusableInTouchMode(true);




        spnr_grade=(Spinner) findViewById(R.id.role_assign_grade);
        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);
      //  spnr_grade.setFocusableInTouchMode(true);






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



        spnr_section =(Spinner) findViewById(R.id.role_assign_section);
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
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(thisActivity);


                student_name = et.getText().toString();

                allergies = et7.getText().toString();

                if(student_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct__name));
                    Toast.makeText(thisActivity,getResources().getString(R.string.correct__name),Toast.LENGTH_LONG).show();
                }
               else if(spnr_branch.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_branch),Toast.LENGTH_LONG).show();
                }
                else if(spnr_grade.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_grade),Toast.LENGTH_LONG).show();
                }
                else if(spnr_section.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_section),Toast.LENGTH_LONG).show();
                }

                else
                {
                    if(isonline()) {

                        progress.show();
                        updatedata(getResources().getString(R.string.url_reference) + "home/edit_student_details.php");
                    }
                }
            }
        });

    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);

                        try {
                            JSONObject json=new JSONObject(arg0);
                            String response=json.getString("sucess");

                            if (response.equals("Profile updated")){


                                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                builder.setMessage(getResources().getString(R.string.student_update))
                                        .setCancelable(false)
                                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent=new Intent(thisActivity,Section_Student_View.class);
                                                    intent.putExtra("branch_id", branch_id);
                                                    intent.putExtra("branch_name", branch_name);
                                                    intent.putExtra("grade_id", grade_id);
                                                    intent.putExtra("grade_name", grade_name);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("email", user_email);
                                                    intent.putExtra("section_id", section_id);
                                                    intent.putExtra("section_name",section_name);
                                                    intent.putExtra("student_id", student_id);
                                                    intent.putExtra("student_name", student_name);
                                                    intent.putExtra("min_role", min_role);
                                                    intent.putExtra("redirection",redirection);
                                                    intent.putExtra("allergies",allergies);
                                                    startActivity(intent);
                                                    finish();
                                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                            else {
                                Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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


//                        Toast.makeText(thisActivity, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", "");
                params.put("id",student_id);
                params.put("name",student_name);
                params.put("allergies",allergies);
                params.put("section_id",Section_id_set);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    public void spinnerfun_branch() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        if (status.contains("clear")) {
                            progress.hide();
                        }
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

                            if (status.contains("entered")) {
                                int i = -1;
                                for (String id : myarray2_branch) {
                                    i++;
                                    if (id.equals(branch_id)) {
                                        spnr_branch.setSelection(i);
                                    }
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
                        if (status.contains("entered")) {
                            int i = -1;
                            for (String id : myarray2_grade) {
                                i++;
                                if (id.equals(grade_id)) {
                                    spnr_grade.setSelection(i);
                                }
                            }
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

                        if (status.contains("entered")) {
                            int i = -1;
                            for (String id : myarray2_section) {
                                i++;
                                if (id.equals(section_id)) {
                                    spnr_section.setSelection(i);
                                    status="clear";
                                }
                            }
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

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit."))
        {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }


    public static void hideKeyboard(Activity activity)
    {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void goBack(){
        Intent intent=new Intent(thisActivity,Section_Student_View.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("section_id", section_id);
        intent.putExtra("section_name",section_name);
        intent.putExtra("student_id", student_id);
        intent.putExtra("student_name", student_name);
        intent.putExtra("min_role", min_role);
        intent.putExtra("redirection",redirection);
        intent.putExtra("allergies",allergies);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public void onBackPressed() {
        goBack();

    }
}

