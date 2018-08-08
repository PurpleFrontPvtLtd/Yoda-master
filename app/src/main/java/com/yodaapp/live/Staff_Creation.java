package com.yodaapp.live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


public class
Staff_Creation extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    List<Createadmin> feedslist;
    String staff_name = "", staff_email = "",staff_phone = "",staff_password = "",staff_confirm_password = "", staff_userid = "";
    String school_id = "", user_id = "", user_email = "";
    CheckBox ch;
    EditText et,et2,et3,et4,et5,et6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_creation1);

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

        progress = new ProgressDialog(Staff_Creation.this);
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
            e.printStackTrace();
        }


        et = (EditText) findViewById(R.id.staff_name);
        et2 = (EditText) findViewById(R.id.staff_email);
        et3 = (EditText) findViewById(R.id.staff_phone);
        et4 = (EditText) findViewById(R.id.staff_creation_password);
        et5 = (EditText) findViewById(R.id.staff_creation_confirm_password);
        et6 = (EditText) findViewById(R.id.staff_userid);

        ch = (CheckBox) findViewById(R.id.staff_creation_change_password);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText et5 = (EditText) findViewById(R.id.staff_creation_password);
                EditText et6 = (EditText) findViewById(R.id.staff_creation_confirm_password);
                if (isChecked) {
                    et5.setTransformationMethod(null);
                    et6.setTransformationMethod(null);
                    ch.setText(getResources().getString(R.string.hide_password));
                } else {
                    et5.setTransformationMethod(new PasswordTransformationMethod());
                    et6.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText(getResources().getString(R.string.show_password));
                }
            }
        });

        Button b = (Button) findViewById(R.id.staff_creation_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Staff_Creation.this);


                staff_name = et.getText().toString();
                staff_email = et2.getText().toString();
                staff_phone = et3.getText().toString();
                staff_password = et4.getText().toString();
                staff_confirm_password = et5.getText().toString();
                staff_userid = et6.getText().toString();

                if(staff_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct__name));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.correct__name),Toast.LENGTH_LONG).show();
                }
                else if(staff_email.trim().equals("")) {
                    et2.setError(getResources().getString(R.string.correct_email));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.correct_email), Toast.LENGTH_LONG).show();
                }
                else if(!isEmailValid(staff_email)) {
                    et2.setError(getResources().getString(R.string.correct_email2));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.correct_email2), Toast.LENGTH_LONG).show();
                }
                else if(staff_userid.trim().equals("")) {
                    et6.setError(getResources().getString(R.string.staff_username));
                    Toast.makeText(Staff_Creation.this, getResources().getString(R.string.staff_username), Toast.LENGTH_LONG).show();
                }
                else if(!staff_userid.trim().equals(staff_userid)) {
                    et6.setError(getResources().getString(R.string.staff_username_space));
                    Toast.makeText(Staff_Creation.this, getResources().getString(R.string.staff_username_space), Toast.LENGTH_LONG).show();
                }
                else if(staff_userid.length() < 6) {
                    et6.setError(getResources().getString(R.string.staff_username_size));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.staff_username_size),Toast.LENGTH_LONG).show();
                }
                else if(staff_password.trim().equals("")) {
                    et4.setError(getResources().getString(R.string.correct_password));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.correct_password),Toast.LENGTH_LONG).show();
                }
                else if(staff_confirm_password.trim().equals("")) {
                    et5.setError(getResources().getString(R.string.correct_confirm_password));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.correct_confirm_password),Toast.LENGTH_LONG).show();
                }
                else if(!staff_password.trim().equals(staff_password)) {
                    et4.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.password_space),Toast.LENGTH_LONG).show();
                }
                else if(staff_password.length() < 6 ) {
                    et4.setError(getResources().getString(R.string.password_limit_correct));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.password_limit_correct),Toast.LENGTH_LONG).show();
                }
                else if(!staff_confirm_password.trim().equals(staff_confirm_password)) {
                    et5.setError(getResources().getString(R.string.password_confirm_space));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.password_confirm_space),Toast.LENGTH_LONG).show();
                }
                else if(staff_confirm_password.length() < 6) {
                    et5.setError(getResources().getString(R.string.confirm_password_limit_correct));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.confirm_password_limit_correct),Toast.LENGTH_LONG).show();
                }
                else if(!staff_confirm_password.equals(staff_password)) {
                    et5.setError(getResources().getString(R.string.password_confirm_password_not_match));
                    Toast.makeText(Staff_Creation.this,getResources().getString(R.string.password_confirm_password_not_match),Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        MCrypt mcrypt = new MCrypt();
                        staff_password = MCrypt.bytesToHex( mcrypt.encrypt(staff_password) );
                        staff_confirm_password = MCrypt.bytesToHex( mcrypt.encrypt(staff_confirm_password) );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isonline()) {
                        progress.show();
                        displaydata(getResources().getString(R.string.url_reference) + "home/staff_creation.php");
                    } else {
                        Toast.makeText(Staff_Creation.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    void displaydata(String uri) {
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
//                                    Intent intent = new Intent(Staff_Creation.this, Role_Assign.class);
//                                    intent.putExtra("user_id", user_id);
//                                    intent.putExtra("school_id", school_id);
//                                    intent.putExtra("email", user_email);
//                                    intent.putExtra("staff_id", flower.getId());
//                                    Staff_Creation.this.finish();
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    Intent intent = new Intent(Staff_Creation.this, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    Staff_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);




                                } else if(flower.getSucess().equals("existing userid")) {
                                    Toast.makeText(Staff_Creation.this, getResources().getString(R.string.userid_existing_message), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Staff_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
                                }
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
                        Toast.makeText(Staff_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Staff_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Staff_Creation.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Staff_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("staff_name", staff_name);
                params.put("staff_email", staff_email);
                params.put("staff_phone", staff_phone);
                params.put("staff_password", staff_password);
                params.put("staff_userid",staff_userid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
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
                Intent intent = new Intent(Staff_Creation.this, MainActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                Staff_Creation.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Staff_Creation.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Staff_Creation.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);

    }

    public void clickSname(View view)
    {
        et.requestFocus();

    }
    public void clickSphone(View view)
    {
        et3.requestFocus();
    }
    public void clickSemail(View view)
    {
        et2.requestFocus();
    }
    public void clickUname(View view)
    {
        et6.requestFocus();
    }
    public void clickSupass(View view)
    {
        et4.requestFocus();
    }
    public void clickScpass(View view)
    {
        et5.requestFocus();
    }
}
