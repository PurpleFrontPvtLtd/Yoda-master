package com.yodaapp.live.Parent;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communication_Parents_Message_Send extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    String student_id = "";
    String student_name = "";
    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();
    String tag_string_req_category = "string_req_category";
    Spinner spnr2;
    String teacher_id = "";
    String message = "";
    List<Createadmin> feedslist22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_parents_message_send);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
           // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progress = new ProgressDialog(Communication_Parents_Message_Send.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        myarray.add("Select");
        myarray2.add("Select");


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

        progress.show();
        spinnerfun();
        spnr2=(Spinner) findViewById(R.id.communication_parents_new_message_spinner);
        ArrayAdapter<String> adapter3=new ArrayAdapter<>(Communication_Parents_Message_Send.this,android.R.layout.simple_spinner_dropdown_item,myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
      //  spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                if (myarray.get(position).equals("Select") || myarray.get(position).equals("")) {
                    teacher_id = "";
                } else {
                    teacher_id = myarray2.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teacher_id = "";
            }
        });
        Button b = (Button) findViewById(R.id.communication_parents_send_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacher_id.equals("")) {
                    Toast.makeText(Communication_Parents_Message_Send.this, R.string.parents_new_messages_teacher_validation, Toast.LENGTH_LONG).show();
                } else {
                    EditText et = (EditText) findViewById(R.id.communication_parents_send_message);
                    message = et.getText().toString();
                    if (message.trim().equals("")) {
                        et.setError(getResources().getString(R.string.parents_new_messages_enter_message));
                        Toast.makeText(Communication_Parents_Message_Send.this, R.string.parents_new_messages_enter_message, Toast.LENGTH_LONG).show();
                    } else {
                        progress.show();
                        updatedata(getResources().getString(R.string.url_reference) + "parent_home/individual_message_new_teacher_send.php");
                    }
                }
            }
        });
    }


    public void updatedata(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progress.hide();
//                        Log.d("response", s);
                        feedslist22 = CreateadminJSONParsers.parserFeed(s);
                        if (feedslist22 != null) {
                            for (final Createadmin flower : feedslist22) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    Intent intent = new Intent(Communication_Parents_Message_Send.this, Communication_Parents_Staff_View.class);
                                    intent.putExtra("id",student_id);
                                    intent.putExtra("name",student_name);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("school_name", school_name);
                                    Communication_Parents_Message_Send.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                                else {
                                    Toast.makeText(Communication_Parents_Message_Send.this,getResources().getString(R.string.unknownerror2),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Communication_Parents_Message_Send.this);
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
                                        Intent intent = new Intent(Communication_Parents_Message_Send.this, SplashScreen.class);
                                        Communication_Parents_Message_Send.this.finish();
                                        startActivity(intent);
                                        Communication_Parents_Message_Send.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("email",user_email);
                params.put("school_id",school_id);
                params.put("message",message);
                params.put("student_id",student_id);
                params.put("teacher_id",teacher_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }
    public void spinnerfun()
    {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"parent_home/teacher_role_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String name = obj.getString("name");
                                String role = obj.getString("role");
                                name = name + " ( "+role+" ) ";
                                myarray.add(name);
                                myarray2.add(id);
//                                Log.d("res", name);
                            }
                            ArrayAdapter<String> adapter3=new ArrayAdapter<>(Communication_Parents_Message_Send.this,android.R.layout.simple_spinner_dropdown_item,myarray);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr2.setAdapter(adapter3);

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Communication_Parents_Message_Send.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Communication_Parents_Message_Send.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Communication_Parents_Message_Send.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("school_id",school_id);
                params.put("user_email",user_email);
                params.put("student_id",student_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
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
                super.onBackPressed();
                Intent intent = new Intent(Communication_Parents_Message_Send.this, Communication_Parents_Staff_View.class);
                intent.putExtra("id",student_id);
                intent.putExtra("name",student_name);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                Communication_Parents_Message_Send.this.finish();
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
        Intent intent = new Intent(Communication_Parents_Message_Send.this, Communication_Parents_Staff_View.class);
        intent.putExtra("id",student_id);
        intent.putExtra("name",student_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("school_name", school_name);
        Communication_Parents_Message_Send.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
