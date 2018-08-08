package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent_Model.Communication_Parents_Individual_Send_Model;
import com.yodaapp.live.Parent_Parsers.Communication_Parents_Individual_Send_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.adapter.MessageListAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Communication_Parents_Individual_Send1 extends Activity {

    Activity thisActivity=this;
    ProgressDialog progress;
    List<Communication_Parents_Individual_Send_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "";
    String student_id = "",student_name = "",teacher_id = "", teacher_name = "";
    TextView tv;
    List<Createadmin> feedslist22;
    String school_name;
    ScrollView ss;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_parents_individual_send1);
        recyclerView = (RecyclerView) findViewById(R.id.mesaagelistrecycelr);
        layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }
        progress = new ProgressDialog(Communication_Parents_Individual_Send1.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        ss = (ScrollView) findViewById(R.id.communcations_parents_individual_layout);
        ss.post(new Runnable() {
            @Override
            public void run() {
                ss.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        try {
            student_id = getIntent().getExtras().getString("id");
            student_name = getIntent().getExtras().getString("name");
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            teacher_id = getIntent().getExtras().getString("teacher_id");
            teacher_name = getIntent().getExtras().getString("teacher_name");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.communcations_parents_individual_name);
        tv.setText(teacher_name);

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communication_Parents_Individual_Send1.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "parent_home/individual_messages_parent_teacher.php");
            } else {
                Toast.makeText(Communication_Parents_Individual_Send1.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        Button b = (Button) findViewById(R.id.communcations_parents_individual_send_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    EditText et = (EditText) findViewById(R.id.communcations_parents_individual_send_message);
                    String message = et.getText().toString();
                    if (!message.trim().equals("")) {
                        progress.show();
                        hideSoftKeyboard(Communication_Parents_Individual_Send1.this);
                        senddata(getResources().getString(R.string.url_reference) + "parent_home/individual_message_insert.php", message);
                    } else {
                        et.setError(getResources().getString(R.string.insert_message_sending));
                        Toast.makeText(Communication_Parents_Individual_Send1.this, getResources().getString(R.string.insert_message_sending), Toast.LENGTH_LONG).show();
                    }

            }
        });


    }


    private void senddata(final String s, final String message) {
        StringRequest request = new StringRequest(Request.Method.POST, s,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
                        feedslist22 = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist22 != null) {
                            for (final Createadmin flower : feedslist22) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    EditText et = (EditText) findViewById(R.id.communcations_parents_individual_send_message);
                                    et.setText("");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Parents_Individual_Send1.this);
                                    builder.setMessage(getResources().getString(R.string.message_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    progress.show();
                                                    updatedata(getResources().getString(R.string.url_reference) + "parent_home/individual_messages_parent_teacher.php");
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(Communication_Parents_Individual_Send.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_Parents_Individual_Send1.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Toast.makeText(Communication_Parents_Individual_Send1.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Parents_Individual_Send1.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        senddata(s, message);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Communication_Parents_Individual_Send1.this, SplashScreen.class);
                                        Communication_Parents_Individual_Send1.this.finish();
                                        startActivity(intent);
                                        Communication_Parents_Individual_Send1.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("email", user_email);
                params.put("school_id", school_id);
                params.put("message",message);
                params.put("student_id",student_id);
                params.put("teacher_id",teacher_id);
//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            LinearLayout ll_main = (LinearLayout) findViewById(R.id.communcations_parents_individual_layout_linear);
            ll_main.removeAllViews();
            for (final Communication_Parents_Individual_Send_Model flower : feedslist) {
                String message_message = flower.getMessage();
                String message_send_by = flower.getName();
                String message_created = flower.getCreated();

                SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date date = srcDf.parse(message_created);
                    SimpleDateFormat destDf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
                    message_created = destDf.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LinearLayout ll = new LinearLayout(Communication_Parents_Individual_Send1.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setBackgroundColor(Color.parseColor("#eaeaea"));
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                p3.gravity = Gravity.RIGHT;

                TextView tv = new TextView(Communication_Parents_Individual_Send1.this);
                tv.setText(message_send_by);
                tv.setTextAppearance(Communication_Parents_Individual_Send1.this, R.style.TextviewStyle);
                tv.setPadding(7,0,7,0);
                ll.addView(tv);

                TextView tv2 = new TextView(Communication_Parents_Individual_Send1.this);
                tv2.setText(message_message);
                tv2.setTextAppearance(Communication_Parents_Individual_Send1.this, R.style.SimpleTextviewStyle);
                tv2.setPadding(7, 0, 7, 0);
                ll.addView(tv2);

                TextView tv3 = new TextView(Communication_Parents_Individual_Send1.this);
                tv3.setText(message_created);
                tv3.setLayoutParams(p3);
                tv3.setTextAppearance(Communication_Parents_Individual_Send1.this, R.style.SimpleTextviewStyle_12);
                tv3.setPadding(7, 0, 7, 0);
                ll.addView(tv3);

                ImageView divider2 = new ImageView(Communication_Parents_Individual_Send1.this);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                lp2.setMargins(0, 0, 0, 0);
                divider2.setLayoutParams(lp2);
                divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ll.addView(divider2);
                ll_main.addView(ll);
                ss.scrollTo(0, ss.getBottom());
                ss.post(new Runnable() {
                    @Override
                    public void run() {
                        ss.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
        else {
            Toast.makeText(Communication_Parents_Individual_Send1.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    void updatedata(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_Parents_Individual_Send_JSONParser.parserFeed(arg0);

                        recyclerView.setAdapter(new MessageListAdapter(feedslist,thisActivity));
                       // updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_Parents_Individual_Send1.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Parents_Individual_Send.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Parents_Individual_Send1.this);
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
                                        Intent intent = new Intent(Communication_Parents_Individual_Send1.this, SplashScreen.class);
                                        Communication_Parents_Individual_Send1.this.finish();
                                        startActivity(intent);
                                        Communication_Parents_Individual_Send1.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("email", user_email);
                params.put("school_id", school_id);
                params.put("student_id",student_id);
                params.put("teacher_id",teacher_id);
//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
//                Log.d("student_id", student_id);
//                Log.d("teacher_id", teacher_id);
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

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Communication_Parents_Individual_Send1.this, MainActivity_Parent.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("redirection", "Communications");
                Communication_Parents_Individual_Send1.this.finish();
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
        Intent intent = new Intent(Communication_Parents_Individual_Send1.this, Communication_Parents_Staff_View.class);
        intent.putExtra("id",student_id);
        intent.putExtra("name",student_name);
        intent.putExtra("user_id",user_id);
        intent.putExtra("school_id",school_id);
        intent.putExtra("email",user_email);
        intent.putExtra("school_name",school_name);
        Communication_Parents_Individual_Send1.this.finish();
        startActivity(intent);
        Communication_Parents_Individual_Send1.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
