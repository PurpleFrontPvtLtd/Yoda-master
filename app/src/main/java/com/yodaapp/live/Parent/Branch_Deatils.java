package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import com.yodaapp.live.Parent_Model.Branch_Details_Model;
import com.yodaapp.live.Parent_Parsers.Branch_Details_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.controller.AppController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Branch_Deatils extends Activity {

    String branch_id = "", branch_name = "", school_name = "";
    private String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    List<Branch_Details_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_branch_details);

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

        try {
            branch_id = getIntent().getExtras().getString("id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
        } catch (Exception e) {
//            e.printStackTrace();
        }


//        Log.d("school_id", school_id);
//        Log.d("branch_id", branch_id);
//        Log.d("user_id", user_id);
//        Log.d("user_email", user_email);
//        Log.d("branch_name", branch_name);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(Branch_Deatils.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if(school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Branch_Deatils.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/branch_view.php");
            } else {
                Toast.makeText(Branch_Deatils.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

    }


    void updatedata(String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response",arg0);
                        feedslist = Branch_Details_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Branch_Deatils.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Branch_Deatils.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Branch_Deatils.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/branch_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Branch_Deatils.this, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        Branch_Deatils.this.finish();
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

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updateddisplay()
    {
        progress.show();
        if (feedslist != null) for (final Branch_Details_Model flower : feedslist) {

            TextView tv = (TextView) findViewById(R.id.parent_branch_details_branch_name);

            tv.setText(branch_name);

            TextView tv2 = (TextView) findViewById(R.id.parent_branch_details_branch_email);
            tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv2.setText("Email : " + flower.getBranch_email());

            TextView tv3 = (TextView) findViewById(R.id.parent_branch_details_branch_phone);
            tv3.setText("Contact : " + flower.getBranch_phone());

            TextView tv4 = (TextView) findViewById(R.id.parent_branch_details_branch_address);
            tv4.setText("Address : " + flower.getBranch_address());

            TextView tv5 = (TextView) findViewById(R.id.parent_branch_details_branch_fb);
            tv5.setPaintFlags(tv5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv5.setText("Facebook : " + flower.getBranch_facebook());

            TextView tv6 = (TextView) findViewById(R.id.parent_branch_details_branch_website);
            tv6.setPaintFlags(tv6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv6.setText("Website : " + flower.getBranch_website());

            TextView tv7 = (TextView) findViewById(R.id.parent_branch_details_branch_timing);
            tv7.setText("Timing : " + flower.getBranch_timing());

        }
        else {
            Toast.makeText(Branch_Deatils.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
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
                progress.hide();
                Intent intent = new Intent(Branch_Deatils.this, MainActivity_Parent.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                Branch_Deatils.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        progress.hide();
        super.onBackPressed();
        Intent intent = new Intent(Branch_Deatils.this, MainActivity_Parent.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("school_name",school_name);
        Branch_Deatils.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
