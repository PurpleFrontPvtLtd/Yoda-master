package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent_Parsers.Communicatioin_Staff_Send_School_JSONParser_backup;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Communicatioin_Staff_Send_School_Model;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Communication_toStaff_View_Messages extends Activity {

    ProgressDialog progress;
    List<Communicatioin_Staff_Send_School_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "", role_id = "";
    String branch_id = "";
    String branch_name = "";
    TextView tv;
    List<Createadmin> feedslist22;
    String school_name;
    ScrollView ss;
    Activity thisActivity=this;
    ProgressDialog mProgressDialog;
    String filename;
    String attach_file = "";
    String ddefault = "";
    int screen = 0;

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communications_staff_royal_send);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        ss = (ScrollView) findViewById(R.id.communication_staff_royal_layout);
        ss.post(new Runnable() {
            @Override
            public void run() {
                ss.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        try {
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
            role_id = getIntent().getExtras().getString("role_id");
            ddefault = getIntent().getExtras().getString("ddefault");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TextView tv2 = (TextView) findViewById(R.id.school_communication_messages);
            tv2.setText(school_name + " " + getResources().getString(R.string.communications));
        } catch(Exception e) {
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.communcations_staff_royal_school_name);
        tv.setText(school_name);

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(thisActivity, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/communication_recieve_school.php");
            } else {
                Toast.makeText(thisActivity, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
        Log.d("role_id",role_id);
        Button b = (Button) findViewById(R.id.communication_staff_send_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont = Integer.valueOf(role_id);
                if (cont < 4) {
                    EditText et = (EditText) findViewById(R.id.communication_staff_send_message);
                    String message = et.getText().toString();
                    if (!message.trim().equals("")) {
                        progress.show();
                        hideSoftKeyboard(thisActivity);
                        senddata(getResources().getString(R.string.url_reference) + "home/communication_send_school.php", message);
                    } else {
                        Toast.makeText(thisActivity, getResources().getString(R.string.insert_message_sending), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(thisActivity, getResources().getString(R.string.permission_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            screen = 1;
        }
        else if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            screen = 1;
        }
    }

    private void senddata(final String s, final String message) {
        StringRequest request = new StringRequest(Request.Method.POST, s,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("response", response);
                        feedslist22 = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist22) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    EditText et = (EditText) findViewById(R.id.communication_staff_send_message);
                                    et.setText("");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                    builder.setMessage(getResources().getString(R.string.message_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    progress.show();
                                                    updatedata(getResources().getString(R.string.url_reference) + "home/communication_recieve_school.php");
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    Toast.makeText(thisActivity, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Send_School.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
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
                                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                                        thisActivity.finish();
                                        startActivity(intent);
                                        thisActivity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("message", message);
                if (!attach_file.equals("")) {
                    params.put("attach_file", attach_file);
                }
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
            LinearLayout ll_main = (LinearLayout) findViewById(R.id.communication_staff_royal_layout_linear);
            ll_main.removeAllViews();
            for (final Communicatioin_Staff_Send_School_Model flower : feedslist) {
                String message_id = flower.getId();
                String message_message = flower.getMessages();
                String message_send_by = flower.getSend_by();
                String messages_send_to = flower.getSend_to();
                String messages_school_id = flower.getSchool_id();
                String message_branch_id = flower.getBranch_id();
                String message_grade_id = flower.getGrade_id();
                String message_created = flower.getCreated();
                String message_send_by_id = flower.getSend_by_id();

                SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date date = srcDf.parse(message_created);
                    SimpleDateFormat destDf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
                    message_created = destDf.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("user_id",user_id);
                Log.d("mesage_send_by",message_send_by);
                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout ll = new LinearLayout(thisActivity);
                ll.setOrientation(LinearLayout.VERTICAL);

                if(user_id.equals(message_send_by_id)) {
                    if(screen == 1) {
                        p4.setMargins(200, 0, 0, 0);
                    } else {
                        p4.setMargins(100, 0, 0, 0);
                    }
                    ll.setBackgroundColor(Color.parseColor("#00b0da"));
                    ll.setClickable(true);

                } else {
                    ll.setBackgroundColor(Color.parseColor("#eaeaea"));
                    if(screen == 1) {
                        p4.setMargins(0, 0, 200, 0);
                    } else {
                        p4.setMargins(0,0,100,0);
                    }
                }
                ll.setLayoutParams(p4);

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                p3.gravity = Gravity.LEFT;

                TextView tv = new TextView(thisActivity);
                tv.setText(message_send_by);
                tv.setTextAppearance(thisActivity, R.style.TextviewStyle);
                tv.setPadding(7, 0, 7, 0);
                ll.addView(tv);

                TextView tv2 = new TextView(thisActivity);
                tv2.setText(message_message);
                tv2.setTextAppearance(thisActivity, R.style.SimpleTextviewStyle);
                tv2.setPadding(7, 0, 7, 0);
                ll.addView(tv2);

                if (!flower.getAttachment_id().equals("")) {
                    TextView tv4 = new TextView(thisActivity);
                    tv4.setText("Download File - " + flower.getFile_name() + "." + flower.getAttachment_type());
                    tv4.setTextAppearance(thisActivity, R.style.SimpleTextviewStyle);
                    tv4.setPadding(7, 0, 7, 0);
                    tv4.setTextColor(Color.BLUE);
                    tv4.setId(Integer.valueOf(flower.getAttachment_id()));
                    tv4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filename = flower.getFile_name() + "." + flower.getAttachment_type();
                            new DownloadFile().execute(getResources().getString(R.string.url_reference) + "home/download_file.php" + "?attachment_id=" + flower.getAttachment_id());
                        }
                    });
                    ll.addView(tv4);
                }

                TextView tv3 = new TextView(thisActivity);
                tv3.setText(message_created);
                tv3.setLayoutParams(p3);
                tv3.setTextAppearance(thisActivity, R.style.SimpleTextviewStyle_12);
                tv3.setPadding(7, 0, 7, 0);
                ll.addView(tv3);

                ImageView divider2 = new ImageView(thisActivity);
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
        } else {
            Toast.makeText(thisActivity, "No Response from server", Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    void updatedata(String uri) {

        final StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communicatioin_Staff_Send_School_JSONParser_backup.parserFeed(arg0);
                        updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Send_School.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/communication_recieve_school.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                                        thisActivity.finish();
                                        startActivity(intent);
                                        thisActivity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                Log.d("user_id", user_id);
                Log.d("email", user_email);
                Log.d("school_id", school_id);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.communication_staff_school_send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_menu_click: {
                Intent intent = new Intent(thisActivity, Upload_Image.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                intent.putExtra("direction", "school");
                intent.putExtra("ddefault",ddefault);
                thisActivity.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;
            }
           /* case R.id.upload_menu_server_attach: {
                progress.show();
                file_list(getResources().getString(R.string.url_reference) + "home/select_file.php");
                return true;
            }*/
            case android.R.id.home: {
                Intent intent = new Intent(thisActivity, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                thisActivity.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            }
            default:
                return true;
        }
    }

    void file_list(final String s) {
        StringRequest request = new StringRequest(Request.Method.POST, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
//                Log.d("response", response);
                LinearLayout ll = new LinearLayout(thisActivity);
                ll.setOrientation(LinearLayout.VERTICAL);
                final ArrayList<String> myarray2 = new ArrayList<>();
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int kk = 0; kk < arr.length(); kk++) {
//                            JSONObject obj = arr.getJSONObject(kk);
                        String question23 = arr.getString(kk);
                        myarray2.add(question23);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }
                ListView tv2 = new ListView(thisActivity);
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_list_item_1, android.R.id.text1, myarray2);
                tv2.setAdapter(modeAdapter);

                ll.addView(tv2);
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                builder.setTitle(getResources().getString(R.string.file_atacment))
                        .setCancelable(false)
                        .setView(ll);
                final AlertDialog alert = builder.create();
                alert.show();
                tv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        attach_file = myarray2.get(position);
                        alert.hide();

                        LinearLayout ll2 = new LinearLayout(thisActivity);
                        ll2.setOrientation(LinearLayout.VERTICAL);
                        final EditText et = new EditText(thisActivity);
                        et.setHint(getResources().getString(R.string.enter_message2));
                        et.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
                        et.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                        ll2.addView(et);

                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage("File attached " + attach_file)
                                .setCancelable(false)
                                .setView(ll2)
                                .setNegativeButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int cont = Integer.valueOf(role_id);
                                        if (cont < 4) {
                                            String message = et.getText().toString();
                                            if (!message.trim().equals("")) {
                                                progress.show();
                                                hideSoftKeyboard(thisActivity);
                                                senddata(getResources().getString(R.string.url_reference) + "home/communication_send_school.php", message);
                                            } else {
                                                Toast.makeText(thisActivity, getResources().getString(R.string.insert_message_sending), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(thisActivity, getResources().getString(R.string.permission_error), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(thisActivity, attach_file + " is selected", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                Toast.makeText(thisActivity, volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("here in error", volleyError.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                builder.setMessage(getResources().getString(R.string.message_success))
                        .setCancelable(true)
                        .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                file_list(s);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(thisActivity, SplashScreen.class);
                                thisActivity.finish();
                                startActivity(intent);
                                thisActivity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                params.put("username", user_email);
                params.put("school_id", school_id);
//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(ddefault != null) {
            if(ddefault.equals("ddefault")) {
                Intent intent = new Intent(thisActivity, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                thisActivity.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            } else {
                Intent intent = new Intent(thisActivity, Communications_Staff.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                thisActivity.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        } else {
            Intent intent = new Intent(thisActivity, Communications_Staff.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("email", user_email);
            intent.putExtra("redirection", "Communications");
            thisActivity.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
    }

    // DownloadFile AsyncTask
    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(thisActivity);
            // Set your progress dialog Title
            mProgressDialog.setTitle(getResources().getString(R.string.download));
            // Set your progress dialog Message
            mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory()
                        .getPath();
//                Log.d("filepath", filepath);
//                Log.d("filepath", Environment.DIRECTORY_DOWNLOADS);

                filepath = filepath + "/" + Environment.DIRECTORY_DOWNLOADS;
                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath + "/"
                        + filename);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                // Close connection
                output.flush();
                output.close();
                input.close();
                mProgressDialog.dismiss();

                File pdfFile = new File(filepath + "/" + filename);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFile);
               /* Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "*//*");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    startActivity(pdfIntent);
                } catch(ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(Communication_Staff_Send_School.this, "No Application available to view file", Toast.LENGTH_SHORT).show();
                }*/


                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(filename).substring(1));
                newIntent.setDataAndType(Uri.fromFile(pdfFile), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(thisActivity, getResources().getString(R.string.type_handler), Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                // Error Log
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
            }
            return null;
        }

        private String fileExt(String url) {
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf("."));
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            mProgressDialog.setProgress(progress[0]);
            // Dismiss the progress dialog

        }
    }
}
