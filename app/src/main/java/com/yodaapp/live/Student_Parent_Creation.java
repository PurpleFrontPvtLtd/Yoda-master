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
import android.text.method.PasswordTransformationMethod;
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

public class Student_Parent_Creation extends Activity {

    protected String tag_string_req_recieve2 = "string_req_recieve2";
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    int min_role = 0 ;
    Spinner spnr2, spnr3;
    String profession_id = "0";
    CheckBox ch;
    String parent_name = "", parent_email = "",parent_phone = "",parent_password = "",parent_confirm_password = "", parent_userid = "";
    int relationship_id = 0;
    List<Createadmin> feedslist;
    String redirection = "";
    EditText et,et2,et3,et4,et5,et6;

    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();

    ArrayList<String> myarray3= new ArrayList<>();
    ArrayList<String> myarray4= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_parent_creation);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        progress = new ProgressDialog(Student_Parent_Creation.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        myarray.add("Select");
        myarray2.add("0");

        myarray3.add("Select");
        myarray4.add("0");

        if(school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                updatedata(getResources().getString(R.string.url_reference) + "home/relationship_list.php");
                updatedata_profession(getResources().getString(R.string.url_reference) + "home/profession_list.php");
            } else {
                Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        spnr2=(Spinner) findViewById(R.id.parent_relationship_spinner);
        ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Creation.this,android.R.layout.simple_spinner_dropdown_item,myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
        //  spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                hideKeyboard(Student_Parent_Creation.this);
//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                String poss = myarray2.get(position);
                try {
                    relationship_id = Integer.valueOf(poss);
                } catch (Exception e) {
                    relationship_id = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                relationship_id = 0;
                hideKeyboard(Student_Parent_Creation.this);
            }
        });


        et = (EditText) findViewById(R.id.parent_name);
        et2 = (EditText) findViewById(R.id.parent_email);
        et3 = (EditText) findViewById(R.id.parent_phone);
        et4 = (EditText) findViewById(R.id.parent_password);
        et5 = (EditText) findViewById(R.id.parent_confirm_password);
        et6 = (EditText) findViewById(R.id.parent_userid);

        spnr3=(Spinner) findViewById(R.id.parent_profession_spinner);
        ArrayAdapter<String> adapter4=new ArrayAdapter<>(Student_Parent_Creation.this,android.R.layout.simple_spinner_dropdown_item,myarray3);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr3.setAdapter(adapter4);
        // spnr3.setFocusableInTouchMode(true);

        spnr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                hideKeyboard(Student_Parent_Creation.this);
                String poss = myarray4.get(position);
                try {
                    profession_id = poss;
                } catch (Exception e) {
                    profession_id = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                profession_id = "0";
                hideKeyboard(Student_Parent_Creation.this);
            }
        });

        ch = (CheckBox) findViewById(R.id.parent_change_password);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                EditText et5 = (EditText) findViewById(R.id.parent_password);
                EditText et6 = (EditText) findViewById(R.id.parent_confirm_password);
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

        Button b = (Button) findViewById(R.id.parent_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Student_Parent_Creation.this);


                parent_name = et.getText().toString();
                parent_email = et2.getText().toString();
                parent_phone = et3.getText().toString();
                parent_password = et4.getText().toString();
                parent_confirm_password = et5.getText().toString();
                parent_userid = et6.getText().toString();

                if(parent_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct_name));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.correct_name),Toast.LENGTH_LONG).show();
                }
                else if(relationship_id == 0) {
                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.relationship_validation), Toast.LENGTH_LONG).show();
                }

                else if(parent_email.trim().equals("")) {
                    et2.setError(getResources().getString(R.string.correct_email));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.correct_email), Toast.LENGTH_LONG).show();
                }
                else if(!isEmailValid(parent_email)) {
                    et2.setError(getResources().getString(R.string.correct_email2));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.correct_email2), Toast.LENGTH_LONG).show();
                }
                else if(parent_userid.trim().equals("")) {
                    et6.setError(getResources().getString(R.string.staff_username));
                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.staff_username), Toast.LENGTH_LONG).show();
                }
                else if(!parent_userid.trim().equals(parent_userid)) {
                    et6.setError(getResources().getString(R.string.staff_username_space));
                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.staff_username_space), Toast.LENGTH_LONG).show();
                }
                else if(parent_userid.length() < 6) {
                    et6.setError(getResources().getString(R.string.staff_username_size));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.staff_username_size),Toast.LENGTH_LONG).show();
                }
                else if(parent_password.trim().equals("")) {
                    et4.setError(getResources().getString(R.string.correct_password));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.correct_password),Toast.LENGTH_LONG).show();
                }
                else if(parent_confirm_password.trim().equals("")) {
                    et5.setError(getString(R.string.correct_confirm_password));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.correct_confirm_password),Toast.LENGTH_LONG).show();
                }
                else if(!parent_password.trim().equals(parent_password)) {
                    et4.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.password_space),Toast.LENGTH_LONG).show();
                }
                else if(parent_password.length() < 6 ) {
                    et4.setError(getResources().getString(R.string.password_limit_correct));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.password_limit_correct),Toast.LENGTH_LONG).show();
                }
                else if(!parent_confirm_password.trim().equals(parent_confirm_password)) {
                    et5.setError(getResources().getString(R.string.password_confirm_space));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.password_confirm_space),Toast.LENGTH_LONG).show();
                }
                else if(parent_confirm_password.length() < 6) {
                    et5.setError(getResources().getString(R.string.confirm_password_limit_correct));
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.confirm_password_limit_correct),Toast.LENGTH_LONG).show();
                }
                else if(!parent_confirm_password.equals(parent_password)) {
                    Toast.makeText(Student_Parent_Creation.this,getResources().getString(R.string.password_confirm_password_not_match),Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        MCrypt mcrypt = new MCrypt();
                        parent_password = MCrypt.bytesToHex(mcrypt.encrypt(parent_password));
                        parent_confirm_password = MCrypt.bytesToHex(mcrypt.encrypt(parent_confirm_password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isonline()) {
                        if(relationship_id != 0) {
                            progress.show();
                            displaydata(getResources().getString(R.string.url_reference) + "home/parent_creation.php");
                        } else {
                            Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.relationship_validation), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    void displaydata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0") && !flower.getSchool_id().equals("0")) {
                                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.parent_creation_success_message), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Student_Parent_Creation.this, Section_Student_View.class);
                                    intent.putExtra("branch_id", branch_id);
                                    intent.putExtra("branch_name", branch_name);
                                    intent.putExtra("grade_id", grade_id);
                                    intent.putExtra("grade_name", grade_name);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("section_id", section_id);
                                    intent.putExtra("section_name", section_name);
                                    intent.putExtra("student_id", student_id);
                                    intent.putExtra("student_name", student_name);
                                    intent.putExtra("min_role",min_role);
                                    intent.putExtra("redirection",redirection);
                                    Student_Parent_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                                else if(flower.getSucess().equals("success")) {
                                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.parent_creation_error_message), Toast.LENGTH_LONG).show();
                                } else if(flower.getSucess().equals("existing userid")) {
                                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.userid_existing_message), Toast.LENGTH_LONG).show();
                                } else if(flower.getSucess().equals("existing relation")) {
                                    Toast.makeText(Student_Parent_Creation.this, getResources().getString(R.string.parent_relation), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Student_Parent_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Student_Parent_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Student_Parent_Creation.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Student_Parent_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("student_id",student_id);
                params.put("user_id",user_id);
                params.put("user_email",user_email);
                params.put("role_id", String.valueOf(min_role));
                params.put("parent_name", parent_name);
                params.put("parent_email", parent_email);
                params.put("parent_phone", parent_phone);
                params.put("parent_password", parent_password);
                params.put("parent_userid",parent_userid);
                params.put("relationship_id",String.valueOf(relationship_id));
                params.put("profession_id",profession_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedata(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        myarray.clear();
                        myarray2.clear();
                        myarray.add("Select");
                        myarray2.add("0");
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
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Creation.this,android.R.layout.simple_spinner_dropdown_item,myarray);
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
                        Toast.makeText(Student_Parent_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("Error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Parent_Creation.this);
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
                                        Intent intent = new Intent(Student_Parent_Creation.this, MainActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Student_Parent_Creation.this.finish();
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
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email",user_email);
                params.put("branch_id",branch_id);
                params.put("branch_name",branch_name);
                params.put("grade_id",grade_id);
                params.put("grade_name",grade_name);
                params.put("section_id",section_id);
                params.put("section_name",section_name);
                params.put("student_id",student_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

    }


    void updatedata_profession(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        myarray3.clear();
                        myarray4.clear();
                        myarray3.add("Select");
                        myarray4.add("0");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray3.add(category);
                                myarray4.add(id);
//                                Log.d("res", category);
                            }
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Creation.this,android.R.layout.simple_spinner_dropdown_item,myarray3);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr3.setAdapter(adapter3);

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
                        Toast.makeText(Student_Parent_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("Error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Parent_Creation.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata_profession(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Student_Parent_Creation.this, MainActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Student_Parent_Creation.this.finish();
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
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email",user_email);
                params.put("branch_id",branch_id);
                params.put("branch_name",branch_name);
                params.put("grade_id",grade_id);
                params.put("grade_name",grade_name);
                params.put("section_id",section_id);
                params.put("section_name",section_name);
                params.put("student_id",student_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

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

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Student_Parent_Creation.this, MainActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Student_Parent_Creation.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Student_Parent_Creation.this, Section_Student_View.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("section_id", section_id);
        intent.putExtra("section_name", section_name);
        intent.putExtra("student_id", student_id);
        intent.putExtra("student_name", student_name);
        intent.putExtra("min_role",min_role);
        intent.putExtra("redirection",redirection);
        Student_Parent_Creation.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }


    public void clickPname(View view)
    {
        et.requestFocus();
    }
    public void clickPphone(View view)
    {
        et3.requestFocus();
    }
    public void clickPemail(View view)
    {
        et2.requestFocus();
    }
    public void clickPuname(View view)
    {
        et6.requestFocus();
    }
    public void clickPpassword(View view)
    {
        et4.requestFocus();
    }
    public void clickPconpass(View view)
    {
        et5.requestFocus();
    }
}
