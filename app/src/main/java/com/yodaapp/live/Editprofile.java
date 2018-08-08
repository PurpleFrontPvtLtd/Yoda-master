package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent.MainActivity_Parent;
import com.yodaapp.live.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Editprofile extends Activity {

    protected String tag_string_req_send = "string_req_send";
    String name = "", email = "", phone = "", password = "";
    String id = "", school_id = "", profile_id = "", school_name = "";
    String username = "";
    EditText nameet, phoneet, emailet;
    Button b;
    ProgressDialog progress;
    ArrayList<String> myarray = new ArrayList<>();
    private String TAG = Editprofile.class.getSimpleName();

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static final boolean isValidPhoneNumber(CharSequence enumber) {
        return android.util.Patterns.PHONE.matcher(enumber).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile1);



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
        getActionBar().setTitle("Edit profile");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Editprofile.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        nameet = (EditText) findViewById(R.id.viewedit_name_edit);
        phoneet = (EditText) findViewById(R.id.viewedit_phone_edit);
        emailet = (EditText) findViewById(R.id.viewedit_website_edit);

        b = (Button) findViewById(R.id.viewedit_update);

        try {
            SQLiteDatabase db2 = openOrCreateDatabase("school", MODE_PRIVATE, null);
            Cursor c = db2.rawQuery("SELECT * FROM user_master", null);
            c.moveToFirst();
            id = c.getString(c.getColumnIndex("id"));
            name = c.getString(c.getColumnIndex("name"));
            email = c.getString(c.getColumnIndex("email_real"));
            username = c.getString(c.getColumnIndex("email"));
            password = c.getString(c.getColumnIndex("password"));
            phone = c.getString(c.getColumnIndex("phone"));
            school_id = c.getString(c.getColumnIndex("school_id"));
            profile_id = c.getString(c.getColumnIndex("user_type"));
            db2.close();
        } catch (Exception e) {
                       Log.d("Exception : ", "" + e);
            e.printStackTrace();
        }

        try {
            school_name = getIntent().getExtras().getString("school_name");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            phone = getIntent().getExtras().getString("phone");
        } catch (Exception e) {
            e.printStackTrace();
        }

        nameet.setText(name);
        phoneet.setText(phone);
//
//        try {
//
//            if (phone.equals("")) {
//                phoneet.setText(StaticVariable.contact);
//            }
//
//        }catch (Exception e){
//            phoneet.setText(StaticVariable.contact);
//
//            e.printStackTrace();
//
//        }

        if (!email.equals("null")) {
            emailet.setText(email);
        }
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
                builder.setTitle(R.string.dialog_password_title);
                builder.setMessage(R.string.dialog_password_message);
                LinearLayout ll = new LinearLayout(Editprofile.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                final EditText et = new EditText(Editprofile.this);
                et.setHint(R.string.dialog_password_hint);
                et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et.setTransformationMethod(new PasswordTransformationMethod());
                ll.addView(et);
                builder.setView(ll);
                builder.setPositiveButton(R.string.dialog_password_button, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String pass = et.getText().toString();
                        String word2 = "";
                        try {
                            MCrypt mcrypt = new MCrypt();
                            word2 = new String(mcrypt.decrypt(password));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (pass.equals(word2)) {
                            name = nameet.getText().toString();
                            phone = phoneet.getText().toString();
                            email = emailet.getText().toString();
                            validationfunction();
                        } else {

                            Toast.makeText(Editprofile.this, R.string.dialog_password_correct, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton(R.string.dialog_password_button2, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (profile_id.equals("1")) {
                            Intent intent = new Intent(Editprofile.this, MainActivity.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", id);
                            intent.putExtra("email", username);
                            intent.putExtra("school_name", school_name);
                            intent.putExtra("redirection", "View/Edit Profile");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Editprofile.this.finish();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        } else if (profile_id.equals("3")) {
                            Intent intent = new Intent(Editprofile.this, MainActivity_Parent.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", id);
                            intent.putExtra("email", username);
                            intent.putExtra("school_name", school_name);
                            intent.putExtra("redirection", "View/Edit Profile");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Editprofile.this.finish();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        } else {
                            Intent intent = new Intent(Editprofile.this, SplashScreen.class);
                            Editprofile.this.finish();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void updatedisplay() {
        progress.show();
        dbhelp entry = new dbhelp(Editprofile.this);
        entry.open();
        entry.updateeuser(id, name, phone, email);
        entry.close();
        progress.hide();
        if (profile_id.equals("1")) {
            Intent intent = new Intent(Editprofile.this, MainActivity.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", id);
            intent.putExtra("email", username);
            intent.putExtra("school_name", school_name);
            intent.putExtra("redirection", "View/Edit Profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        } else if (profile_id.equals("3")) {
            Intent intent = new Intent(Editprofile.this, MainActivity_Parent.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", id);
            intent.putExtra("email", username);
            intent.putExtra("school_name", school_name);
            intent.putExtra("redirection", "View/Edit Profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        } else {
            Intent intent = new Intent(Editprofile.this, SplashScreen.class);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
    }

    public void validationfunction() {
        if (name.equals("") || name.isEmpty() || name.trim().isEmpty()) {
            nameet.setError(getResources().getString(R.string.correct_name));
            Toast.makeText(Editprofile.this, R.string.correct_name, Toast.LENGTH_LONG).show();
        } else if (phone.equals("") || phone.isEmpty() || phone.trim().isEmpty()) {
            phoneet.setError(getResources().getString(R.string.correct_contact));
            Toast.makeText(Editprofile.this, R.string.correct_contact, Toast.LENGTH_LONG).show();
        } else if (phone.length() != 10) {
            phoneet.setError(getResources().getString(R.string.correct_limit_contact));
            Toast.makeText(Editprofile.this, R.string.correct_limit_contact, Toast.LENGTH_LONG).show();
        } else if (!isEmailValid(email)) {
            emailet.setError(getResources().getString(R.string.correct_email2));
            Toast.makeText(Editprofile.this, R.string.correct_email2, Toast.LENGTH_LONG).show();
        } else {
            if (isValidPhoneNumber(phone)) {
                if (isonline()) {
                    progress.show();
                    StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/edit_profile.php",

                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String arg0) {
                                    Log.d("response", arg0);
                                    //                                   Log.d("here in sucess","sucess");
                                    JSONObject parentObject;
                                    try {
                                        parentObject = new JSONObject(arg0);
                                        //						String id = parentObject.getString("id");
                                        String sucess = parentObject.getString("sucess");
                                        if (sucess.equals("Profile updated")) {
                                            progress.hide();
                                            updatedisplay();
                                        } else {
                                            progress.hide();
                                            Toast.makeText(Editprofile.this, R.string.unknownerror5, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {

                                        //                                      e.printStackTrace();
                                    } catch (Exception e) {
                                        //                                      e.printStackTrace();
                                    }


                                }
                            },


                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError arg0) {
                                    progress.hide();
                                    //                                   VolleyLog.d(TAG, "Error: " + arg0.getMessage());
                                    Toast.makeText(Editprofile.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                                    //                                   Toast.makeText(Editprofile.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                                    if (profile_id.equals("1")) {
                                        Intent intent = new Intent(Editprofile.this, MainActivity.class);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("user_id", id);
                                        intent.putExtra("email", username);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("redirection", "View/Edit Profile");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Editprofile.this.finish();
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    } else if (profile_id.equals("3")) {
                                        Intent intent = new Intent(Editprofile.this, MainActivity_Parent.class);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("user_id", id);
                                        intent.putExtra("email", username);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("redirection", "View/Edit Profile");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Editprofile.this.finish();
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    } else {
                                        Intent intent = new Intent(Editprofile.this, SplashScreen.class);
                                        Editprofile.this.finish();
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", id);
                            params.put("school_id", school_id);
                            params.put("username", username);
                            params.put("name", name);
                            params.put("email", email);
                            params.put("phone", phone);

//                            Log.d("id", id);
//                            Log.d("school_id", school_id);
//                            Log.d("username", username);
//                            Log.d("name", name);
//                            Log.d("email", email);
//                            Log.d("phone", phone);
                            return params;
                        }

                        ;
                    };


                    AppController.getInstance().addToRequestQueue(request, tag_string_req_send);
                } else {
                    Toast.makeText(Editprofile.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Editprofile.this, R.string.correct_mobile, Toast.LENGTH_LONG).show();
            }
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (profile_id.equals("1")) {
                    Intent intent = new Intent(Editprofile.this, MainActivity.class);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("user_id", id);
                    intent.putExtra("email", username);
                    intent.putExtra("school_name", school_name);
                    intent.putExtra("redirection", "View/Edit Profile");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Editprofile.this.finish();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                } else if (profile_id.equals("3")) {
                    Intent intent = new Intent(Editprofile.this, MainActivity_Parent.class);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("user_id", id);
                    intent.putExtra("email", username);
                    intent.putExtra("school_name", school_name);
                    intent.putExtra("redirection", "View/Edit Profile");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Editprofile.this.finish();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                } else {
                    Intent intent = new Intent(Editprofile.this, SplashScreen.class);
                    Editprofile.this.finish();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                }
                return true;
            }
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (profile_id.equals("1")) {
            Intent intent = new Intent(Editprofile.this, MainActivity.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", id);
            intent.putExtra("email", username);
            intent.putExtra("school_name", school_name);
            intent.putExtra("redirection", "View/Edit Profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        } else if (profile_id.equals("3")) {
            Intent intent = new Intent(Editprofile.this, MainActivity_Parent.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", id);
            intent.putExtra("email", username);
            intent.putExtra("school_name", school_name);
            intent.putExtra("redirection", "View/Edit Profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        } else {
            Intent intent = new Intent(Editprofile.this, SplashScreen.class);
            Editprofile.this.finish();
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
    }
}