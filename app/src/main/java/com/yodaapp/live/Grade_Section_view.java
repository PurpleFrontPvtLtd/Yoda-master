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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.HomePageList;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Grade_section_Model;
import com.yodaapp.live.parsers.Grade_Section_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Grade_Section_view extends Activity {


    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "", section_head="",section_head_id="";
    String section_id = "", section_name = "";
    List<Grade_section_Model> feedslist;
    int min_role = 0;
    ListView listview1,listview2;
    LinearLayout linear,remove_student,add_stud;

    ImageView addsub;

    ArrayList<String> sujectList=new ArrayList<>();
    ArrayList<String> sujectIdList=new ArrayList<>();
    ArrayList<String> studentList=new ArrayList<>();
    ArrayList<String> subjectTeacherList=new ArrayList<>();
    ArrayList<String> TeacherIdList=new ArrayList<>();
    ArrayList<String> studentIdList=new ArrayList<>();
    Activity thisActivity=this;
    LinearLayout remove_layout;

    String grade_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_section_view);

        linear = (LinearLayout)findViewById(R.id.sub_assign_layout);
        remove_student = (LinearLayout)findViewById(R.id.remove_student);
        add_stud = (LinearLayout)findViewById(R.id.add_stud);
//        linearadd = (LinearLayout)findViewById(R.id.crtPar);
//        linearexist = (LinearLayout)findViewById(R.id.crtExt);
//        lineardeact = (LinearLayout)findViewById(R.id.crtInact);

        listview1=(ListView)findViewById(R.id.listview);
        listview2=(ListView)findViewById(R.id.listview2);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(thisActivity,SectionSubjectView.class);
                intent.putExtra("subject_name",sujectList.get(position));
                intent.putExtra("subject_id",sujectIdList.get(position));
                intent.putExtra("teacher_id",TeacherIdList.get(position));

                intent.putExtra("teacher_name",subjectTeacherList.get(position));
                intent.putExtra("branch_id", branch_id);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("grade_id", grade_id);
                intent.putExtra("grade_name", grade_name);
                intent.putExtra("section_id", section_id);
                intent.putExtra("section_name", section_name);
                intent.putExtra("min_role", min_role);


                startActivity(intent);
                finish();

            }
        });
        addsub = (ImageView)findViewById(R.id.addsub);
        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            section_id = getIntent().getExtras().getString("section_id");
            section_name = getIntent().getExtras().getString("section_name");
            min_role = getIntent().getExtras().getInt("min_role");
            section_head = getIntent().getExtras().getString("section_head");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (min_role == 0 || min_role >=4) {

            linear.setVisibility(View.GONE);
            addsub.setVisibility(View.GONE);
            add_stud.setVisibility(View.GONE);
            remove_student.setVisibility(View.GONE);
//            linearadd.setVisibility(View.GONE);
//            lineardeact.setVisibility(View.GONE);
//            linearexist.setVisibility(View.GONE);
            //Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
        }

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

        progress = new ProgressDialog(Grade_Section_view.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        TextView tv = (TextView) findViewById(R.id.grade_section_heading_name);
        tv.setText(section_name);

        remove_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Grade_Section_view.this, RemoveStudent.class);


                intent.putExtra("grade_student",grade_student);
                intent.putExtra("section_id",section_id);
                intent.putExtra("section_name",section_name);
                intent.putExtra("branch_id", branch_id);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("grade_id", grade_id);
                intent.putExtra("grade_name", grade_name);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role", min_role);
                intent.putExtra("section_head", section_head);
                intent.putExtra("section_head_id", "");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });

        TextView b = (TextView) findViewById(R.id.grade_section_student_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Grade_Section_view.this, Section_Student_Assign.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("section_id", section_id);
                    intent.putExtra("section_name", section_name);
                    intent.putExtra("min_role", min_role);
                    Grade_Section_view.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Grade_Section_view.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }
            }
        });

        //TextView b2 = (TextView) findViewById(R.id.grade_section_view_add_subject);
        addsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Grade_Section_view.this, Grade_Section_Add_Subject.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("section_id", section_id);
                    intent.putExtra("section_name", section_name);
                    intent.putExtra("min_role", min_role);
                    Grade_Section_view.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Grade_Section_view.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }
            }
        });
        if (school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Grade_Section_view.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/section_view.php");
            } else {
                Toast.makeText(Grade_Section_view.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
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
                        feedslist = Grade_Section_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("Error", arg0.getMessage());
                        Toast.makeText(Grade_Section_view.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Grade_Section_view.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Section_view.this);
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
                                        Intent intent = new Intent(Grade_Section_view.this, Branch_Detail_View.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("branch_id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Grade_Section_view.this.finish();
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
                params.put("section_id", section_id);
                params.put("section_name", section_name);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

    }


    void updateddisplay() {
        progress.show();
        if (feedslist != null) {
            for (final Grade_section_Model flower : feedslist) {

                section_head = flower.getSection_head_name();
                section_head_id = flower.getSection_head_id();
                String section_grade_name = flower.getGrade_name();
                String section_branch_name = flower.getBranch_name();

                TextView tv = (TextView) findViewById(R.id.grade_section_head);
                if (!section_head.equals("null")) {
                    tv.setText(section_head);
                }

                TextView tv2 = (TextView) findViewById(R.id.grade_section_name);
                tv2.setText(grade_name);

                TextView tv3 = (TextView) findViewById(R.id.grade_branch_name);
                tv3.setText(branch_name);


                String grade_subject = flower.getSubjects();

                try {
                    JSONArray ar = new JSONArray(grade_subject);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        String section_id = obj.getString("subject_id");
                        final String section_name = obj.getString("subject_name");
                        final String subject_teacher_id = obj.getString("subject_teacher_id");
                        final String subject_teacher = obj.getString("subject_teacher");
                        sujectList.add(section_name);
                        subjectTeacherList.add(subject_teacher);
                        TeacherIdList.add(subject_teacher_id);
                        sujectIdList.add(section_id);
//                        Log.d("sections", obj.getString("subject_name"));
                    /*    LinearLayout ll = (LinearLayout) findViewById(R.id.grade_subject_layout);

                        final TextView b2 = new TextView(Grade_Section_view.this);
                        b2.setId(Integer.valueOf(section_id));
                        b2.setText(section_name);
                        Drawable left = getResources().getDrawable(R.drawable.black_dot);
                        b2.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                        b2.setTextAppearance(Grade_Section_view.this, R.style.SimpleTextviewStyle);
                        ll.addView(b2);
          */          }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }


                grade_student = flower.getStudents();

                try {
                    JSONArray ar = new JSONArray(grade_student);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        final String student_id = obj.getString("student_id");
                        final String student_name = obj.getString("student_name");
                        studentIdList.add(student_id);
                        studentList.add(student_name);

                    }

                    if (studentIdList.size()==0){
                        remove_student.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
//                    e.printStackTrace();
                }

                listview1.setAdapter(new HomePageList(Grade_Section_view.this,sujectList,subjectTeacherList));
                listview2.setAdapter(new HomePageList(Grade_Section_view.this,studentList));
                listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Grade_Section_view.this, Section_Student_View.class);
                        intent.putExtra("branch_id", branch_id);
                        intent.putExtra("branch_name", branch_name);
                        intent.putExtra("grade_id", grade_id);
                        intent.putExtra("grade_name", grade_name);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("section_id", section_id);
                        intent.putExtra("section_name", section_name);

                        intent.putExtra("student_id", studentIdList.get(position));
                        intent.putExtra("student_name", studentList.get(position));
                        intent.putExtra("min_role", min_role);
                        intent.putExtra("redirection","gradestudents");
                        Grade_Section_view.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                    }
                });

            }
        } else {
            Toast.makeText(Grade_Section_view.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
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
    void removeSection() {
        progress.show();

        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_reference) + "home/remove_section.php",

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                builder.setMessage(getResources().getString(R.string.section_remove_success))
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
                params.put("section_id", section_id);


                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.calender_event_edit_delete: {


                if (!(studentIdList.size()==0)){

                    Toast.makeText(thisActivity,"Please remove all students from section and try again",Toast.LENGTH_LONG).show();
                }else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(Grade_Section_view.this);

                    builder.setMessage(getResources().getString(R.string.confirm_remove_section))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeSection();
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

            case android.R.id.home:
                Intent intent = new Intent(Grade_Section_view.this, Grade_Detail_View.class);
                intent.putExtra("branch_id", branch_id);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("grade_id", grade_id);
                intent.putExtra("grade_name", grade_name);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("section_head", section_head);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role", min_role);
                Grade_Section_view.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;

            case R.id.edit:

                Intent intent1=new Intent(thisActivity,Section_Creation.class);
                intent1.putExtra("branch_id", branch_id);
                intent1.putExtra("branch_name", branch_name);
                intent1.putExtra("user_id", user_id);
                intent1.putExtra("school_id", school_id);
                intent1.putExtra("email", user_email);
                intent1.putExtra("grade_id", grade_id);
                intent1.putExtra("grade_name", grade_name);
                intent1.putExtra("section_id", section_id);
                intent1.putExtra("section_name", section_name);
                intent1.putExtra("section_head", section_head);
                intent1.putExtra("min_role", min_role);
                intent1.putExtra("section_head_id", section_head_id);
                intent1.putExtra("redirection", "edit_grade_section");

                startActivity(intent1);

                return true;

            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Grade_Section_view.this, Grade_Detail_View.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("section_id", section_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("min_role", min_role);
        intent.putExtra("section_name", section_name);
        intent.putExtra("section_head",section_head);
        intent.putExtra("redirection","edit_grade_section");


        Grade_Section_view.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
