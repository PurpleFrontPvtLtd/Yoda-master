package com.yodaapp.live;


import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School_creation extends Activity{

    private String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    List<Createadmin> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_creation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(School_creation.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();
        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            // getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            //getActionBar().setIcon(R.drawable.ic_home_white_24dp);
            //    getRegisterationID();
        }
        Button b1 = (Button) findViewById(R.id.school_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.school_your_name);
                EditText et2 = (EditText) findViewById(R.id.school_name);
                EditText et3 = (EditText) findViewById(R.id.school_email);
                EditText et4 = (EditText) findViewById(R.id.school_password);
                EditText et5 = (EditText) findViewById(R.id.school_confirm_password);
                String your_name = et.getText().toString();
                String name = et2.getText().toString();
                String email = et3.getText().toString();
                String password = et4.getText().toString();
                String confirm_password = et5.getText().toString();

                if(password.equals(confirm_password)) {
                    try {
                        MCrypt mcrypt = new MCrypt();
                        password = MCrypt.bytesToHex(mcrypt.encrypt(password));
                    } catch (Exception e) {
//                    e.printStackTrace();
                    }
                    if (isonline()) {
                        progress.show();
                        updatedisplay(getResources().getString(R.string.url_reference) + "home/school_contactus.php", your_name, name, email, password);
//                        updatedisplay(getResources().getString(R.string.url_reference) + "home/school_creation.php", your_name, name, email, password);
                    } else {
                        Toast.makeText(School_creation.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    void updatedisplay(String uri, final String your_name, final String name, final String email, final String password) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {

//                        Log.d("here in sucess", "sucess");
//                        Log.d("here in sucess", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                                for (final Createadmin flower : feedslist) {
                                    if (flower.getSucess().equals("success")) {
                                        Intent intent = new Intent(School_creation.this, School_ThankYou.class);
                                        School_creation.this.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
//                                    else {
//                                        Toast.makeText(School_creation.this, flower.getId(), Toast.LENGTH_LONG).show();
//                                    }
                                }
                        }
                        progress.hide();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(School_creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(School_creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(School_creation.this, PreRegistration.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        School_creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("your_name", your_name);
                params.put("school_name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            };
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
                Intent intent = new Intent(School_creation.this, SplashScreen.class);
                School_creation.this.finish();
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
        Intent intent = new Intent(School_creation.this, PreRegistration.class);
        School_creation.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
