package com.yodaapp.live.Parent;

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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.yodaapp.live.Parent_Model.Communication_Parents_Staff_View_Model;
import com.yodaapp.live.Parent_Parsers.Communication_Parents_Staff_View_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.adapter.ParentsCommunicationAdapter;
import com.yodaapp.live.adapter.ParentsIndividualCommunicationAdapter;
import com.yodaapp.live.controller.AppController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communication_Parents_Staff_View extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", school_name = "";
    String user_id = "";
    String user_email = "";
    String student_id = "";
    String student_name = "";
    List<Communication_Parents_Staff_View_Model> feedslist;
    ListView listview1;
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_parents_staff_view);
        listview1=(ListView)findViewById(R.id.listview);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }
        progress = new ProgressDialog(Communication_Parents_Staff_View.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            student_id = getIntent().getExtras().getString("id");
            student_name = getIntent().getExtras().getString("name");
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communication_Parents_Staff_View.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "parent_home/individual_messages_parent_home_backup.php");
            } else {
                Toast.makeText(Communication_Parents_Staff_View.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
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
                        feedslist = Communication_Parents_Staff_View_JSONParser.parserFeed(arg0);
                        listview1.setAdapter(new ParentsIndividualCommunicationAdapter(thisActivity,feedslist));

                        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Communication_Parents_Staff_View.this, Communication_Parents_Individual_Send1.class);
                                intent.putExtra("id", student_id);
                                intent.putExtra("name", student_name);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("teacher_id", feedslist.get(position).getId());
                                intent.putExtra("teacher_name", feedslist.get(position).getName());
                                Communication_Parents_Staff_View.this.finish();
                                startActivity(intent);
                                Communication_Parents_Staff_View.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });
                        //    updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_Parents_Staff_View.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Toast.makeText(Communication_Parents_Staff_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Parents_Staff_View.this);
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
                                        Intent intent = new Intent(Communication_Parents_Staff_View.this, SplashScreen.class);
                                        Communication_Parents_Staff_View.this.finish();
                                        startActivity(intent);
                                        Communication_Parents_Staff_View.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("student_id", student_id);
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
     /*       LinearLayout ll_main = (LinearLayout) findViewById(R.id.communication_parents_staff_layout);
            LinearLayout ll_main2 = (LinearLayout) findViewById(R.id.communication_parents_staff_layout_read);
    */        for (final Communication_Parents_Staff_View_Model flower : feedslist) {

                TextView tv = new TextView(Communication_Parents_Staff_View.this);
                tv.setId(Integer.valueOf(flower.getId()));
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv.setTextAppearance(Communication_Parents_Staff_View.this, R.style.SimpleTextviewStyle);
                if (flower.getStatus().equals("0")) {
                    tv.setText(flower.getName());
                } else if (!flower.getStatus().equals("")) {
                    tv.setText(flower.getName() + "(" + flower.getStatus() + ")");
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tv.setText(flower.getName());
                }
                tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Communication_Parents_Staff_View.this, Communication_Parents_Individual_Send1.class);
                        intent.putExtra("id", student_id);
                        intent.putExtra("name", student_name);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("teacher_id", flower.getId());
                        intent.putExtra("teacher_name", flower.getName());
                        Communication_Parents_Staff_View.this.finish();
                        startActivity(intent);
                        Communication_Parents_Staff_View.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
               /* if (flower.getStatus().equals("0")) {
                    ll_main2.addView(tv);
                } else if (!flower.getStatus().equals("")) {
                    ll_main.addView(tv);
                } else {
                    ll_main2.addView(tv);
                }*/
            }
        } else {
            Toast.makeText(Communication_Parents_Staff_View.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
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
        inflater.inflate(R.menu.communication_parents_staff_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.parent_individual_send_message: {
                Intent intent = new Intent(Communication_Parents_Staff_View.this, Communication_Parents_Message_Send.class);
                intent.putExtra("id", student_id);
                intent.putExtra("name", student_name);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                Communication_Parents_Staff_View.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;
            }

            case android.R.id.home: {
                Intent intent = new Intent(Communication_Parents_Staff_View.this, MainActivity_Parent.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("redirection", "Communications");
                Communication_Parents_Staff_View.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            }
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Communication_Parents_Staff_View.this, MainActivity_Parent.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("school_name", school_name);
        intent.putExtra("redirection", "Communications");
        Communication_Parents_Staff_View.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
