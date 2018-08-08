package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Reset_password extends Activity {

    EditText et;
    private String TAG = Reset_password.class.getSimpleName();
    private String tag_string_req_recieve = "string_req_recieve";
    ProgressDialog progress;
    String school_id = "";
    TextView tv, tv2, tv3, tv4;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
          //  getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
         //   getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progress = new ProgressDialog(Reset_password.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        tv = (TextView) findViewById(R.id.resetpassword_textemail);
        tv2 = (TextView) findViewById(R.id.resetpassword_note);
        tv3 = (TextView) findViewById(R.id.resetpassword_success);
        tv4 = (TextView) findViewById(R.id.resetpassword_success_message);

        et = (EditText) findViewById(R.id.resetpassword_email);
        b = (Button) findViewById(R.id.resetpassword_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailet = et.getText().toString();
                if (emailet.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct_email));
                    Toast.makeText(Reset_password.this, R.string.correct_email, Toast.LENGTH_LONG).show();
                } else if(!emailet.trim().equals(emailet)) {
                    et.setError(getResources().getString(R.string.correct_username_without_space));
                    Toast.makeText(Reset_password.this, R.string.correct_username_without_space, Toast.LENGTH_LONG).show();
                } else {
                            try {
                                if (isonline()) {
                                        progress.show();
                                        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference)+"home/reset_password_backup.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        progress.hide();
                                                        updatedisplay(s);
                                                        Log.d("response",s);
                                                    }
                                                }
                                                ,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
                                                        progress.hide();
                                                        Toast.makeText(Reset_password.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                                                        VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
//                                                        Toast.makeText(Reset_password.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Reset_password.this, SplashScreen.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        Reset_password.this.finish();
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                                                    }
                                                }
                                        ) {

                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("username", emailet);
                                                return params;
                                            }

                                        };
                                        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve);
                                } else {
                                    Toast.makeText(Reset_password.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
//                                Log.d("Exception caught : ", "" + e);
                            }
                }
            }
        });
    }


    void updatedisplay(String s)
    {
        progress.show();
        try {
            JSONObject parentObject = new JSONObject(s);
            String success = parentObject.getString("sucess");
            switch (success) {
                case "Profile updated":
                    et.setVisibility(View.GONE);
                    b.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    break;
                case "Error App": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reset_password.this);
                    builder.setMessage(R.string.password_reset_error)
                            .setCancelable(false)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                case "Error": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reset_password.this);
                    builder.setMessage(R.string.profile_local_online)
                            .setCancelable(false)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                case "No email": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reset_password.this);
                    builder.setMessage("No email is defined for the user. You can not reset the password.")
                            .setCancelable(false)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
            }
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        progress.hide();
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

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(Reset_password.this, SplashScreen.class);
                Reset_password.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right,R.anim.right_left);
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Reset_password.this, SplashScreen.class);
        Reset_password.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right,R.anim.right_left);
    }
}