package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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


public class Communications_Staff_to_Staff_New_Message extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "", role_id = "";
    List<Createadmin> feedslist;

    String tag_string_req_category = "string_req_category";

    String tag_string_req_category2 = "string_req_category_role";

    String tag_string_req_category3 = "string_req_category_branch";
    String student_id = "", parent_id = "";
    String message = "";
    Activity thisActivity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communications__staff_to__staff__new__message);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progress = new ProgressDialog(Communications_Staff_to_Staff_New_Message.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            role_id = getIntent().getExtras().getString("role_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button b = (Button) findViewById(R.id.communication_staff_new_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.communication_staff_new_message);
                message = et.getText().toString();
                if (message.trim().equals("")) {
                    et.setError(getResources().getString(R.string.enter_message));
                    Toast.makeText(thisActivity, getResources().getString(R.string.enter_message), Toast.LENGTH_LONG).show();
                } else {
                    progress.show();
                    updateonlinedata(getResources().getString(R.string.url_reference) + "home/communication_staff_message_insert.php");
                }
            }
        });

    }


    void updateonlinedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
                        feedslist = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                    builder.setMessage(getResources().getString(R.string.message_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Intent intent = new Intent(thisActivity, Communications_Staff.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("email", user_email);
                                                    intent.putExtra("role_id",role_id);
                                                    thisActivity.finish();
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(Communications_Staff_New_Message.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
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
                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                                      }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("messages", message);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(thisActivity, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                thisActivity.finish();
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
        Intent intent = new Intent(thisActivity, MainActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("redirection", "Communications");
        thisActivity.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

}
