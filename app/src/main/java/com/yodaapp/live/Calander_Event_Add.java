package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Calander_Event_Add extends Activity {

    ProgressDialog progress;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String user_id = "", school_id = "", user_name = "", role_id = "";
    String branch_id = "All", school_id_set = "", grade_id = "All", section_id = "All";
    String name = "", details = "", start_date = "", end_date = "";
    Spinner spnr2, spnr_branch, spnr_grade, spnr_section;
    List<Createadmin> feedslist22;

    Date dp_date = null;
    Date dp2_date = null;

    ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();

    ArrayList<String> myarray_branch = new ArrayList<>();
    ArrayList<String> myarray2_branch = new ArrayList<>();

    ArrayList<String> myarray_grade = new ArrayList<>();
    ArrayList<String> myarray2_grade = new ArrayList<>();

    ArrayList<String> myarray_section = new ArrayList<>();
    ArrayList<String> myarray2_section = new ArrayList<>();

    String tag_string_req_category3 = "string_req_category_branch";

    String tag_string_req_category4 = "string_req_category_grade";

    String tag_string_req_category5 = "string_req_category_section";

    String tag_string_req_category6 = "string_req_category_subject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calander_event_add);

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

        progress = new ProgressDialog(Calander_Event_Add.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_name = getIntent().getExtras().getString("username");
            role_id = getIntent().getExtras().getString("role_id");
        } catch (Exception e) {

            user_id=StaticVariable.user_id;
            school_id=StaticVariable.school_id;

            e.printStackTrace();
        }

        myarray_branch.add("All");
        myarray2_branch.add("All");

        myarray_grade.add("All");
        myarray2_grade.add("All");

        myarray_section.add("All");
        myarray2_section.add("All");

        if (StaticVariable.school_id.equals("") || StaticVariable.user_id.equals("")) {
            Toast.makeText(Calander_Event_Add.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
             //   spinnerfun();
                spinnerfun_branch();
            } else {
                Toast.makeText(Calander_Event_Add.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        spnr2 = (Spinner) findViewById(R.id.calander_event__add_school);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
     //   spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                if (myarray.get(position).equals("All") || myarray.get(position).equals("")) {
                    branch_id = "All";
                    grade_id = "All";
                    section_id = "All";
                } else {
                    school_id_set = myarray2.get(position);
                    spinnerfun_branch();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                school_id_set = "All";
                branch_id = "All";
                grade_id = "All";
                section_id = "All";
            }
        });


        spnr_branch = (Spinner) findViewById(R.id.calander_event__add_branch);
        ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_branch);
        adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_branch.setAdapter(adapter3_branch);
        //spnr_branch.setFocusableInTouchMode(true);

        spnr_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_branch.get(position));
//                Log.d("myaray2", myarray2_branch.get(position));
                if (myarray_branch.get(position).equals("All") || myarray_branch.get(position).equals("")) {
                    section_id = "All";
                    grade_id = "All";
                } else {
                    branch_id = myarray2_branch.get(position);
                    progress.show();
                    spinnerfun_grade(myarray2_branch.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                branch_id = "All";
                section_id = "All";
                grade_id = "All";
            }
        });


        spnr_grade = (Spinner) findViewById(R.id.calander_event__add_grade);
        ArrayAdapter<String> adapter3_grade = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);
        //spnr_grade.setFocusableInTouchMode(true);

        spnr_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_grade.get(position));
//                Log.d("myaray2", myarray2_grade.get(position));
                if (myarray_grade.get(position).equals("All") || myarray_grade.get(position).equals("")) {
                    section_id = "All";
                    grade_id = "All";
                } else {
                    grade_id = myarray2_grade.get(position);
                    progress.show();
                    int pos = spnr_branch.getSelectedItemPosition();
                    spinnerfun_section(myarray2_branch.get(pos), myarray2_grade.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                section_id = "All";
                grade_id = "All";
            }
        });


        spnr_section = (Spinner) findViewById(R.id.calander_event__add_section);
        ArrayAdapter<String> adapter3_section = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_section);
        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_section.setAdapter(adapter3_section);
        //spnr_section.setFocusableInTouchMode(true);

        spnr_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));
                if (myarray_section.get(position).equals("All") || myarray_section.get(position).equals("")) {
                    section_id = "All";
                } else {
                    section_id = myarray2_section.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                section_id = "All";
            }
        });

        Button b = (Button) findViewById(R.id.calander_event__add_create);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.calander_event__add_name);
                EditText et2 = (EditText) findViewById(R.id.calander_event__add_details);
                name = et.getText().toString();
                details = et2.getText().toString();
                if (name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.enter_event_name));
                    Toast.makeText(Calander_Event_Add.this, "Enter event name", Toast.LENGTH_LONG).show();
                } else if (start_date.equals("")) {
                    Toast.makeText(Calander_Event_Add.this, "Select event start date", Toast.LENGTH_LONG).show();
                } else if (end_date.equals("")) {
                    Toast.makeText(Calander_Event_Add.this, "Select event end date", Toast.LENGTH_LONG).show();
                } else if (dp2_date.before(dp_date)) {
                    Toast.makeText(Calander_Event_Add.this, "Event end date should not be before event start date", Toast.LENGTH_LONG).show();
                } else {
                    int cont = Integer.valueOf(role_id);
                    if (cont < 4) {
                        progress.show();
                        updatedata(getResources().getString(R.string.url_reference) + "home/event_create.php");
                    } else {
                        Toast.makeText(Calander_Event_Add.this, "You dont have enough permision to send message", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        final TextView tv = (TextView) findViewById(R.id.calander_event__add_start_date);
        final TextView tv2 = (TextView) findViewById(R.id.calander_event__add_end_date);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(Calander_Event_Add.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    dp_date = dateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    dp2_date = dateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    String eve = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    tv.setText(eve);
                                    if (end_date.equals("")) {
                                        tv2.setText(eve);
                                        end_date = start_date;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.getDatePicker().setMinDate(new Date().getDate());
                dpd.show();
            }
        });


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(Calander_Event_Add.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    end_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    dp2_date = dateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    String eve = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    tv2.setText(eve);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.getDatePicker().setMinDate(new Date().getDate());
                dpd.show();
            }
        });
    }

    protected void updatedata(final String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
//                Log.d("response", response);
                feedslist22 = CreateadminJSONParsers.parserFeed(response);
                if (feedslist22 != null) {
                    for (final Createadmin flower : feedslist22) {
                        if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Calander_Event_Add.this);
                            builder.setMessage("Event has successfully created")
                                    .setCancelable(false)
                                    .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
                                            intent.putExtra("school_id", school_id);
                                            intent.putExtra("user_id", user_id);
                                            intent.putExtra("email", user_name);
                                            intent.putExtra("redirection", "Calender");
                                            Calander_Event_Add.this.finish();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Toast.makeText(Calander_Event_Add.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
//                Log.d("here in sucess", "sucess");
                progress.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Calander_Event_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                Toast.makeText(Calander_Event_Add.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("here in error", volleyError.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(Calander_Event_Add.this);
                builder.setMessage(getResources().getString(R.string.nointernetaccess) + " an error has occured try again")
                        .setCancelable(false)
                        .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updatedata(uri);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Calander_Event_Add.this, SplashScreen.class);
                                Calander_Event_Add.this.finish();
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
                params.put("role_id", role_id);
                params.put("name", name);
                params.put("details", details);
                params.put("start_date", start_date);
                params.put("end_date", end_date);
                params.put("school_id_set", school_id);
                if (!section_id.equals("All")) {
                    params.put("section_id", section_id);
                    params.put("grade_id", grade_id);
                    params.put("branch_id", branch_id);
                } else if (!grade_id.equals("All")) {
                    params.put("grade_id", grade_id);
                    params.put("branch_id", branch_id);
                } else if (!branch_id.equals("All")) {
                    params.put("branch_id", branch_id);
                }

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category4);
    }


    void spinnerfun() {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/school_name.php",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        myarray.clear();
                        myarray2.clear();
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
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr2.setAdapter(adapter3);

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
                        Toast.makeText(Calander_Event_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Calander_Event_Add.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Calander_Event_Add.this);
                        builder.setMessage(getResources().getString(R.string.nointernetaccess) + " an error has occured try again")
                                .setCancelable(false)
                                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinnerfun();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Calander_Event_Add.this, SplashScreen.class);
                                        Calander_Event_Add.this.finish();
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
                params.put("role_id", role_id);
//                Log.d("school_id", school_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_name);
//                Log.d("role_id", role_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    public void spinnerfun_branch() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/branch_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_branch.clear();
                        myarray2_branch.clear();

                        myarray_branch.add("All");
                        myarray2_branch.add("All");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("branch_name");
                                myarray_branch.add(category);
                                myarray2_branch.add(id);
//                                Log.d("res", category);
                            }

                            ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_branch);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_branch.setAdapter(adapter3_branch);
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
                        Toast.makeText(Calander_Event_Add.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
                        intent.putExtra("user_id", StaticVariable.user_id);
                        intent.putExtra("school_id", StaticVariable.school_id);
                        intent.putExtra("email", StaticVariable.email);
                        Calander_Event_Add.this.finish();
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
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void spinnerfun_grade(final String branch_idd) {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/grade_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_grade.clear();
                        myarray2_grade.clear();
                        myarray_grade.add("All");
                        myarray2_grade.add("All");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray_grade.add(category);
                                myarray2_grade.add(id);
//                                Log.d("res", category);
                            }


                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_grade = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_grade);
                        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_grade.setAdapter(adapter3_grade);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Calander_Event_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_name);
                        Calander_Event_Add.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("branch_id", branch_idd);
//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_idd);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category5);
    }


    public void spinnerfun_section(final String branch, final String grade) {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/section_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_section.clear();
                        myarray2_section.clear();
                        myarray_section.add("All");
                        myarray2_section.add("All");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("section");
                                myarray_section.add(category);
                                myarray2_section.add(id);
//                                Log.d("res", category);
                            }

                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_section = new ArrayAdapter<>(Calander_Event_Add.this, android.R.layout.simple_spinner_dropdown_item, myarray_section);
                        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_section.setAdapter(adapter3_section);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Calander_Event_Add.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_name);
                        Calander_Event_Add.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("grade_id", grade);
                params.put("branch_id", branch);
//                Log.d("school_id", school_id);
//                Log.d("grade_id", grade);
//                Log.d("branch_id", branch);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category6);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_name);
                intent.putExtra("redirection", "Calender");
                Calander_Event_Add.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Calander_Event_Add.this, MainActivity.class);
        intent.putExtra("school_id", StaticVariable.school_id);
        intent.putExtra("user_id", StaticVariable.user_id);
        intent.putExtra("email", StaticVariable.email);
        intent.putExtra("redirection", "Calender");
        Calander_Event_Add.this.finish();
        startActivity(intent);
        Calander_Event_Add.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
