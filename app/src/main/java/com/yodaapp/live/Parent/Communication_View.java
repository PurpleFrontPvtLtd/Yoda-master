package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent_Model.Communication_View_Model;
import com.yodaapp.live.Parent_Parsers.Communication_View_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.adapter.MessageListAdapter;
import com.yodaapp.live.adapter.MessageListAdapter1;
import com.yodaapp.live.controller.AppController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Communication_View extends Activity {


    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    String student_id = "";
    String student_name = "";
    List<Communication_View_Model> feedslist;
    ScrollView ss;
    ProgressDialog mProgressDialog;
    String filename;
    String attach_file = "";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Activity thisActivity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_parent_view);
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

        progress = new ProgressDialog(Communication_View.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

      /*  ss = (ScrollView) findViewById(R.id.communcations_parent_view_layout);
        ss.post(new Runnable() {
            @Override
            public void run() {
                ss.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
*/


        try {
            student_id = getIntent().getExtras().getString("id");
            student_name = getIntent().getExtras().getString("name");
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* TextView tv = (TextView) findViewById(R.id.communcations_parent_student_name);
        tv.setText(student_name);
*/
        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(Communication_View.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "parent_home/communication_parents_student_view.php");
            } else {
                Toast.makeText(Communication_View.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }

    }


    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_View_JSONParser.parserFeed(arg0);
                        recyclerView.setAdapter(new MessageListAdapter1(feedslist,thisActivity));

                        //   updatedisplay();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Communication_View.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Communication_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_View.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/communication_recieve_section.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Communication_View.this, SplashScreen.class);
                                        Communication_View.this.finish();
                                        startActivity(intent);
                                        Communication_View.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
        //    LinearLayout ll_main = (LinearLayout) findViewById(R.id.communcations_parent_view_layout_linear);
         //   ll_main.removeAllViews();
            for (final Communication_View_Model flower : feedslist) {
                String message_id = flower.getId();
                String message_message = flower.getMessages();
                String message_send_by = flower.getUser_name();
                String message_created = flower.getCreated();
                SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                try {
                    Date date = srcDf.parse(message_created);
                    SimpleDateFormat destDf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
                    message_created = destDf.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LinearLayout ll = new LinearLayout(Communication_View.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setBackgroundColor(Color.parseColor("#eaeaea"));
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                p3.gravity = Gravity.RIGHT;

                TextView tv = new TextView(Communication_View.this);
                tv.setText(message_send_by);
                tv.setTextAppearance(Communication_View.this, R.style.TextviewStyle);
                tv.setPadding(7,0,7,0);
                ll.addView(tv);

                TextView tv2 = new TextView(Communication_View.this);
                tv2.setText(message_message);
                tv2.setTextAppearance(Communication_View.this, R.style.SimpleTextviewStyle);
                tv2.setPadding(7, 0, 7, 0);
                ll.addView(tv2);

                if(!flower.getAttachment_id().equals("")) {
                    TextView tv4 = new TextView(Communication_View.this);
                    tv4.setText("Download File - " + flower.getFile_name() + "." + flower.getAttachment_type());
                    tv4.setTextAppearance(Communication_View.this, R.style.SimpleTextviewStyle);
                    tv4.setPadding(7, 0, 7, 0);
                    tv4.setTextColor(Color.BLUE);
                    tv4.setId(Integer.valueOf(flower.getAttachment_id()));
                    tv4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filename = flower.getFile_name() + "." + flower.getAttachment_type();
                            new DownloadFile().execute(getResources().getString(R.string.url_reference) + "home/download_file.php"+"?attachment_id="+flower.getAttachment_id());
                        }
                    });
                    ll.addView(tv4);
                }

                TextView tv3 = new TextView(Communication_View.this);
                tv3.setText(message_created);
                tv3.setLayoutParams(p3);
                tv3.setTextAppearance(Communication_View.this, R.style.SimpleTextviewStyle_12);
                tv3.setPadding(7, 0, 7, 0);
                ll.addView(tv3);

                ImageView divider2 = new ImageView(Communication_View.this);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                lp2.setMargins(0, 0, 0, 0);
                divider2.setLayoutParams(lp2);
                divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ll.addView(divider2);
                //ll_main.addView(ll);
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
            Toast.makeText(Communication_View.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
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
                Intent intent = new Intent(Communication_View.this, MainActivity_Parent.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("redirection", "Communications");
                Communication_View.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }


    // DownloadFile AsyncTask
    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(Communication_View.this);
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
//                Log.d("filepath",filepath);
//                Log.d("filepath",Environment.DIRECTORY_DOWNLOADS);

                filepath = filepath +"/" +Environment.DIRECTORY_DOWNLOADS;
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
//                    Toast.makeText(Communication_View.this, "No Application available to view file", Toast.LENGTH_SHORT).show();
//                }

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(filename).substring(1));
                newIntent.setDataAndType(Uri.fromFile(pdfFile),mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Communication_View.this, getResources().getString(R.string.type_handler), Toast.LENGTH_LONG).show();
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
                e.printStackTrace();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Communication_View.this, MainActivity_Parent.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("school_name", school_name);
        intent.putExtra("redirection", "Communications");
        Communication_View.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}