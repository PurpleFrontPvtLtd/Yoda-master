package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.yodaapp.live.Parent.MainActivity_Parent;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.ExistingUser_Model;
import com.yodaapp.live.parsers.ExistingUser_JSONParser;
import com.yodaapp.live.pushnotification.WakeLocker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Existinguser extends Activity {


    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Receiving push messages
     */

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            /*lblMessage.append(newMessage + "\n");*/
            Toast.makeText(getApplicationContext(), R.string.message + " " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };
    EditText et, et2;
    String username = "", password = "", email,phone="";
    ProgressDialog progress;
    List<ExistingUser_Model> feedslist;
    CheckBox ch;
    AsyncTask<Void, Void, Void> mRegisterTask;
    String name = "";
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "93162003572";
    /**
     * Tag used on log messages.
     */
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    String user_id = "", user_email = "", school_id = "";
    private String tag_string_req_recieve2 = "string_req_recieve2";
    private String TAG = Existinguser.class.getSimpleName();

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.existinguser);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
           // getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            //getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        //    getRegisterationID();
        }

        progress = new ProgressDialog(Existinguser.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) findViewById(R.id.existinguser_username);
        et2 = (EditText) findViewById(R.id.existinguser_password);

        Button b = (Button) findViewById(R.id.existinguser_login);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                username = et.getText().toString();
                email = username;
                password = et2.getText().toString();
                validationfunction();


            }
        });
        ch = (CheckBox) findViewById(R.id.existinguser_checkbox);
        ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("lee", "checkbok");
                if (isChecked) {
                    et2.setTransformationMethod(null);
                    ch.setText(R.string.hide_password);
                } else {
                    et2.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText(R.string.show_password);
                }
            }
        });

        Button b3 = (Button) findViewById(R.id.existinguser_forgetpassword);
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Existinguser.this, Reset_password.class);
                Existinguser.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }

    public void validationfunction() {
        boolean invalid = false;
        if (username.equals("") || username.isEmpty() || username.trim().isEmpty()) {
            invalid = true;
            et.setError(getResources().getString(R.string.correct_user_name));
            Toast.makeText(Existinguser.this, R.string.correct_user_name, Toast.LENGTH_LONG).show();
        } else if (username.length() < 6) {
            invalid = true;
            Toast.makeText(Existinguser.this, R.string.username_minimum_limit, Toast.LENGTH_LONG).show();
        } else if (password.equals("") || password.isEmpty() || password.trim().isEmpty()) {
            invalid = true;
            et2.setError(getResources().getString(R.string.correct_password));
            Toast.makeText(Existinguser.this, R.string.correct_password, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            invalid = true;
            et2.setError(getResources().getString(R.string.password_limit_correct));
            Toast.makeText(Existinguser.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
        } else if (password.trim().length() < 6) {
            invalid = true;
            et2.setError(getResources().getString(R.string.password_limit_correct));
            Toast.makeText(Existinguser.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
        } else {
            if (invalid == false) {
                if (password.trim().equals(password)) {
                    if (username.trim().equals(username)) {
                        if (isonline()) {
                            progress.show();
                            final String alertchoose = "2";
                            try {
                                MCrypt mcrypt = new MCrypt();
                                password = MCrypt.bytesToHex(mcrypt.encrypt(password));
                                Log.d("password",password);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            StringRequest request = new StringRequest(Method.POST, getResources().getString(R.string.url_reference) + "home/passwordcheck.php",

                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String arg0) {


                                            try {


                                                Log.d(TAG, arg0);
                                                Log.d("here in sucess", "sucess");
                                                feedslist = ExistingUser_JSONParser.parserFeed(arg0);
                                                progress.hide();
                                                updatedisplay();
                                            }catch (NoClassDefFoundError e){
                                                e.printStackTrace();
                                            }
                                        }
                                    },


                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError arg0) {
                                            progress.hide();
                                            Log.d(TAG, "Error: " + arg0.getMessage());
                                            Toast.makeText(Existinguser.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                                            Toast.makeText(Existinguser.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Existinguser.this, SplashScreen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            Existinguser.this.finish();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        }
                                    }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("email", username);
                                    params.put("password", password);
                                    params.put("passwordset", password);
                                    params.put("alertchoose", alertchoose);
                                    return params;
                                }

                                ;
                            };
                            AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
                        } else {
                            Toast.makeText(Existinguser.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(Existinguser.this, R.string.correct_username_without_space, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Existinguser.this, R.string.password_space, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Existinguser.this, R.string.password_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    public void updatedisplay() {
        progress.hide();
        //	Toast.makeText(Existinguser.this,"update display",Toast.LENGTH_LONG).show();
        if (feedslist != null) {
            for (final ExistingUser_Model flower : feedslist) {
                String success = flower.getMessage();
                user_email = email;
                user_id = flower.getId();
                school_id = flower.getSchool_id();
              //  phone= flower.get();
                switch (success) {
                    case "Existing profile":

                        if (flower.getSchool_status().contains("1")) {

                            Toast.makeText(Existinguser.this,R.string.school_inactivate,Toast.LENGTH_LONG).show();

                        }

                       else if (flower.getStatus().contains("0")) {

                            try {
                                if (checkPlayServices()) {
                                    gcm = GoogleCloudMessaging.getInstance(Existinguser.this);
                                    regid = getRegistrationId(Existinguser.this);
                                    StaticVariable.regid=regid;
                             //    Log.v("reg_id",regid);
                            //     Toast.makeText(Existinguser.this,regid,Toast.LENGTH_SHORT).show();
                                    if (regid.isEmpty()) {
                                        registerInBackground();
                                    } else {
                                        sendRegistrationIdToBackend();
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            dbhelp entry = new dbhelp(Existinguser.this);
                            entry.open();
                            entry.createuser(flower.getId(), flower.getSchool_id(), email, password, flower.getProfile_id(), flower.getSchool_name(), flower.getName(), flower.getEmail_real(),flower.getContact());
                            entry.close();
                            if (flower.getProfile_id().equals("1")) {
                                StaticVariable.school_id=flower.getSchool_id();
                                StaticVariable.user_id=flower.getId();
                                StaticVariable.email=email;
                                StaticVariable.role_id=flower.getProfile_id();
                                Intent intent = new Intent(Existinguser.this, MainActivity.class);
                                intent.putExtra("user_id", flower.getId());
                                intent.putExtra("school_id", flower.getSchool_id());
                                intent.putExtra("email", email);
                                intent.putExtra("roles", flower.getProfile_id());
                                intent.putExtra("school_name", flower.getSchool_name());
                                Existinguser.this.finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            } else if (flower.getProfile_id().equals("3")) {
                                Intent intent = new Intent(Existinguser.this, MainActivity_Parent.class);
                                StaticVariable.school_id=flower.getSchool_id();
                                StaticVariable.user_id=flower.getId();
                                StaticVariable.email=email;
                                StaticVariable.role_id=flower.getProfile_id();

                                intent.putExtra("user_id", flower.getId());
                                intent.putExtra("school_id", flower.getSchool_id());
                                intent.putExtra("email", email);
                                intent.putExtra("school_name", flower.getSchool_name());
                                Existinguser.this.finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        }
                        else {

                            Toast.makeText(Existinguser.this,R.string.account_inactivate,Toast.LENGTH_LONG).show();
                        }


                        break;
                    case "inactive account":
                        Toast.makeText(Existinguser.this,R.string.account_to_activate,Toast.LENGTH_LONG).show();
                        break;
                    case "Wrong Password":
                        AlertDialog.Builder builder = new AlertDialog.Builder(Existinguser.this);
                        builder.setTitle(R.string.dialog_password_wrong_title)
                                .setMessage(R.string.dialog_password_wrong_message)
                                .setPositiveButton(R.string.dialog_password_wrong_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Existinguser.this, Reset_password.class);
                                        Existinguser.this.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                })
                                .setNegativeButton(R.string.dialog_password_wrong_button2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(Existinguser.this, R.string.email_password, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(Existinguser.this, flower.getId() + "school" + flower.getSchool_id() + "success" + flower
                                .getMessage(), Toast.LENGTH_LONG).show();

                        break;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Existinguser.this);
            builder.setTitle(R.string.dialog_password_wrong_title)
                    .setMessage(R.string.dialog_password_wrong_message)
                    .setPositiveButton(R.string.dialog_password_wrong_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Existinguser.this, Reset_password.class);
                            Existinguser.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    })
                    .setNegativeButton(R.string.dialog_password_wrong_button2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
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

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Existinguser.this, SplashScreen.class);
                Existinguser.this.finish();
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
        Intent intent = new Intent(Existinguser.this, PreRegistration.class);
        Existinguser.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //            Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(Existinguser.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    storeRegistrationId(Existinguser.this, regid);
                    Log.v("regid",regid+"a");
             //       Toast.makeText(Existinguser.this,regid,Toast.LENGTH_SHORT).show();
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //Toast.makeText(Existinguser.this,msg,Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(Existinguser.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        if (isonline()) {
            progress.show();
            StringRequest request = new StringRequest(Method.POST, getResources().getString(R.string.url_reference) + "pushnotification/register2.php",

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String arg0) {
                            progress.hide();
                            Log.d("response", arg0);
                        }
                    },


                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            Log.d("error", arg0.getMessage());
                            progress.hide();
                            Toast.makeText(Existinguser.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("school_id", school_id);
                    params.put("email", user_email);
                    params.put("regId", regid);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
        }

    }

    String regId = "", msg = "";

    public void getRegisterationID() {

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object...params) {
                // TODO Auto-generated method stub
                String msg = "";
                try {
                    gcm = GoogleCloudMessaging.getInstance(Existinguser.this);

                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(Existinguser.this);
                    }
                    regId = gcm.register(SENDER_ID);
                    Log.d("in async task", regId);
                    Toast.makeText(Existinguser.this,regId,Toast.LENGTH_SHORT).show();

                    // try
                    msg = "Device registered, registration ID=" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);

    }
}