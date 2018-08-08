package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yodaapp.live.GroupActivity1;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.adapter.GroupMessageReceiveAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.model.Group_Message_Receive_Model;
import com.yodaapp.live.parsers.CreateadminJSONParsers;
import com.yodaapp.live.parsers.Group_Message_Receive_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageViewSentOneToOne extends Activity {

    ProgressDialog progress;
    List<Group_Message_Receive_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "", role_id = "";
    String branch_id = "", group_id, group_name, contact_id,contact_name;
    String branch_name = "";
    TextView tv;
    List<Createadmin> feedslist22;
    String school_name;
    ScrollView ss;
    ProgressDialog mProgressDialog;
    String filename;
    String attach_file = "";
    String ddefault = "";
    int screen = 0;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Activity thisActivity = this;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        setContentView(R.layout.communication_staff_branches_send);


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


        recyclerView = (RecyclerView) findViewById(R.id.mesaagelistrecycelr);
        layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        progress = new ProgressDialog(MessageViewSentOneToOne.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        ss = (ScrollView) findViewById(R.id.communication_staff_branch_layout);
        ss.post(new Runnable() {
            @Override
            public void run() {
                ss.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        try {
            contact_id = getIntent().getExtras().getString("contact_id");
            contact_name = getIntent().getExtras().getString("contact_name");
//            group_name = getIntent().getExtras().getString("group_name");

            getActionBar().setTitle(contact_name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TextView tv2 = (TextView) findViewById(R.id.branch_communication_message);
            tv2.setText(branch_name + " " + getResources().getString(R.string.communications));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.communcations_staff_branches_school_name);
        tv.setText(school_name);

     /*   if (StaticVariable.school_id.equals("") || StaticVariable.user_id.equals("")) {
            Toast.makeText(MessageViewSentOneToOne.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/group_message_receive.php");
            } else {
                Toast.makeText(MessageViewSentOneToOne.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }*/

        Button b = (Button) findViewById(R.id.communication_staff_branch_send_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    EditText et = (EditText) findViewById(R.id.communication_staff_branch_send_message);
                    String message = et.getText().toString();
                    if (!message.trim().equals("")) {
                        progress.show();
                        hideSoftKeyboard(MessageViewSentOneToOne.this);
                        et.setText("");
                        senddata(getResources().getString(R.string.url_reference) + "home/create_group_message.php", message);
                    } else {
                        et.setError(getResources().getString(R.string.insert_message_sending));
                        Toast.makeText(MessageViewSentOneToOne.this, getResources().getString(R.string.insert_message_sending), Toast.LENGTH_LONG).show();
                    }

            }
        });

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            screen = 1;
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            screen = 1;
        }

        oneToOneCheck();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void senddata(String s, final String message) {
        StringRequest request = new StringRequest(Request.Method.POST, s,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
                        update_read_messages();
                        updatedata(getResources().getString(R.string.url_reference) + "home/group_message_receive.php");

                        feedslist22 = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist22 != null) {
                            for (final Createadmin flower : feedslist22) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    EditText et = (EditText) findViewById(R.id.communication_staff_branch_send_message);
                                    et.setText("");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                                    builder.setMessage(getResources().getString(R.string.message_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    progress.show();
                                                    updatedata(getResources().getString(R.string.url_reference) + "home/group_message_receive.php");
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(Communication_Staff_Branches_send.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MessageViewSentOneToOne.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Branches_send.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/group_message_receive.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MessageViewSentOneToOne.this, SplashScreen.class);
                                        MessageViewSentOneToOne.this.finish();
                                        startActivity(intent);
                                        MessageViewSentOneToOne.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", StaticVariable.user_id);
                params.put("school_id", StaticVariable.school_id);
                params.put("message", message);
                params.put("group_id", group_id);
                params.put("reg_id", StaticVariable.regid);


//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    protected void update_read_messages() {
        StringRequest request = new StringRequest(Request.Method.GET, getString(R.string.url_reference)+"home/update_read_message.php?user_id="+StaticVariable.user_id+"&group_id="+group_id+"&school_id="+StaticVariable.school_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", "resDATA" + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);


                } catch (Exception e) {
                    Log.d("Exception", "" + e);
                    //  Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(thisActivity, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                Toast.makeText(thisActivity, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error", volleyError.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "269");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Group_Message_Receive_JSONParser.parserFeed(arg0);

                        recyclerView.setAdapter(new GroupMessageReceiveAdapter(feedslist, thisActivity));

                        //   updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(MessageViewSentOneToOne.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_Staff_Branches_send.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                        builder.setMessage(getResources().getString(R.string.nointernetaccess) + "an error has occured try again")
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/group_message_receive.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MessageViewSentOneToOne.this, SplashScreen.class);
                                        MessageViewSentOneToOne.this.finish();
                                        startActivity(intent);
                                        MessageViewSentOneToOne.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", StaticVariable.user_id);
                params.put("email", StaticVariable.email);
                params.put("school_id", StaticVariable.school_id);
                params.put("group_id", group_id);
//                Log.d("user_id", user_id);
//                Log.d("email", user_email);
//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_id);
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
     /*   MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
*//*
        getMenuInflater().inflate(R.menu.info_menu, menu);
        Drawable drawable = menu.findItem(R.id.create_group).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.blue));
        menu.findItem(R.id.create_group).setIcon(drawable);
   */     return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
           /* case R.id.create_group:

                Intent intent = new Intent(thisActivity, Groupinfo.class);
                startActivity(intent);
*/


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
                LinearLayout ll = new LinearLayout(MessageViewSentOneToOne.this);
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
                    e.printStackTrace();
                }
                ListView tv2 = new ListView(MessageViewSentOneToOne.this);
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(MessageViewSentOneToOne.this, android.R.layout.simple_list_item_1, android.R.id.text1, myarray2);
                tv2.setAdapter(modeAdapter);

                ll.addView(tv2);
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                builder.setTitle(getResources().getString(R.string.file_atacment))
                        .setCancelable(true)
                        .setView(ll);
                final AlertDialog alert = builder.create();
                alert.show();
                tv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        attach_file = myarray2.get(position);
                        alert.hide();

                        LinearLayout ll2 = new LinearLayout(MessageViewSentOneToOne.this);
                        ll2.setOrientation(LinearLayout.VERTICAL);
                        final EditText et = new EditText(MessageViewSentOneToOne.this);
                        et.setHint(getResources().getString(R.string.enter_message2));
                        et.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
                        et.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                        ll2.addView(et);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                        builder.setMessage("File attached " + attach_file)
                                .setCancelable(false)
                                .setView(ll2)
                                .setNegativeButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int cont = Integer.valueOf(StaticVariable.role_id);

                                            String message = et.getText().toString();
                                            if (!message.trim().equals("")) {
                                                progress.show();
                                                hideSoftKeyboard(MessageViewSentOneToOne.this);
                                                senddata(getResources().getString(R.string.url_reference) + "home/communication_send_branch.php", message);
                                            } else {
                                                Toast.makeText(MessageViewSentOneToOne.this, getResources().getString(R.string.insert_message_sending), Toast.LENGTH_LONG).show();
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

                        Toast.makeText(MessageViewSentOneToOne.this, attach_file + " is selected", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(MessageViewSentOneToOne.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                Toast.makeText(Communication_Staff_Branches_send.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("here in error", volleyError.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewSentOneToOne.this);
                builder.setMessage(getResources().getString(R.string.error_occured))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                file_list(s);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MessageViewSentOneToOne.this, SplashScreen.class);
                                MessageViewSentOneToOne.this.finish();
                                startActivity(intent);
                                MessageViewSentOneToOne.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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

    public void oneToOneCheck() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/one_to_one_message.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
/*
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            group_id = jsonObj.getString("group_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        try {

                            feedslist = Group_Message_Receive_JSONParser.parserFeed(response);

                            if (feedslist.size() > 0) {
                                group_id = feedslist.get(0).getGroup_id();
                                String id=feedslist.get(0).getId();
                                if (!id.contains("0")){
                                    recyclerView.setAdapter(new GroupMessageReceiveAdapter(feedslist, thisActivity));

                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("contact_id", contact_id);
                params.put("scope_id", "9");
                params.put("group_name", contact_name);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

   public void goBack(){

       Intent intent=new Intent(thisActivity, com.yodaapp.live.Parent.MainActivity_Parent.class);
       intent.putExtra("redirection","Communications");
       startActivity(intent);
       finish();

       overridePendingTransition(R.anim.left_right, R.anim.right_left);

   }
    @Override
    public void onBackPressed() {

        goBack();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MessageViewSentOneToOne Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yodaapp.live/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MessageViewSentOneToOne Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yodaapp.live/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // DownloadFile AsyncTask
    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(MessageViewSentOneToOne.this);
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
//                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                pdfIntent.setDataAndType(path, "*/*");
//                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                try{
//                    startActivity(pdfIntent);
//                } catch(ActivityNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Communication_Staff_Branches_send.this, "No Application available to view file", Toast.LENGTH_SHORT).show();
//                }

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(filename).substring(1));
                newIntent.setDataAndType(Uri.fromFile(pdfFile), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MessageViewSentOneToOne.this, getResources().getString(R.string.type_handler), Toast.LENGTH_LONG).show();
                }

//                Intent i = new Intent();
//                i.setAction(android.content.Intent.ACTION_VIEW);
//                final File theFile = new File(filepath + "/" + filename);
//
//                // Use default tools to open your file
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setData(Uri.fromFile(theFile));
//
//                startActivity(intent);
            } catch (Exception e) {
                // Error Log
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
            }
            return null;
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
