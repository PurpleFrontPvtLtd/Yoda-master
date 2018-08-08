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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.HomePageList;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.BranchDetail_Model;
import com.yodaapp.live.parsers.BranchDetail_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Branch_Detail_View extends Activity {

    String branch_id = "", branch_name = "";
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "",branch_head = "",branch_head_id="";
    int min_role = 0;
    List<BranchDetail_Model> feedslist;
    ExpandableListView grade_list;
    ArrayList<String> gradeNameList=new ArrayList<>();
    ArrayList<String> gradeIdList=new ArrayList<>();
    String branch_email="",branch_phone="",branch_address="",branch_fb="",branch_website="",branch_timing="";
    ImageView addgrade;
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_detail_view1);

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        getLayoutInflater().inflate(R.layout.branch_detail_view, mDrawerLayout);
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        grade_list=(ExpandableListView)findViewById(R.id.grade_list);
        addgrade=(ImageView)findViewById(R.id.addgrade);


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

            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            user_email = getIntent().getExtras().getString("email");
            min_role = getIntent().getExtras().getInt("min_role");
            branch_head = getIntent().getExtras().getString("branch_head");
            branch_head_id = getIntent().getExtras().getString("branch_head_id");


        } catch (Exception e) {
//            e.printStackTrace();
            //branch_head="";
        }
        /*Log.d("school_id", school_id);
        Log.d("branch_id", branch_id);
        Log.d("user_id", user_id);
        Log.d("user_email", user_email);
        Log.d("branch_name", branch_name);*/

        if (min_role == 0 || min_role >=4) {

            addgrade.setVisibility(View.GONE);
            //Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
        }
        else {
            addgrade.setVisibility(View.VISIBLE);

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Branch_Detail_View.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if(school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Branch_Detail_View.this,getResources().getString(R.string.unknownerror3),Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/branch_view.php");
            } else {
                Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        addgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Branch_Detail_View.this, Grade_Creation.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("min_role", min_role);
                    intent.putExtra("branch_head", branch_head);
                    intent.putExtra("branch_head_id", branch_head_id);
                    intent.putExtra("redirection", "redirection");
                    Branch_Detail_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }
            }
        });


 //       Log.d("branch_id", branch_id);
//        TextView b = (TextView) findViewById(R.id.branch_grade_creation);
//        b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (min_role == 0) {
//                    Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
//                } else if (min_role >= 4) {
//                    Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
//                } else if (min_role < 4 && min_role > 0) {
//                    Intent intent = new Intent(Branch_Detail_View.this, Grade_Creation.class);
//                    intent.putExtra("branch_id", branch_id);
//                    intent.putExtra("branch_name", branch_name);
//                    intent.putExtra("user_id", user_id);
//                    intent.putExtra("school_id", school_id);
//                    intent.putExtra("email", user_email);
//                    intent.putExtra("min_role", min_role);
//                    Branch_Detail_View.this.finish();
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                } else {
//                    Toast.makeText(Branch_Detail_View.this, getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    void updatedata(String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response",arg0);
                        feedslist = BranchDetail_JSONParser.parserFeed(arg0);
                        updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Branch_Detail_View.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Branch_Detail_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Branch_Detail_View.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/branch_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Branch_Detail_View.this, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        Branch_Detail_View.this.finish();
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
        if (feedslist != null) for (final BranchDetail_Model flower : feedslist) {

            TextView tv = (TextView) findViewById(R.id.branch_nam);
            tv.setText(branch_name);
            TextView thead = (TextView)findViewById(R.id.branch_hea);

          try {

              if (branch_head.contains("null")) {
                  thead.setText("");
              } else {
                  thead.setText(branch_head);

              }

          }catch (Exception e){
              e.printStackTrace();
          }

            TextView tv2 = (TextView) findViewById(R.id.branch_detail_email);
            tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv2.setText(flower.getBranch_email());

            branch_email = flower.getBranch_email();



            TextView tv3 = (TextView) findViewById(R.id.branch_detail_phone);
            tv3.setText(flower.getBranch_phone());

            branch_phone = flower.getBranch_phone();

            TextView tv4 = (TextView) findViewById(R.id.branch_detail_address);
            tv4.setText(flower.getBranch_address());

            branch_address = flower.getBranch_address();


            TextView tv8 = (TextView) findViewById(R.id.name);
            if(flower.getBranch_type().equals("1")) {
                tv8.setText("Main Branch");
            } else {
                tv8.setText("Regular Branch");
            }

            TextView tv5 = (TextView) findViewById(R.id.branch_detail_facebook);
            tv5.setPaintFlags(tv5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv5.setText(flower.getBranch_facebook());

            branch_fb = flower.getBranch_facebook();

            TextView tv6 = (TextView) findViewById(R.id.branch_detail_website);
            tv6.setPaintFlags(tv6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv6.setText(flower.getBranch_website());

            branch_website = flower.getBranch_website();

            TextView tv7 = (TextView) findViewById(R.id.branch_detail_timing);
            tv7.setText(flower.getBranch_timing());

            branch_timing = flower.getBranch_timing();

            String branch_grade = flower.getBranch_grade();

            try {
                JSONArray ar = new JSONArray(branch_grade);
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject obj = ar.getJSONObject(i);
                    String grade_id = obj.getString("id");
                    final String grade_name = obj.getString("name");
                    gradeNameList.add(grade_name);
                    gradeIdList.add(grade_id);
//                    Log.d("roles", obj.getString("name"));
//                    LinearLayout ll = (LinearLayout) findViewById(R.id.branch_detail_grade_layout);
//
//                    final TextView b2 = new TextView(Branch_Detail_View.this);
//                    b2.setId(Integer.valueOf(grade_id));
//                    b2.setText(grade_name);
//                    b2.setTextAppearance(Branch_Detail_View.this,R.style.SimpleTextviewStyle);
//                    Drawable left = getResources().getDrawable(R.drawable.black_dot);
//                    b2.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//                    b2.setPaintFlags(b2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                    b2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(Branch_Detail_View.this, Grade_Detail_View.class);
//                            intent.putExtra("branch_id", branch_id);
//                            intent.putExtra("branch_name", branch_name);
//                            intent.putExtra("grade_id", String.valueOf(b2.getId()));
//                            intent.putExtra("grade_name", b2.getText().toString());
//                            intent.putExtra("user_id", user_id);
//                            intent.putExtra("school_id", school_id);
//                            intent.putExtra("email", user_email);
//                            intent.putExtra("min_role", min_role);
//                            Branch_Detail_View.this.finish();
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                        }
//                    });
//                    ll.addView(b2);
                }
                grade_list.setAdapter(new HomePageList(Branch_Detail_View.this,gradeNameList));
                grade_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Branch_Detail_View.this, Grade_Detail_View.class);
                            intent.putExtra("branch_id", branch_id);
                            intent.putExtra("branch_name", branch_name);
                            intent.putExtra("grade_id", gradeIdList.get(position));
                            intent.putExtra("grade_name", gradeNameList.get(position));
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("min_role", min_role);
                            intent.putExtra("branch_head", branch_head);

                            Branch_Detail_View.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
            } catch (JSONException e) {
 //               e.printStackTrace();
            }

        }
        else {
            Toast.makeText(Branch_Detail_View.this,"No Response from server",Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }
    void removeBranch() {
        progress.show();

        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_reference) + "home/remove_branch.php",

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Branch_Detail_View.this);
                                builder.setMessage(getResources().getString(R.string.branch_remove_success))
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
                params.put("branch_id", branch_id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    protected boolean isonline()
    {
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
                    Intent intent = new Intent(Branch_Detail_View.this, MainActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    Branch_Detail_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                    return true;
                case R.id.calender_event_edit_delete: {


                    if (!(gradeIdList.size()==0)){

                        Toast.makeText(Branch_Detail_View.this,"Please remove all grades from section and try again",Toast.LENGTH_LONG).show();
                    }else {


                        AlertDialog.Builder builder = new AlertDialog.Builder(Branch_Detail_View.this);

                        builder.setMessage(getResources().getString(R.string.confirm_remove_branch))
                                .setCancelable(false)
                                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeBranch();
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
//                    branch_id = getIntent().getExtras().getString("id");
//                    branch_name = getIntent().getExtras().getString("branch_name");
//                    school_id = getIntent().getExtras().getString("school_id");
//                    user_id = getIntent().getExtras().getString("user_id");
//                    user_email = getIntent().getExtras().getString("email");
//                    min_role = getIntent().getExtras().getInt("min_role");
//                    branch_head = getIntent().getExtras().getString("branch_head");




                    Intent intent1 = new Intent(thisActivity,Branch_Creation.class);
                    intent1.putExtra("school_id",school_id);
                    intent1.putExtra("user_id",user_id);
                    intent1.putExtra("email",user_email);
                    intent1.putExtra("min_role",min_role);

                    intent1.putExtra("branch_name",branch_name);
                    intent1.putExtra("branch_address",branch_address);
                    intent1.putExtra("branch_email",branch_email);
                    intent1.putExtra("branch_phone",branch_phone);
                    intent1.putExtra("branch_head",branch_head);
                    intent1.putExtra("branch_website",branch_website);
                    intent1.putExtra("branch_timing",branch_timing);
                    intent1.putExtra("branch_fb",branch_fb);
                    intent1.putExtra("branch_id",branch_id);
                    intent1.putExtra("branch_head_id",branch_head_id);
                    intent1.putExtra("school_name","");
                    intent1.putExtra("redirection","edit_branch_section");
                    startActivity(intent1);
                    finish();

                  //  Toast.makeText(thisActivity,"Please  try again",Toast.LENGTH_LONG).show();

                    return true;


                default:
                    return true;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Branch_Detail_View.this, MainActivity.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        Branch_Detail_View.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}

