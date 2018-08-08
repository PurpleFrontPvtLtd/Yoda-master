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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.HomePageList;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.GradeView_Model;
import com.yodaapp.live.parsers.GradeView_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Grade_Detail_View extends Activity {

    String branch_id = "", branch_name = "", grade_id = "", grade_name = "",branch_head="";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    List<GradeView_Model> feedslist;
    int min_role = 0;
    private String tag_string_req_recieve2 = "string_req_recieve2";
    ListView listview;
    LinearLayout linear;
    String grade_head_master = "",grade_head_id="",section_id = "",section_name = "",section_head ="",redirection ="";
    ImageView addgrade;
    ArrayList<String> gradeList=new ArrayList<>();
    ArrayList<String> sectionIdList=new ArrayList<>();
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_detail_view);
        listview=(ListView)findViewById(R.id.listview);
        linear = (LinearLayout)findViewById(R.id.grd_assign_layout);
        addgrade=(ImageView)findViewById(R.id.addgrade);

        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            min_role = getIntent().getExtras().getInt("min_role");
            branch_head = getIntent().getExtras().getString("branch_head");
            section_id = getIntent().getExtras().getString("section_id");
            section_name = getIntent().getExtras().getString("section_name");
            section_head = getIntent().getExtras().getString("section_head");
            grade_head_master = getIntent().getExtras().getString("grade_head_master");
            redirection = getIntent().getExtras().getString("redirection");



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

        progress = new ProgressDialog(Grade_Detail_View.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if (school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Grade_Detail_View.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/grade_view.php");
            } else {
                Toast.makeText(Grade_Detail_View.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        Log.d("ROL1","ROL1"+min_role);


        if(min_role == 0 || min_role >=4)
        {

            linear.setVisibility(View.GONE);
            addgrade.setVisibility(View.GONE);
        }

        addgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Grade_Detail_View.this, Section_Creation.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("section_name", section_name);
                    intent.putExtra("section_id", section_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("email", user_email);
                    intent.putExtra("min_role", min_role);
                    intent.putExtra("grade_head_master", "");
                    intent.putExtra("grade_head_id", "");
                    intent.putExtra("redirection", "grade_detail");

                    Grade_Detail_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Grade_Detail_View.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }

            }
        });

        TextView b = (TextView) findViewById(R.id.grade_detail_view_add_section);
        b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Grade_Detail_View.this, Section_Creation.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("min_role", min_role);
                    Grade_Detail_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Grade_Detail_View.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
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
                      Log.d("grade_response", arg0);
                        feedslist = GradeView_JSONParser.parserFeed(arg0);


                        updateddisplay();
                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Log.d("Error", arg0.getMessage());
                        Toast.makeText(Grade_Detail_View.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Grade_Detail_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Detail_View.this);
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
                                        Intent intent = new Intent(Grade_Detail_View.this, Branch_Detail_View.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("branch_id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Grade_Detail_View.this.finish();
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

                params.put("user_id", StaticVariable.user_id);

                params.put("user_email", "af");

                params.put("branch_id", branch_id);

                params.put("branch_name", branch_name);

                params.put("grade_id", grade_id);

                params.put("grade_name", grade_name);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.v("response", ""+response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updateddisplay() {
        progress.show();
        if (feedslist != null) {
            for (final GradeView_Model flower : feedslist) {

                grade_head_master = flower.getHead_master_name();
                grade_head_id = flower.getHead_master_id();
              Log.v("grade_head_id",grade_head_id);
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

                        gradeList.add(section_name);
                        sectionIdList.add(section_id);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }
            listview.setAdapter(new HomePageList(Grade_Detail_View.this,gradeList));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Grade_Detail_View.this, Grade_Section_view.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("section_id", sectionIdList.get(position));
                    intent.putExtra("section_name", gradeList.get(position));
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("min_role", min_role);
                    Grade_Detail_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });

        } else {
            Toast.makeText(Grade_Detail_View.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calender_event_edit_menu, menu);

        inflater.inflate(R.menu.viewedit, menu);


        if(min_role == 0 || min_role >=4)
        {
            menu.findItem(R.id.calender_event_edit_delete).setVisible(false);
            menu.findItem(R.id.edit).setVisible(false);

        }

        else
        {
            menu.findItem(R.id.calender_event_edit_delete).setVisible(true);
            menu.findItem(R.id.edit).setVisible(true);

        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            case R.id.calender_event_edit_delete: {


                if (!(sectionIdList.size()==0)){

                    Toast.makeText(Grade_Detail_View.this,"Please remove all sections from grade and try again",Toast.LENGTH_LONG).show();
                }else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Detail_View.this);

                    builder.setMessage(getResources().getString(R.string.confirm_remove_grade))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeGrade();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                return true;
            }

            case R.id.edit:

                Intent intent1= new Intent(thisActivity,Grade_Creation.class);

                intent1.putExtra("branch_id",branch_id);
                intent1.putExtra("branch_name",branch_name);
                intent1.putExtra("school_id",school_id);
                intent1.putExtra("user_id",user_id);
                intent1.putExtra("email",user_email);
                intent1.putExtra("grade_id",grade_id);
                intent1.putExtra("grade_name",grade_name);
                intent1.putExtra("section_name",section_name);
                intent1.putExtra("section_head",section_head);
                intent1.putExtra("grade_head_master",grade_head_master);
                intent1.putExtra("redirection",redirection);
                intent1.putExtra("section_id",section_id);
                intent1.putExtra("min_role",min_role);
                intent1.putExtra("branch_head",branch_head);
                intent1.putExtra("grade_head_id",grade_head_id);
                intent1.putExtra("grade_head_master",grade_head_master);
                intent1.putExtra("redirection","edit_grade_section");

                startActivity(intent1);
                finish();



                return true;

            default:
                return true;
        }
    }

    void removeGrade() {
        progress.show();

        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_reference) + "home/remove_grade.php",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");

                        try {
                            JSONObject obj=new JSONObject(arg0);

                            String status=obj.getString("sucess");

                            if (status.equals("Profile updated")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Detail_View.this);
                                builder.setMessage(getResources().getString(R.string.grade_remove_success))
                                        .setCancelable(false)
                                        .setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(thisActivity, MainActivity.class);
                                                intent.putExtra("user_id", user_id);
                                                intent.putExtra("school_id", school_id);
                                                intent.putExtra("email", user_email);
                                                thisActivity.finish();
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_View_All.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage(getResources().getString(R.string.error_config))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, Existinguser.class);
                                        finish();
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
                params.put("grade_id", grade_id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Grade_Detail_View.this, Branch_Detail_View.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("min_role", min_role);
        Grade_Detail_View.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
