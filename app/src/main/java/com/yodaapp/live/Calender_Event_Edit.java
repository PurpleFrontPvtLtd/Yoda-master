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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Calander_Event_Details_Model;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.Calander_Event_Details_JSONParser;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Calender_Event_Edit extends Activity {

    ProgressDialog progress;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String user_id = "", school_id = "", user_name = "", role_id = "";
    String event_id = "";
    String name = "", details = "", start_date = "", end_date = "";
    List<Createadmin> feedslist22;
    List<Calander_Event_Details_Model> feedslist;

    Date dp_date = null;
    Date dp2_date = null;
    String tag_string_req_category3 = "string_req_category_branch";
    EditText et, et2;
    TextView tv, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_event_edit);

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
        progress = new ProgressDialog(Calender_Event_Edit.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) findViewById(R.id.calander_event__edit_name);
        et2 = (EditText) findViewById(R.id.calander_event__edit_details);
        tv = (TextView) findViewById(R.id.calander_event__edit_start_date);
        tv2 = (TextView) findViewById(R.id.calander_event__edit_end_date);

        try {
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_name = getIntent().getExtras().getString("username");
            role_id = getIntent().getExtras().getString("role_id");
            event_id = getIntent().getExtras().getString("event_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Calender_Event_Edit.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                spinnerfun(getResources().getString(R.string.url_reference) + "home/event_view_details.php");
            } else {
                Toast.makeText(Calender_Event_Edit.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        Button b = (Button) findViewById(R.id.calander_event__edit_create);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et.getText().toString();
                details = et2.getText().toString();

                start_date = tv.getText().toString();
                end_date = tv2.getText().toString();

                if (name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.enter_event_name));
                    Toast.makeText(Calender_Event_Edit.this, "Enter event name", Toast.LENGTH_LONG).show();
                }  else if (start_date.equals("")) {
                    Toast.makeText(Calender_Event_Edit.this, "Select event start date", Toast.LENGTH_LONG).show();
                } else if (end_date.equals("")) {
                    Toast.makeText(Calender_Event_Edit.this, "Select event end date", Toast.LENGTH_LONG).show();
                } else if (dp2_date.before(dp_date)) {
                    Toast.makeText(Calender_Event_Edit.this, "Event end date should not be before event start date", Toast.LENGTH_LONG).show();
                } else {
                    int cont = Integer.valueOf(role_id);
                    if (cont < 4) {
                        progress.show();
                        updatedata(getResources().getString(R.string.url_reference) + "home/event_edit.php");
                    } else {
                        Toast.makeText(Calender_Event_Edit.this, getResources().getString(R.string.calander_validation), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
//                Log.d("date", "" + mDay);
//                Log.d("Month", "" + mMonth);
//                Log.d("Year", "" + mYear);

                if (!start_date.equals("")) {
                    try {

                        SimpleDateFormat srcDf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        Date date = srcDf.parse(start_date);

                        String monthname = (String) android.text.format.DateFormat.format("dd", date);
                        mDay = Integer.valueOf(monthname);
//                        Log.d("date", "" + mDay);

                        monthname = (String) android.text.format.DateFormat.format("MM", date);
                        mMonth = Integer.valueOf(monthname) - 1;
//                        Log.d("month", "" + mMonth);

                        monthname = (String) android.text.format.DateFormat.format("yyyy", date);
                        mYear = Integer.valueOf(monthname);
//                        Log.d("Year", "" + mYear);

                    } catch (ParseException e) {
//                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(Calender_Event_Edit.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    dp_date = dateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    String eve = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    tv.setText(eve);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);

                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.getDatePicker().setMinDate(new Date().getTime());
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

                if (!end_date.equals("")) {
                    try {

                        SimpleDateFormat srcDf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        Date date = srcDf.parse(end_date);

                        String monthname = (String) android.text.format.DateFormat.format("dd", date);
                        mDay = Integer.valueOf(monthname);
//                        Log.d("date", "" + mDay);

                        monthname = (String) android.text.format.DateFormat.format("MM", date);
                        mMonth = Integer.valueOf(monthname) - 1;
//                        Log.d("month", "" + mMonth);

                        monthname = (String) android.text.format.DateFormat.format("yyyy", date);
                        mYear = Integer.valueOf(monthname);
//                        Log.d("Year", "" + mYear);

                    } catch (ParseException e) {
//                        e.printStackTrace();
                    }

                }

                DatePickerDialog dpd = new DatePickerDialog(Calender_Event_Edit.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
                dpd.getDatePicker().setMinDate(new Date().getTime());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Calender_Event_Edit.this);
                            builder.setMessage(getResources().getString(R.string.event_update))
                                    .setCancelable(false)
                                    .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Calender_Event_Edit.this, Calender_Event_Details.class);
                                            intent.putExtra("user_id", user_id);
                                            intent.putExtra("school_id", school_id);
                                            intent.putExtra("username", user_name);
                                            intent.putExtra("role_id", role_id);
                                            intent.putExtra("event_id", event_id);
                                            Calender_Event_Edit.this.finish();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
//                        else {
//                            Toast.makeText(Calender_Event_Edit.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
//                        }
                    }
                }
                Log.d("here in sucess", "sucess");
                progress.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Calender_Event_Edit.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                Toast.makeText(Calender_Event_Edit.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("here in error", volleyError.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(Calender_Event_Edit.this);
                builder.setMessage(getResources().getString(R.string.error_occured))
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
                                Intent intent = new Intent(Calender_Event_Edit.this, SplashScreen.class);
                                Calender_Event_Edit.this.finish();
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

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_name);
                params.put("role_id", role_id);
                params.put("name", name);
                params.put("details", details);
                params.put("start_date", start_date);
                params.put("end_date", end_date);
                params.put("event_id", event_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    protected void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            try {
                for (final Calander_Event_Details_Model flower : feedslist) {
                    start_date = flower.getEvent_start();
                    end_date = flower.getEvent_end();

                    tv.setText(start_date);
                    tv2.setText(end_date);
                    name = flower.getName();
                    et.setText(flower.getName());
                    details = flower.getEvent_notes();
                    et2.setText(flower.getEvent_notes());
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        dp2_date = dateFormat.parse(end_date);
                        dp_date = dateFormat.parse(start_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        progress.hide();
    }

    void spinnerfun(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Calander_Event_Details_JSONParser.parserFeed(response);
                        updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Calender_Event_Edit.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Calender_Event_Edit.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Calender_Event_Edit.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinnerfun(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Calender_Event_Edit.this, SplashScreen.class);
                                        Calender_Event_Edit.this.finish();
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

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_name);
                params.put("role_id", role_id);
                params.put("event_id", event_id);
//                Log.d("school_id", school_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_name);
//                Log.d("role_id", role_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    protected void delete_event(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Intent intent = new Intent(Calender_Event_Edit.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_name);
                intent.putExtra("redirection", "Calender");
                Calender_Event_Edit.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Calender_Event_Edit.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
//                Toast.makeText(Calender_Event_Edit.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("here in error", volleyError.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_name", user_name);
                params.put("role_id", role_id);
                params.put("event_id", event_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calender_event_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calender_event_edit_delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(Calender_Event_Edit.this);
                builder.setMessage(getResources().getString(R.string.deleteaccess))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(Calender_Event_Edit.this, getResources().getString(R.string.calander_validation), Toast.LENGTH_LONG).show();
                                //finish();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int cont = Integer.valueOf(role_id);
                                if (cont < 4) {
                                    delete_event(getResources().getString(R.string.url_reference) + "home/event_delete.php");
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
            case android.R.id.home: {
                Intent intent = new Intent(Calender_Event_Edit.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_name);
                intent.putExtra("redirection", "Calender");
                Calender_Event_Edit.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            }
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
        Intent intent = new Intent(Calender_Event_Edit.this, Calender_Event_Details.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("username", user_name);
        intent.putExtra("role_id", role_id);
        intent.putExtra("event_id", event_id);
        Calender_Event_Edit.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
