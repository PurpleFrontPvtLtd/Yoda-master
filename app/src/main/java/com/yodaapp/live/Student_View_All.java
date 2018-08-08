package com.yodaapp.live;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ViewAllStudentsAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Student_View_All_Model;
import com.yodaapp.live.parsers.Student_View_All_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Student_View_All extends Activity {


    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "" ;
    List<Student_View_All_Model> feedslist;
    ListView listview;
    int min_role = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view_all1);
        listview=(ListView)findViewById(R.id.listview);
        ImageButton b = (ImageButton) findViewById(R.id.addstudentbtn);



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

        progress = new ProgressDialog(Student_View_All.this);
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
            min_role = getIntent().getExtras().getInt("min_role");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Student_View_All.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/student_view_all.php");
            } else {
                Toast.makeText(Student_View_All.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }



        if(min_role ==0 || min_role >=4)
        {
            b.setVisibility(View.GONE);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Student_View_All.this, Student_Creation.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("role_id",min_role);
                    intent.putExtra("redirection","Allstudents");
                    Student_View_All.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Student_View_All.this,getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void updatedisplay()
    {
        progress.show();
        if(feedslist != null) {
            String prevoius_student = "";
            String student_id = "";
            for(final Student_View_All_Model flower : feedslist)
            {
                student_id = flower.getStudent_id();
                LinearLayout ll = (LinearLayout) findViewById(R.id.students_view_all_layout);
                LinearLayout ll_hor = new LinearLayout(Student_View_All.this);
                LinearLayout.LayoutParams p_hor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor.setLayoutParams(p_hor);

                TextView tv = new TextView(Student_View_All.this);
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv.setTextAppearance(Student_View_All.this, R.style.SimpleTextviewStyle);
                tv.setId(Integer.valueOf(flower.getStudent_id()));
                tv.setText(flower.getStudent_name());
                if(prevoius_student.equals(student_id)) {
                tv.setVisibility(View.GONE);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("role_id", "" + min_role);

                        if (flower.getGrade_id().equals("null") && flower.getSection_id().equals("null")) {
                            Toast.makeText(Student_View_All.this,"NO section is assigned to this student",Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(Student_View_All.this, Section_Student_View.class);
                            intent.putExtra("branch_id", flower.getBranch_id());
                            intent.putExtra("branch_name", flower.getBranch_name());
                            intent.putExtra("grade_id", flower.getGrade_id());
                            intent.putExtra("grade_name", flower.getGrade_name());
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("section_id", flower.getSection_id());
                            intent.putExtra("section_name", flower.getSection_name());
                            intent.putExtra("student_id", flower.getStudent_id());
                            intent.putExtra("student_name", flower.getStudent_name());
                            intent.putExtra("min_role", min_role);
                            intent.putExtra("redirection", "home");
                            Student_View_All.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    }
                });
                ll.addView(tv);


                TextView tv2 = new TextView(Student_View_All.this);
                tv2.setTextAppearance(Student_View_All.this, R.style.SimpleTextviewStyle);
                if(prevoius_student.equals(student_id)) {
                    tv2.setPadding(5, 0, 0, 0);
                }
                tv2.setText("( " + flower.getGrade_name() + " - ");
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("role_id", "" + min_role);
                        if (flower.getGrade_id().equals("null") && flower.getSection_id().equals("null")) {
                            Toast.makeText(Student_View_All.this,"NO section is assigned to this student",Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(Student_View_All.this, Section_Student_View.class);
                            intent.putExtra("branch_id", flower.getBranch_id());
                            intent.putExtra("branch_name", flower.getBranch_name());
                            intent.putExtra("grade_id", flower.getGrade_id());
                            intent.putExtra("grade_name", flower.getGrade_name());
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("section_id", flower.getSection_id());
                            intent.putExtra("section_name", flower.getSection_name());
                            intent.putExtra("student_id", flower.getStudent_id());
                            intent.putExtra("student_name", flower.getStudent_name());

                            intent.putExtra("min_role", min_role);
                            intent.putExtra("redirection", "allstudents");
                            Student_View_All.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    }
                });

                TextView tv3 = new TextView(Student_View_All.this);
                    tv3.setTextAppearance(Student_View_All.this, R.style.SimpleTextviewStyle);
                tv3.setText(flower.getSection_name() + " ) ");

                    if(flower.getGrade_id().equals("null") && flower.getSection_id().equals("null")) {
                    tv2.setText(" ( " + "Unassigned" + " ) ");
                    ll_hor.addView(tv2);
                } else {
                    ll_hor.addView(tv2);
                    ll_hor.addView(tv3);
                }
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick(View v) {
                        Log.d("role_id","" + min_role);
                        if (flower.getGrade_id().equals("null") && flower.getSection_id().equals("null")) {
                            Toast.makeText(Student_View_All.this,"NO section is assigned to this student",Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(Student_View_All.this,Section_Student_View.class);
                            intent.putExtra("branch_id", flower.getBranch_id());
                            intent.putExtra("branch_name", flower.getBranch_name());
                            intent.putExtra("grade_id", flower.getGrade_id());
                            intent.putExtra("grade_name", flower.getGrade_name());
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("section_id", flower.getSection_id());
                            intent.putExtra("section_name", flower.getSection_name());
                            intent.putExtra("student_id", flower.getStudent_id());
                            intent.putExtra("student_name", flower.getStudent_name());
                            intent.putExtra("min_role", min_role);
                            intent.putExtra("redirection","allstudents");
                            Student_View_All.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    }
                });
                ll.addView(ll_hor);
                prevoius_student = student_id;
            }
        }
        progress.hide();
    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("stu_response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Student_View_All_JSONParser.parserFeed(arg0);
                        listview.setAdapter(new ViewAllStudentsAdapter(Student_View_All.this,feedslist));
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (feedslist.get(position).getGrade_id().equals("null") && feedslist.get(position).getSection_id().equals("null")) {
                                    Toast.makeText(Student_View_All.this,"NO section is assigned to this student",Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent(Student_View_All.this,Section_Student_View.class);
                                    intent.putExtra("branch_id", feedslist.get(position).getBranch_id());
                                    intent.putExtra("branch_name", feedslist.get(position).getBranch_name());
                                    intent.putExtra("grade_id", feedslist.get(position).getGrade_id());
                                    intent.putExtra("grade_name", feedslist.get(position).getGrade_name());
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("section_id", feedslist.get(position).getSection_id());
                                    intent.putExtra("section_name", feedslist.get(position).getSection_name());
                                    intent.putExtra("student_id", feedslist.get(position).getStudent_id());
                                    intent.putExtra("student_name", feedslist.get(position).getStudent_name());
                                    intent.putExtra("min_role", min_role);
                                    intent.putExtra("redirection","allstudents");
                                    Student_View_All.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                            }
                        });
                      //  updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Student_View_All.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_View_All.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_View_All.this);
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
                                        Intent intent = new Intent(Student_View_All.this, Existinguser.class);
                                        Student_View_All.this.finish();
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

                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("user_email", StaticVariable.email);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
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
                Intent intent = new Intent(Student_View_All.this, MainActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Student_View_All.this.finish();
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
        Intent intent = new Intent(Student_View_All.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        Student_View_All.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
