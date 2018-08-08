package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.Individual_message_adapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Communication_Staff_Message_Home_Model;
import com.yodaapp.live.parsers.Communication_Staff_Message_Home_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communication_Staff_Message_Home extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", school_name = "";
    String user_id = "";
    String user_email = "";
    String student_id = "";
    String student_name = "";
    String role_id = "";
    ListView listview;
    List<Communication_Staff_Message_Home_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_staff_message_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listview=(ListView)findViewById(R.id.listview);


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

        progress = new ProgressDialog(Communication_Staff_Message_Home.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
            role_id = getIntent().getExtras().getString("role_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communication_Staff_Message_Home.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/communication_staff_message_home.php");
            } else {
                Toast.makeText(Communication_Staff_Message_Home.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
    }


    void updatedata(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_Staff_Message_Home_JSONParser.parserFeed(arg0);
                        listview.setAdapter(new Individual_message_adapter(Communication_Staff_Message_Home.this,feedslist));
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Communication_Staff_Message_Home.this, Communication_Staff_Message_Home_Individual_Send.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("parent_id", feedslist.get(position).getId());
                                intent.putExtra("parent_name", feedslist.get(position).getName());
                                intent.putExtra("student_id", feedslist.get(position).getStudent_id());
                                intent.putExtra("student_name", feedslist.get(position).getStudent_name());
                                intent.putExtra("role_id",role_id);
                                Communication_Staff_Message_Home.this.finish();
                                startActivity(intent);
                                Communication_Staff_Message_Home.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });
                       // updatedisplay();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_Staff_Message_Home.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Message_Home.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Staff_Message_Home.this);
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
                                        Intent intent = new Intent(Communication_Staff_Message_Home.this, SplashScreen.class);
                                        Communication_Staff_Message_Home.this.finish();
                                        startActivity(intent);
                                        Communication_Staff_Message_Home.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("school_id", school_id);
                params.put("email", user_email);
//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
//                Log.d("student_id", student_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            LinearLayout ll_main = (LinearLayout) findViewById(R.id.communication_staff_message_home_layout);
            LinearLayout ll_main2 = (LinearLayout) findViewById(R.id.communication_staff_message_home_layout);
            for (final Communication_Staff_Message_Home_Model flower : feedslist) {

                TextView tv = new TextView(Communication_Staff_Message_Home.this);
                tv.setId(Integer.valueOf(flower.getId()));
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv.setTextAppearance(Communication_Staff_Message_Home.this, R.style.SimpleTextviewStyle);
                if (flower.getStatus().equals("0")) {
                    tv.setText(flower.getName() + " ( " + flower.getStudent_name() + " ) ");
                } else if (!flower.getStatus().equals("")) {
                    tv.setText(flower.getName() + " ( " + flower.getStudent_name() + " ) " + " ( " + flower.getStatus() + " ) ");
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tv.setText(flower.getName() + " ( " + flower.getStudent_name() + " ) ");
                }
                tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Communication_Staff_Message_Home.this, Communication_Staff_Message_Home_Individual_Send.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("parent_id", flower.getId());
                        intent.putExtra("parent_name", flower.getName());
                        intent.putExtra("student_id", flower.getStudent_id());
                        intent.putExtra("student_name", flower.getStudent_name());
                        intent.putExtra("role_id",role_id);
                        Communication_Staff_Message_Home.this.finish();
                        startActivity(intent);
                        Communication_Staff_Message_Home.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                if (flower.getStatus().equals("0")) {
                    ll_main2.addView(tv);
                } else if (!flower.getStatus().equals("")) {
                    ll_main.addView(tv);
                } else {
                    ll_main2.addView(tv);
                }
            }
        } else {
            Toast.makeText(Communication_Staff_Message_Home.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
        progress.hide();
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
                Intent intent = new Intent(Communication_Staff_Message_Home.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                Communication_Staff_Message_Home.this.finish();
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
        Intent intent = new Intent(Communication_Staff_Message_Home.this, MainActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("redirection", "Communications");
        Communication_Staff_Message_Home.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
