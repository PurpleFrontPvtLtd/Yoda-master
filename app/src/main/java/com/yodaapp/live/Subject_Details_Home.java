package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Subject_Details_Home_Model;
import com.yodaapp.live.parsers.Subject_Details_Home_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Subject_Details_Home extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    int min_role = 0;
    String subject_id = "";
    List<Subject_Details_Home_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_details_home);


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

        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            min_role = getIntent().getExtras().getInt("min_role");
            subject_id = getIntent().getExtras().getString("subject_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d("school_id", school_id);
//        Log.d("user_id", user_id);
//        Log.d("user_email", user_email);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Subject_Details_Home.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if(school_id.equals("") || user_id.equals("") || subject_id.equals("")) {
            Toast.makeText(Subject_Details_Home.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/subject_home_details.php");
            } else {
                Toast.makeText(Subject_Details_Home.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
    }

    void updatedata(final String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response",arg0);
                        feedslist = Subject_Details_Home_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Subject_Details_Home.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Subject_Details_Home.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Subject_Details_Home.this);
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
                                        Intent intent = new Intent(Subject_Details_Home.this, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        Subject_Details_Home.this.finish();
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
                params.put("subject_id",subject_id);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updateddisplay()
    {
        progress.show();
        if (feedslist != null) {
            for (final Subject_Details_Home_Model flower : feedslist) {

                TextView tv = (TextView) findViewById(R.id.subject_details_home_name);
                tv.setText(flower.getSubject());

                LinearLayout ll = (LinearLayout) findViewById(R.id.subject_details_home_layout);

                        if(!flower.getBranch().equals("null")) {
                            TextView b3 = new TextView(Subject_Details_Home.this);
                            b3.setText("Branch - " + flower.getBranch());
                            b3.setTextAppearance(Subject_Details_Home.this, R.style.SimpleTextviewStyle);
                            ll.addView(b3);
                            if (!flower.getGrade().equals("null")) {
                                TextView b4 = new TextView(Subject_Details_Home.this);
                                b4.setText("Grade - " + flower.getGrade());
                                b4.setTextAppearance(Subject_Details_Home.this, R.style.SimpleTextviewStyle);
                                ll.addView(b4);

                                if(!flower.getSection().equals("null")) {
                                    TextView b5 = new TextView(Subject_Details_Home.this);
                                    b5.setText("Section - " + flower.getSection());
                                    b5.setTextAppearance(Subject_Details_Home.this, R.style.SimpleTextviewStyle);
                                    ll.addView(b5);
                                }
                            }
                        }

                        ImageView divider3 = new ImageView(Subject_Details_Home.this);
                        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                        lp3.setMargins(0, 0, 0, 0);
                        divider3.setLayoutParams(lp3);
                        divider3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ll.addView(divider3);

                        ImageView divider2 = new ImageView(Subject_Details_Home.this);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                        lp2.setMargins(0, 0, 0, 0);
                        divider2.setLayoutParams(lp2);
                        divider2.setBackgroundColor(Color.parseColor("#000000"));
                        ll.addView(divider2);

                        ImageView divider4 = new ImageView(Subject_Details_Home.this);
                        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                        lp4.setMargins(0, 0, 0, 0);
                        divider4.setLayoutParams(lp4);
                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ll.addView(divider4);

                    }

            }
        else {
            Toast.makeText(Subject_Details_Home.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
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
                Intent intent = new Intent(Subject_Details_Home.this, MainActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Subject_Details_Home.this.finish();
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
        Intent intent = new Intent(Subject_Details_Home.this, MainActivity.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        Subject_Details_Home.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
