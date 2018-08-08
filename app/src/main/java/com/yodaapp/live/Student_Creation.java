package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.model.Student_View_All_Model;
import com.yodaapp.live.parsers.Student_View_All_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Student_Creation extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    List<Createadmin> feedslist1;
    String  student_user_id = "", student_email = "", student_phone = "", student_password = "", student_confirm_password = "";
    CheckBox ch;
    String allergies = "";
    int min_role = 0;
    boolean entriesValid = true;
    EditText et,et2,et3,et4,et5,et6,et7;
    String picturePathicon = "";
    ImageButton ig;
    String imgString = "",fileName = "";
    private static int RESULT_LOAD_IMAGE = 0;
    byte[] array;
    String status="";

    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
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
    String selection_branch_id="";
    String staff_id_set = "", role_id_set = "",branch_id_set = "";
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "",redirection="";

    String grade_id_set = "";
    String Section_id_set = "";
    String subject_id_set = "";
   Activity thisActivity=this;
    List<Student_View_All_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_creation1);

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

        progress = new ProgressDialog(Student_Creation.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) findViewById(R.id.student_add_name);
        et2 = (EditText) findViewById(R.id.student_add_userid);
        et3 = (EditText) findViewById(R.id.student_add_email);
        et4 = (EditText) findViewById(R.id.student_add_phone);
        et5 = (EditText) findViewById(R.id.student_add_password);
        et6 = (EditText) findViewById(R.id.student_add_confirm_password);
        et7 = (EditText) findViewById(R.id.student_add_allergies);

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
       // spnr_branch.setFocusableInTouchMode(true);




        spnr_grade=(Spinner) findViewById(R.id.role_assign_grade);
        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);
       // spnr_grade.setFocusableInTouchMode(true);






        spnr_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_grade.get(position));
//                Log.d("myaray2", myarray2_grade.get(position));



                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    grade_id_set = myarray2_grade.get(position);
                    if (!spnr_grade.getSelectedItem().toString().contains("Select")) {
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

                if (!spnr_branch.getSelectedItem().toString().equals("Select")){
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


        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            min_role = getIntent().getExtras().getInt("role_id");
            redirection = getIntent().getExtras().getString("redirection");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ig = (ImageButton) findViewById(R.id.student_add_image);
        ig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RESULT_LOAD_IMAGE = 1;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        ch = (CheckBox) findViewById(R.id.student_creation_change_password);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText et5 = (EditText) findViewById(R.id.student_add_password);
                EditText et6 = (EditText) findViewById(R.id.student_add_confirm_password);
                if (isChecked) {
                    et5.setTransformationMethod(null);
                    et6.setTransformationMethod(null);
                    ch.setText(getResources().getString(R.string.hide_password));
                } else {
                    et5.setTransformationMethod(new PasswordTransformationMethod());
                    et6.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText(getResources().getString(R.string.show_password));
                }
            }
        });

        Button b = (Button) findViewById(R.id.add_new_student_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Student_Creation.this);
                et = (EditText) findViewById(R.id.student_add_name);
                et2 = (EditText) findViewById(R.id.student_add_userid);
                et3 = (EditText) findViewById(R.id.student_add_email);
                et4 = (EditText) findViewById(R.id.student_add_phone);
                et5 = (EditText) findViewById(R.id.student_add_password);
                et6 = (EditText) findViewById(R.id.student_add_confirm_password);
                et7 = (EditText) findViewById(R.id.student_add_allergies);

                student_name = et.getText().toString();
                student_user_id = et2.getText().toString();
                student_email = et3.getText().toString();
                student_phone = et4.getText().toString();
                student_password = et5.getText().toString();
                student_confirm_password = et6.getText().toString();
                allergies = et7.getText().toString();

                if(student_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct__name));
                    Toast.makeText(Student_Creation.this,getResources().getString(R.string.correct__name),Toast.LENGTH_LONG).show();
                }



                else  if(spnr_branch.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_branch),Toast.LENGTH_LONG).show();
                }
                else if(spnr_grade.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_grade),Toast.LENGTH_LONG).show();
                }
                else if(spnr_section.getSelectedItem().equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_section),Toast.LENGTH_LONG).show();
                }

                else if (!student_user_id.isEmpty()||!student_password.isEmpty()||!student_confirm_password.isEmpty()){

                    if (student_user_id.equals("")||!student_password.contains("")||!student_confirm_password.contains("")){
                        Toast.makeText(Student_Creation.this,"Please enter user name",Toast.LENGTH_LONG).show();

                    }
                    else if (!student_user_id.equals("") && student_password.equals("") && student_confirm_password.equals("")){
                        Toast.makeText(Student_Creation.this,"Please enter password",Toast.LENGTH_LONG).show();

                    }

                    else if (!student_user_id.equals("") && !student_password.equals("") && student_confirm_password.equals("")){
                        Toast.makeText(Student_Creation.this,"Please enter Confirm password",Toast.LENGTH_LONG).show();

                    }

                    else if (!student_user_id.equals("") && student_password.equals("") && !student_confirm_password.equals("")){
                        Toast.makeText(Student_Creation.this,"Please enter the Password",Toast.LENGTH_LONG).show();

                    }

                    else if (!student_user_id.equals("") && student_password.equals(student_confirm_password) && !student_confirm_password.equals("")) {


                        updatedata(getResources().getString(R.string.url_reference) + "home/student_creation.php");


                        //Toast.makeText(Student_Creation.this, "Submitted", Toast.LENGTH_LONG).show();

                    }


                    else if(!student_confirm_password.equals(student_password)) {
                        et6.setError(getResources().getString(R.string.password_confirm_password_not_match));
                        Toast.makeText(Student_Creation.this,getResources().getString(R.string.password_confirm_password_not_match),Toast.LENGTH_LONG).show();
                    }
                }





              else if (student_user_id.equals("")||!student_password.contains("")||!student_confirm_password.contains("")) {


                    updatedata(getResources().getString(R.string.url_reference) + "home/student_creation.php");


                    //Toast.makeText(Student_Creation.this, "Please enter user name", Toast.LENGTH_LONG).show();

             }




                    else {

                    Toast.makeText(Student_Creation.this,"ELSE PART",Toast.LENGTH_LONG).show();


                    if(isonline()) {
                        try {
                            MCrypt mcrypt = new MCrypt();
                            student_password = MCrypt.bytesToHex( mcrypt.encrypt(student_password) );
                            student_confirm_password = MCrypt.bytesToHex( mcrypt.encrypt(student_confirm_password) );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // progress.show();
                       // updatedata(getResources().getString(R.string.url_reference) + "home/student_creation.php");
                    }
                }

                //Toast.makeText(Student_Creation.this,"OUTSIDEELSE PART",Toast.LENGTH_LONG).show();
            }
        });
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

                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_branch.setAdapter(adapter3_branch);

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
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
                        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_grade.setAdapter(adapter3_grade);


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
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
                        try {
                            JSONArray ar = new JSONArray(arg0);
                        } catch (JSONException e) {
                            et2.setError(getResources().getString(R.string.userid_existing_message));
                            Toast.makeText(Student_Creation.this, getResources().getString(R.string.userid_existing_message), Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }

                        feedslist = Student_View_All_JSONParser.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Student_View_All_Model flower : feedslist) {
                                if (!flower.getStudent_id().equals("null")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Student_Creation.this);
                                    builder.setMessage(getResources().getString(R.string.sudent_creation_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Intent intent = new Intent(thisActivity, Section_Student_View.class);

                                                    intent.putExtra("branch_id", flower.getBranch_id());
                                                    intent.putExtra("branch_name", flower.getBranch_name());
                                                    intent.putExtra("grade_id", flower.getGrade_id());
                                                    intent.putExtra("grade_name", flower.getGrade_name());
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("email", user_email);
                                                    intent.putExtra("section_id", flower.getSection_id());
                                                    intent.putExtra("section_name", flower.getSection_name());
                                                    intent.putExtra("student_id", flower.getStudent_id());
                                                    intent.putExtra("student_name", flower.getStudent_name());
                                                    intent.putExtra("min_role", min_role);
                                                    intent.putExtra("redirection", redirection);
                                                    thisActivity.finish();
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);             }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else if(flower.getStudent_id().equals("0")) {
                                    et2.setError(getResources().getString(R.string.userid_existing_message));
                                    Toast.makeText(Student_Creation.this, getResources().getString(R.string.userid_existing_message), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Student_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Student_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("student_name",student_name);
                params.put("student_email",student_email);
                params.put("student_userid",student_user_id);
                params.put("student_phone",student_phone);
                params.put("student_password",student_password);
                params.put("image",imgString);
                params.put("file_name",fileName);
                params.put("allergies",allergies);
                params.put("section_id",Section_id_set);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePathicon = cursor.getString(columnIndex);
                cursor.close();
                String fileNameSegments[] = picturePathicon.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                Log.d("file_name",fileName);
//				Log.d("lee", "icon");
                Bitmap bm = BitmapFactory.decodeFile(picturePathicon);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                //bm = Bitmap.createScaledBitmap(bm, 96, 96, true);
                float maxImageSize = (float) 300;
                float ratio = Math.min(
                        (float) maxImageSize / bm.getWidth(),
                        (float) maxImageSize / bm.getHeight());
                int width = Math.round((float) ratio * bm.getWidth());
                int height = Math.round((float) ratio * bm.getHeight());
                Log.d("width", "" + width);
                Log.d("height",""+height);
                maxImageSize = (float) 200;
                ratio = Math.min(
                        (float) maxImageSize / bm.getWidth(),
                        (float) maxImageSize / bm.getHeight());
                width = Math.round((float) ratio * bm.getWidth());
                height = Math.round((float) ratio * bm.getHeight());
                bm = Bitmap.createScaledBitmap(bm, width, height, true);
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                array=stream.toByteArray();
                imgString = Base64.encodeToString(array, Base64.NO_WRAP);
                ig.setImageBitmap(bm);

                //			Toast.makeText(Adminconfig.this, "Image icon is set", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {

                e.printStackTrace();
                //		Toast.makeText(Adminconfig.this, "Please try uploading image of less in size", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    public void onBack(){

        if (redirection.equals("home")){
            Intent intent = new Intent(thisActivity, MainActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            thisActivity.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
        else if (redirection.equals("Allstudents"))

        {
            Intent intent = new Intent(Student_Creation.this, Student_View_All.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            intent.putExtra("min_role", min_role);
            Student_Creation.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);

        }

        else {
            Intent intent = new Intent(Student_Creation.this, MainActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            Student_Creation.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBack();
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


    @Override
    public void onBackPressed() {
        onBack();
    }
    public void clickName(View view){
        et.requestFocus();
    }
    public void clickEmail(View view){
        et3.requestFocus();
    }
    public void clickPhone(View view){
        et4.requestFocus();
    }
    public void clickPassword(View view){
        et5.requestFocus();
    }  public void clickPassword1(View view){
        et6.requestFocus();
    }

    public void clickAllergy(View view){
        et7.requestFocus();
    } public void clickUser(View view){
        et2.requestFocus();
    }
}
