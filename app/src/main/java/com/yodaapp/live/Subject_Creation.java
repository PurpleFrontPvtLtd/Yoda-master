package com.yodaapp.live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Subject_Creation extends Activity {

    private String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    List<Createadmin> feedslist;
    String school_id = "", user_id = "", user_email = "";
    String subject;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_creation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        progress = new ProgressDialog(Subject_Creation.this);
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
        } catch (Exception e) {
//            e.printStackTrace();
        }

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


        Button b = (Button) findViewById(R.id.subject_creation_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Subject_Creation.this);
                et = (EditText) findViewById(R.id.subject_name);
                subject = et.getText().toString();
                if(!subject.equals("")) {
                    progress.show();
                    displaydata(getResources().getString(R.string.url_reference) + "home/subject_creation.php",subject);
                }

                else {
            Toast.makeText(Subject_Creation.this, "Please enter subject name", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    void displaydata(String uri, final String subject) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(Subject_Creation.this, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    Subject_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
//                                else if(subject.equals("")) {
//                                    et.setError(getResources().getString(R.string.subject_name_need));
//                                    Toast.makeText(Subject_Creation.this, getResources().getString(R.string.subject_name_need), Toast.LENGTH_LONG).show();
//                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.subject_name_exists));
                                    Toast.makeText(Subject_Creation.this, getResources().getString(R.string.subject_name_exists), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Subject_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Subject_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Subject_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Subject_Creation.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Subject_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("school_id", school_id);
                params.put("subject_name", subject);
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Subject_Creation.this, MainActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Subject_Creation.this.finish();
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
        Intent intent = new Intent(Subject_Creation.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Subject_Creation.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit."))
        {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity)
    {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
