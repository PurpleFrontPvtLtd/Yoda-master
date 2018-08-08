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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class SectionSubjectView extends Activity {
    String teacher_id = "", teacher_name = "", subject_name = "", grade_name = "";
    Spinner spnr2;ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();
    String tag_string_req_category = "string_req_category",redirection="",section_head="";
    ProgressDialog progress;
    String section_name = "",section_id="", section_head_id = "",section_heads_id="",section_heads_name="";
    List<Createadmin> feedslist;
    String user_id = "";
    String user_email = "";
    String school_id = "";
    String branch_id = "";
    String grade_id = "";
    String branch_name = "";
    String subject_id = "";
    int min_role = 0;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_subject_view);

        et = (EditText) findViewById(R.id.section_creation_name);


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

        Button section_creation_submit=(Button)findViewById(R.id.section_creation_submit);


        section_creation_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(min_role == 0 || min_role >=4)
                {
                    Toast.makeText(SectionSubjectView.this, "you dont have enough permission ", Toast.LENGTH_LONG).show();


                }else {

                    subject_name = et.getText().toString();
                    if (subject_name.equals("")) {
                        Toast.makeText(SectionSubjectView.this, "Please enter subject name", Toast.LENGTH_LONG).show();
                    } else if (spnr2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(SectionSubjectView.this, "Please select teacher head", Toast.LENGTH_LONG).show();

                    } else {
                        if (isonline()) {
                            progress.show();
                            upadedata1(getResources().getString(R.string.url_reference) + "home/edit_subject.php");
                        } else {
                            Toast.makeText(SectionSubjectView.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


     try {
         teacher_id = getIntent().getExtras().getString("teacher_id");

         teacher_name = getIntent().getExtras().getString("teacher_name");
         subject_name = getIntent().getExtras().getString("subject_name");
         subject_id = getIntent().getExtras().getString("subject_id");

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





         et.setText(subject_name);
         Log.v("techer", teacher_id + teacher_name + subject_name);
     }catch (Exception e){
         e.printStackTrace();
     }

        progress = new ProgressDialog(SectionSubjectView.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();



        myarray.add("Select");
        myarray2.add("Select");
        spinnerfun();
        spnr2 = (Spinner) findViewById(R.id.section_staff_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(SectionSubjectView.this, android.R.layout.simple_spinner_dropdown_item, myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
        //  spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                section_head_id = myarray2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
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
                                Log.d("res", category);
                            }



                                try {


                                    int i = -1;
                                    for (String section : myarray) {
                                        i++;
                                        if (section.equals(teacher_name)) {
                                            spnr2.setSelection(i);
                                        }
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    spnr2.setSelection(0);

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
                        Toast.makeText(SectionSubjectView.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SectionSubjectView.this, SplashScreen.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        intent.putExtra("school_id", StaticVariable.school_id);
                        SectionSubjectView.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
//                Log.d("school_id", school_id);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                Intent intent = new Intent(SectionSubjectView.this, Grade_Section_view.class);
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
                SectionSubjectView.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            case R.id.calender_event_edit_delete:


                if (isonline()) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(SectionSubjectView.this);

                    builder.setMessage(getResources().getString(R.string.confirm_remove_subject))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    progress.show();
                                    upadedata2(getResources().getString(R.string.url_reference) + "home/remove_subject.php");
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                      } else {
                    Toast.makeText(SectionSubjectView.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                }


                return true;

            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.calender_event_edit_menu, menu);
            Drawable drawable = menu.findItem(R.id.calender_event_edit_delete).getIcon();
            drawable = DrawableCompat.wrap(drawable);
          // DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.blue));
            menu.findItem(R.id.calender_event_edit_delete).setIcon(drawable);



        return super.onCreateOptionsMenu(menu);
    }

    void upadedata1(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response",arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(SectionSubjectView.this, Grade_Section_view.class);
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
                                    SectionSubjectView.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_right, R.anim.right_left);

                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.section_name_exists));
                                    Toast.makeText(SectionSubjectView.this, getResources().getString(R.string.section_name_exists), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(SectionSubjectView.this, flower.getId(), Toast.LENGTH_LONG).show();
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
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(SectionSubjectView.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SectionSubjectView.this, MainActivity.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        intent.putExtra("school_id", StaticVariable.school_id);
                        intent.putExtra("email", StaticVariable.email);
                        SectionSubjectView.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("branch_id", branch_id);
                params.put("subject_id", subject_id);
                params.put("user_id", user_id);
                params.put("old_staff_id", teacher_id);
                params.put("subject_name", subject_name);
                params.put("staff_id", section_head_id);
                     Log.d("school_id", school_id);
               Log.d("branch_id", branch_id);
                Log.d("subject_id", subject_id);
                Log.d("user_id", user_id);
                 Log.d("old_staff_id", teacher_id);
                 Log.d("subject_name", subject_name);
                 Log.d("staff_id", section_head_id);
                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }
    void upadedata2(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response",arg0);


                        Intent intent = new Intent(SectionSubjectView.this, Grade_Section_view.class);
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
                        SectionSubjectView.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_right, R.anim.right_left);


//                        Log.d("here in sucess", "sucess");
                        progress.hide();

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(SectionSubjectView.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SectionSubjectView.this, MainActivity.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        intent.putExtra("school_id", StaticVariable.school_id);
                        intent.putExtra("email", StaticVariable.email);
                        SectionSubjectView.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("school_id", StaticVariable.school_id);
                params.put("branch_id", branch_id);
                params.put("subject_id", subject_id);
                params.put("section_id", section_id);


                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SectionSubjectView.this, Grade_Section_view.class);
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
        SectionSubjectView.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
