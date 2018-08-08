package com.yodaapp.live.Parent;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Calander_Event_Details_Model;
import com.yodaapp.live.parsers.Calander_Event_Details_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Calender_Event_Details extends Activity {

    ProgressDialog progress;
    String user_id = "", school_id = "" , user_name = "", event_id = "";
    String tag_string_req_details = "string_req_details";
    List<Calander_Event_Details_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_event_details);

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

        progress = new ProgressDialog(Calender_Event_Details.this);
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
            event_id = getIntent().getExtras().getString("event_id");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Calender_Event_Details.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                spinnerfun(getResources().getString(R.string.url_reference) + "home/event_view_details.php");
            } else {
                Toast.makeText(Calender_Event_Details.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

    }

    protected void updatedisplay()
    {
        progress.show();
        if (feedslist != null) {
            try {
                for (final Calander_Event_Details_Model flower : feedslist) {

                        TextView tv = (TextView) findViewById(R.id.calender_event_details_name);
                        tv.setText(flower.getName());

                        TextView tv2 = (TextView) findViewById(R.id.calender_event_details_notes);
                        tv2.setText(flower.getEvent_notes());

                        TextView tv3 = (TextView) findViewById(R.id.calender_event_details_start_date);
                        tv3.setText(flower.getEvent_start());

                        TextView tv4 = (TextView) findViewById(R.id.calender_event_details_end_date);
                        tv4.setText(flower.getEvent_end());

                        TextView tv5 = (TextView) findViewById(R.id.calender_event_details_school);
                        tv5.setText(flower.getSchool_name());

                        if(!flower.getBranch_name().equals("null")) {

                            TextView tv6 = (TextView) findViewById(R.id.calender_event_details_branch);
                            tv6.setText( flower.getBranch_name());
                            if(!flower.getGrade_name().equals("null")) {

                                TextView tv7 = (TextView) findViewById(R.id.calender_event_details_grade);
                                tv7.setText(flower.getGrade_name());

                                if(!flower.getSection_name().equals("null")) {
                                    TextView tv8 = (TextView) findViewById(R.id.calender_event_details_section);
                                    tv8.setText(flower.getSection_name());
                                }
                            }
                        }
                }
            } catch (Exception e)
            {
               // e.printStackTrace();
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
                        Toast.makeText(Calender_Event_Details.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Calender_Event_Details.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Calender_Event_Details.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinnerfun(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Calender_Event_Details.this, SplashScreen.class);
                                        Calender_Event_Details.this.finish();
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
                params.put("event_id", event_id);
//                Log.d("school_id", school_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_name);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_details);
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent(Calender_Event_Details.this, MainActivity_Parent.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_name);
                intent.putExtra("redirection", "Calender");
                Calender_Event_Details.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            }
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Calender_Event_Details.this, MainActivity_Parent.class);
        intent.putExtra("school_id",school_id);
        intent.putExtra("user_id",user_id);
        intent.putExtra("email",user_name);
        intent.putExtra("redirection","Calender");
        Calender_Event_Details.this.finish();
        startActivity(intent);
        Calender_Event_Details.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

}
