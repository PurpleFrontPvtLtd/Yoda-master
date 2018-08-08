package com.yodaapp.live;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.GradeView_Model;
import com.yodaapp.live.parsers.GradeView_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Grade_Detail_View1 extends Activity {

    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    List<GradeView_Model> feedslist;
    int min_role = 0;
    private String tag_string_req_recieve2 = "string_req_recieve2";
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_detail_view);
       // listview=(ListView)findViewById(R.id.)

        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            min_role = getIntent().getExtras().getInt("min_role");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.grade_detail_name);
        tv.setText(grade_name);

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

        progress = new ProgressDialog(Grade_Detail_View1.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if (school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/grade_view.php");
            } else {
                Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        TextView b = (TextView) findViewById(R.id.grade_detail_view_add_section);
        b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (min_role == 0) {
                    Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
                } else if (min_role >= 4) {
                    Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
                } else if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Grade_Detail_View1.this, Section_Creation.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("min_role", min_role);
                    Grade_Detail_View1.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = GradeView_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Log.d("Error", arg0.getMessage());
                        Toast.makeText(Grade_Detail_View1.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Grade_Detail_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Detail_View1.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/grade_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Grade_Detail_View1.this, Branch_Detail_View.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Grade_Detail_View1.this.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("branch_id", branch_id);
                params.put("branch_name", branch_name);
                params.put("grade_id", grade_id);
                params.put("grade_name", grade_name);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updateddisplay() {
        progress.show();
        if (feedslist != null) {
            for (final GradeView_Model flower : feedslist) {

                String grade_head_master = flower.getHead_master_name();
                TextView tv = (TextView) findViewById(R.id.grade_head_master_name);
                if (!grade_head_master.equals("null")) {
                    tv.setText(grade_head_master);
                }

                String grade_section = flower.getSections();

                try {
                    JSONArray ar = new JSONArray(grade_section);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        String section_id = obj.getString("id");
                        final String section_name = obj.getString("name");
//                        Log.d("sections", obj.getString("name"));
                        LinearLayout ll = (LinearLayout) findViewById(R.id.grade_section_layout);


                        final TextView b2 = new TextView(Grade_Detail_View1.this);
                        b2.setId(Integer.valueOf(section_id));
                        b2.setText(section_name);
                        b2.setTextAppearance(Grade_Detail_View1.this, R.style.SimpleTextviewStyle);
                        Drawable left = getResources().getDrawable(R.drawable.black_dot);
                        b2.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Grade_Detail_View1.this, Grade_Section_view.class);
                                intent.putExtra("branch_id", branch_id);
                                intent.putExtra("section_id", String.valueOf(b2.getId()));
                                intent.putExtra("section_name", b2.getText().toString());
                                intent.putExtra("grade_id", grade_id);
                                intent.putExtra("grade_name", grade_name);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("branch_name", branch_name);
                                intent.putExtra("min_role", min_role);
                                Grade_Detail_View1.this.finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                        ll.addView(b2);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }
        } else {
            Toast.makeText(Grade_Detail_View1.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(Grade_Detail_View1.this, Branch_Detail_View.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("id", branch_id);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("min_role", min_role);
                Grade_Detail_View1.this.finish();
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
        Intent intent = new Intent(Grade_Detail_View1.this, Branch_Detail_View.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("min_role", min_role);
        Grade_Detail_View1.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
