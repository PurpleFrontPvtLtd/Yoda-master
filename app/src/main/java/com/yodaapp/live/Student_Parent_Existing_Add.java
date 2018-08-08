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

public class Student_Parent_Existing_Add extends Activity {

    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    protected String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    int min_role = 0 ;
    Spinner spnr2 , spnr3;
    int relationship_id = 0 , parent_id = 0;
    String redirection = "";
    List<Createadmin> feedslist;
    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();

    ArrayList<String> myarray_relation = new ArrayList<>();
    ArrayList<String> myarray2_relation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_parent_existing_add);

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
//            e.printStackTrace();
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

        progress = new ProgressDialog(Student_Parent_Existing_Add.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if(school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/parent_list.php");
            } else {
                Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        myarray.add("Select");
        myarray2.add("0");

        myarray_relation.add("Select");
        myarray2_relation.add("0");

        spnr2=(Spinner) findViewById(R.id.parent_existing_spinner);
        ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Existing_Add.this,android.R.layout.simple_spinner_dropdown_item,myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
       // spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                String poss = myarray2.get(position);
                try {
                    parent_id = Integer.valueOf(poss);
                } catch (Exception e) {
                    parent_id = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent_id = 0;
            }
        });

        spinnerfun(getResources().getString(R.string.url_reference) + "home/relationship_list.php");
        spnr3=(Spinner) findViewById(R.id.student_parent_existing_relationship);
        ArrayAdapter<String> adapter4=new ArrayAdapter<>(Student_Parent_Existing_Add.this,android.R.layout.simple_spinner_dropdown_item,myarray_relation);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr3.setAdapter(adapter4);
        //spnr3.setFocusableInTouchMode(true);

        spnr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_relation.get(position));
//                Log.d("myaray2", myarray2_relation.get(position));
                String poss = myarray2_relation.get(position);
                try {
                    relationship_id = Integer.valueOf(poss);
                } catch (Exception e) {
                    relationship_id = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                relationship_id = 0;
            }
        });

        Button b = (Button) findViewById(R.id.student_parent_existing_add_ok);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (parent_id != 0) {
                    if(relationship_id != 0) {
                        if (isonline()) {
                            if (min_role < 4 && min_role > 0) {
                                progress.show();
                                displaydata(getResources().getString(R.string.url_reference) + "home/parent_selection.php");
                            } else {
                                Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.relationship_validation), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.parent_selection_validation), Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.parent_existing_allot), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Student_Parent_Existing_Add.this, Section_Student_View.class);
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
                                    Student_Parent_Existing_Add.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                                else if(flower.getSucess().equals("success")) {
                                    Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.parent_creation_some_error), Toast.LENGTH_LONG).show();
                                }
                                else if(flower.getSucess().equals("existing relation")) {
                                    Toast.makeText(Student_Parent_Existing_Add.this, getResources().getString(R.string.parent_relation), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Student_Parent_Existing_Add.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Student_Parent_Existing_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Existing_Add.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Student_Parent_Existing_Add.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Student_Parent_Existing_Add.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
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
                params.put("parent_id", String.valueOf(parent_id));
                params.put("relationship_id",String.valueOf(relationship_id));
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
                        progress.hide();
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
                                String name = obj.getString("name");
                                String userid = obj.getString("userid");
                                myarray.add(name + " ( " + userid + " ) ");
                                myarray2.add(id);
                            }
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Existing_Add.this,android.R.layout.simple_spinner_dropdown_item,myarray);
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
                        Toast.makeText(Student_Parent_Existing_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Existing_Add.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("Error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Parent_Existing_Add.this);
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
                                        Intent intent = new Intent(Student_Parent_Existing_Add.this, MainActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role",min_role);
                                        Student_Parent_Existing_Add.this.finish();
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

    void spinnerfun(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        myarray_relation.clear();
                        myarray2_relation.clear();
                        myarray_relation.add("Select");
                        myarray2_relation.add("0");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String name = obj.getString("name");
                                myarray_relation.add(name);
                                myarray2_relation.add(id);
//                                Log.d("res", name);
                            }
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Student_Parent_Existing_Add.this,android.R.layout.simple_spinner_dropdown_item,myarray_relation);
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
                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(Student_Parent_Existing_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_Parent_Existing_Add.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Parent_Existing_Add.this);
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
                                        Intent intent = new Intent(Student_Parent_Existing_Add.this, MainActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role",min_role);
                                        Student_Parent_Existing_Add.this.finish();
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
                Intent intent = new Intent(Student_Parent_Existing_Add.this, Section_Student_View.class);
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
                Student_Parent_Existing_Add.this.finish();
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
        Intent intent = new Intent(Student_Parent_Existing_Add.this, Section_Student_View.class);
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
        Student_Parent_Existing_Add.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
