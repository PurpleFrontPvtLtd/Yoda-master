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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.TeacherDetailsAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.ParentDetail_Model;
import com.yodaapp.live.model.Staff_Details_Model;
import com.yodaapp.live.parsers.Staff_Details_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Staff_Details extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    int min_role = 0;
    String staff_id = "";
    List<Staff_Details_Model> feedslist;
    List<ParentDetail_Model> feedslist22=new ArrayList<>();
    Button inactivate_staff,assign_role;
    Activity thisActivity=this;
    ExpandableListView listview;
    LinearLayout layout;
    TextView no_role_staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_details);
        inactivate_staff=(Button)findViewById(R.id.inactivate_staff);
        assign_role=(Button)findViewById(R.id.assign_role);
        listview=(ExpandableListView)findViewById(R.id.listview);
        layout=(LinearLayout)findViewById(R.id.layout);

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
            staff_id = getIntent().getExtras().getString("staff_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d("school_id", school_id);
//        Log.d("user_id", user_id);
//        Log.d("user_email", user_email);

        Log.d("MINROLE1","MINROLE1"+min_role);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Staff_Details.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        no_role_staff=(TextView)findViewById(R.id.no_role);

        layout.setVisibility(View.INVISIBLE);


        if(min_role < 4)
        {
            inactivate_staff.setVisibility(View.VISIBLE);
            inactivate_staff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedslist22.size() == 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);

                        builder.setMessage(getResources().getString(R.string.confirm_inactivate_staff))
                                .setCancelable(false)
                                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        staffInactivate();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        Toast.makeText(thisActivity,"Staff cannot be inactivate if there is roles for staff",Toast.LENGTH_LONG).show();
                    }
                }
            });

         //   assign_role.setVisibility(View.VISIBLE);
            assign_role.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Staff_Details.this, Role_Assign.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("staff_id", staff_id);
                    intent.putExtra("redirection","staff_details");
                    Staff_Details.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                }
            });

        }
//        else {
//            Toast.makeText(thisActivity,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
//
//        }

//        inactivate_staff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.d("MINROLE","MINROLE"+min_role);
//
//                if (min_role < 4) {
//                    inactivate_staff.setVisibility(View.VISIBLE);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
//                    builder.setMessage(getResources().getString(R.string.confirm_inactivate_staff))
//                            .setCancelable(false)
//                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    staffInactivate();
//                                }
//                            })
//                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                }else {
//                    Toast.makeText(thisActivity,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });


        if(school_id.equals("") || user_id.equals("") || staff_id.equals("")) {
            Toast.makeText(Staff_Details.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/staff_details.php");
            } else {
                Toast.makeText(Staff_Details.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
    }


    void staffInactivate() {
        progress.show();

        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_reference) + "home/inactivate_staff.php",

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
                                builder.setMessage(getResources().getString(R.string.staff_inactivate_update))
                                        .setCancelable(false)
                                        .setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Staff_Details.this, MainActivity.class);
                                                intent.putExtra("user_id", user_id);
                                                intent.putExtra("school_id", school_id);
                                                intent.putExtra("email", user_email);
                                                Staff_Details.this.finish();
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
                params.put("staff_id", staff_id);
                params.put("status", "1");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updatedata(final String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response",arg0);
                        feedslist = Staff_Details_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Staff_Details.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Staff_Details.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Staff_Details.this);
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
                                        Intent intent = new Intent(Staff_Details.this, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        Staff_Details.this.finish();
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
                params.put("staff_id",staff_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updateddisplay()
    {
        progress.show();
        if (feedslist != null) {
            for (final Staff_Details_Model flower : feedslist) {

                TextView tv = (TextView) findViewById(R.id.staff_details_name);
                if (!flower.getName().equals("null")) {
                    layout.setVisibility(View.VISIBLE);

                    tv.setText(flower.getName());
                } else {
                    layout.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("No roles Exists for current staff. Please assign role")
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(Staff_Details.this, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    Staff_Details.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_right, R.anim.right_left);

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                   // tv.setText(getResources().getString(R.string.staff_noroles_selected));
                }

                TextView tv2 = (TextView) findViewById(R.id.staff_details_email);
                if (!flower.getEmail().equals("null")) {
                    tv2.setText(flower.getEmail());
                }

                TextView tv3 = (TextView) findViewById(R.id.staff_details_contact);
                if (!flower.getContact().equals("null")) {
                    tv3.setText(flower.getContact());
                }


                String branch_grade = flower.getDetails();
                try {
                    JSONArray ar = new JSONArray(branch_grade);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        String role = obj.getString("role");
                        String branch_name = obj.getString("branch_name");
                        String grade = obj.getString("grade");
                        String section = obj.getString("section");
                        String subject = obj.getString("subject");

                        ParentDetail_Model model=new ParentDetail_Model();
                        model.setBranch(branch_name);
                        model.setRole(role);
                        model.setGrade(grade);
                        model.setSection(section);
                        model.setSubject(subject);

                        feedslist22.add(model);



                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }

                if (feedslist22.size()==0){
                    no_role_staff.setVisibility(View.VISIBLE);
                }
                else {
                    no_role_staff.setVisibility(View.GONE);

                }

                listview.setAdapter(new TeacherDetailsAdapter(thisActivity,feedslist22));

            }
        }
        else {
            Toast.makeText(Staff_Details.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(Staff_Details.this, MainActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Staff_Details.this.finish();
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
        Intent intent = new Intent(Staff_Details.this, MainActivity.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        Staff_Details.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
