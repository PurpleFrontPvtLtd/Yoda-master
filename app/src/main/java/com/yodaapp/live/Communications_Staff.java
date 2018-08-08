package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Communication_Staff_Main_Model;
import com.yodaapp.live.parsers.Communication_Staff_Main_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communications_Staff extends Activity {

    ProgressDialog progress;
    List<Communication_Staff_Main_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "";
    TextView tv;
    String school_name;
    String role_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_staff);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Communications_Staff.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        try {
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
            role_id = getIntent().getExtras().getString("role_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView b = (TextView) findViewById(R.id.communication_staff_messages);
      //  b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Communications_Staff.this, Communication_Staff_Send_School.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                Communications_Staff.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        TextView b2 = (TextView) findViewById(R.id.communication_staff_branches);
      //  b2.setPaintFlags(b2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Communications_Staff.this, Communication_Staff_Branches.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                Communications_Staff.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        tv = (TextView) findViewById(R.id.communication_staff_school_name);
        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communications_Staff.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/communication_main_details.php");
            } else {
                Toast.makeText(Communications_Staff.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }
    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_Staff_Main_JSONParser.parserFeed(arg0);
                        updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communications_Staff.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communications_Staff.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/communication_main_details.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Communications_Staff.this, SplashScreen.class);
                                        Communications_Staff.this.finish();
                                        startActivity(intent);
                                        Communications_Staff.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("user_email", user_email);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            for (final Communication_Staff_Main_Model flower : feedslist) {

                school_name = flower.getBusiness_name();
                tv.setText(school_name);
                role_id = flower.getRoles();

                if (!flower.getUnread().equals("0")) {
                    TextView b = (TextView) findViewById(R.id.communication_staff_messages);
                    b.setText(getResources().getString(R.string.messages) + " ( " + flower.getUnread() + " ) ");
                  //  b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    b.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }
        } else {
            Toast.makeText(Communications_Staff.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Communications_Staff.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                Communications_Staff.this.finish();
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
        Intent intent = new Intent(Communications_Staff.this, MainActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("redirection", "Communications");
        Communications_Staff.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
