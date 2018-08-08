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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Branch_Details_Model;
import com.yodaapp.live.parsers.Branch_Details_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communication_Staff_Section extends Activity {

    ProgressDialog progress;
    List<Branch_Details_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "";
    String school_name = "", role_id = "", branch_id = "", branch_name = "";
    String grade_id = "", grade_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_staff_section);

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

        progress = new ProgressDialog(Communication_Staff_Section.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
            role_id = getIntent().getExtras().getString("role_id");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView tv = (TextView) findViewById(R.id.communication_branches_section_school_name);
        tv.setText(school_name + " > " + branch_name + " > " + grade_name);


        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communication_Staff_Section.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/communication_section_details.php");
            } else {
                Toast.makeText(Communication_Staff_Section.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
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
                        feedslist = Branch_Details_JSONParser.parserFeed(arg0);
                        updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_Staff_Section.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Section.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Staff_Section.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/communication_section_details.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Communication_Staff_Section.this, SplashScreen.class);
                                        Communication_Staff_Section.this.finish();
                                        startActivity(intent);
                                        Communication_Staff_Section.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("role_id", role_id);
                params.put("branch_id", branch_id);
                params.put("grade_id", grade_id);

//                Log.d("school_id", school_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_email);
//                Log.d("role_id", role_id);
//                Log.d("branch_id", branch_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            for (final Branch_Details_Model flower : feedslist) {

                String branches_id = flower.getId();
                String branches_name = flower.getName();
                String branches_type = flower.getType();
                LinearLayout ll = (LinearLayout) findViewById(R.id.communication_branch_section_address_main_layout);

                TextView tv = new TextView(Communication_Staff_Section.this);
                tv.setText(branches_name);
                tv.setTextAppearance(Communication_Staff_Section.this, R.style.SimpleTextviewStyle_22);
                ll.addView(tv);

                final TextView b = new TextView(Communication_Staff_Section.this);
                b.setId(Integer.valueOf(branches_id));
                b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                b.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                b.setTextAppearance(Communication_Staff_Section.this, R.style.SimpleTextviewStyle);
                if (flower.getUnread().equals("")) {
                    b.setText(getResources().getString(R.string.messages));
                }
                if (!flower.getUnread().equals("0")) {
                    b.setText(getResources().getString(R.string.messages) + " ( " + flower.getUnread() + " ) ");
                    b.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    b.setText(getResources().getString(R.string.messages));
                }
                b.setPadding(0, 0, 0, 20);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Communication_Staff_Section.this, Communication_Staff_Section_Send.class);
                        intent.putExtra("branch_id", branch_id);
                        intent.putExtra("branch_name", branch_name);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("school_name", school_name);
                        intent.putExtra("role_id", role_id);
                        intent.putExtra("grade_id", grade_id);
                        intent.putExtra("grade_name", grade_name);
                        intent.putExtra("section_id", String.valueOf(b.getId()));
                        intent.putExtra("section_name", flower.getName());
                        Communication_Staff_Section.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                ll.addView(b);
            }
        } else {
            Toast.makeText(Communication_Staff_Section.this, "No Response from server", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(Communication_Staff_Section.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                Communication_Staff_Section.this.finish();
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
        Intent intent = new Intent(Communication_Staff_Section.this, Communication_Staff_Grades.class);
        intent.putExtra("id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("school_name", school_name);
        intent.putExtra("role_id", role_id);
        Communication_Staff_Section.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
