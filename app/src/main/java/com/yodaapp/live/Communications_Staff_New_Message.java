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


public class Communications_Staff_New_Message extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "", role_id = "";
    List<Createadmin> feedslist;
    ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();

    ArrayList<String> myarray_role = new ArrayList<>();
    ArrayList<String> myarray2_role = new ArrayList<>();

    String tag_string_req_category = "string_req_category";

    String tag_string_req_category2 = "string_req_category_role";

    String tag_string_req_category3 = "string_req_category_branch";
    Spinner spnr2, spnr_role;
    String student_id = "", parent_id = "";
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communications_staff_new_message);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progress = new ProgressDialog(Communications_Staff_New_Message.this);
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
            role_id = getIntent().getExtras().getString("role_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        myarray.add("Select");
        myarray2.add("Select");

        myarray_role.add("Select");
        myarray2_role.add("Select");

        progress.show();
        spinnerfun();
        spnr2 = (Spinner) findViewById(R.id.communication_staff_student_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Communications_Staff_New_Message.this, android.R.layout.simple_spinner_dropdown_item, myarray);
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
                    student_id = "";
                    parent_id = "";
                } else {
                    student_id = myarray2.get(position);
                    parent_id = "";
                    progress.show();
                    spinnerfun_role();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                student_id = "";
                parent_id = "";
            }
        });

        spnr_role = (Spinner) findViewById(R.id.communication_staff_parent_spinner);
        ArrayAdapter<String> adapter3_role = new ArrayAdapter<>(Communications_Staff_New_Message.this, android.R.layout.simple_spinner_dropdown_item, myarray_role);
        adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_role.setAdapter(adapter3_role);
        //spnr_role.setFocusableInTouchMode(true);

        spnr_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_role.get(position));
//                Log.d("myaray2", myarray2_role.get(position));
                if (myarray_role.get(position).equals("Select") || myarray_role.get(position).equals("")) {
                    parent_id = "";
                } else {
                    parent_id = myarray2_role.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent_id = "";
            }
        });


        Button b = (Button) findViewById(R.id.communication_staff_new_message_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.communication_staff_new_message);
                message = et.getText().toString();
                if (student_id.equals("") || student_id.equals("Select")) {

                    Toast.makeText(Communications_Staff_New_Message.this, getResources().getString(R.string.select_student), Toast.LENGTH_LONG).show();
                } else if (parent_id.equals("") || parent_id.equals("Select")) {
                    Toast.makeText(Communications_Staff_New_Message.this, getResources().getString(R.string.select_parent), Toast.LENGTH_LONG).show();
                } else if (message.trim().equals("")) {
                    et.setError(getResources().getString(R.string.enter_message));
                    Toast.makeText(Communications_Staff_New_Message.this, getResources().getString(R.string.enter_message), Toast.LENGTH_LONG).show();
                } else {
                    progress.show();
                    updateonlinedata(getResources().getString(R.string.url_reference) + "home/communication_individual_message_insert.php");
                }
            }
        });

    }

    public void spinnerfun() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/communication_student_view.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String name = obj.getString("name");
                                String section = obj.getString("section");
                                String grade = obj.getString("grade");
                                name = name + "(" + grade + "-" + section + ")";
                                myarray.add(name);
                                myarray2.add(id);
                                Log.d("res", name);
                            }

                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Communications_Staff_New_Message.this, android.R.layout.simple_spinner_dropdown_item, myarray);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr2.setAdapter(adapter3);

                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Communications_Staff_New_Message.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Communications_Staff_New_Message.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("school_id", school_id);
                params.put("user_email", user_email);
                params.put("role_id", role_id);
                Log.d("school_id", school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }

    public void spinnerfun_role() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/communication_parents_view.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_role.clear();
                        myarray2_role.clear();
                        myarray_role.add("Select");
                        myarray2_role.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray_role.add(category);
                                myarray2_role.add(id);
//                                Log.d("res", category);
                            }
                            ArrayAdapter<String> adapter3_role = new ArrayAdapter<>(Communications_Staff_New_Message.this, android.R.layout.simple_spinner_dropdown_item, myarray_role);
                            adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_role.setAdapter(adapter3_role);

                        } catch (JSONException e) {
//                            Log.d("response", response);
//                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Communications_Staff_New_Message.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Communications_Staff_New_Message.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("school_id", school_id);
                params.put("user_email", user_email);
                params.put("student_id", student_id);
//                Log.d("school_id", school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category2);
    }

    void updateonlinedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();
//                        Log.d("response", response);
                        feedslist = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success") && !flower.getId().equals("0")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Communications_Staff_New_Message.this);
                                    builder.setMessage(getResources().getString(R.string.message_success))
                                            .setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
//                                                    /*Intent intent = new Intent(Communications_Staff_New_Message.this, Communications_Staff.class);
//                                                    intent.putExtra("user_id", user_id);
//                                                    intent.putExtra("school_id", school_id);
//                                                    intent.putExtra("email", user_email);
//                                                    intent.putExtra("role_id",role_id);
//                                                    Communications_Staff_New_Message.this.finish();
//                                                    startActivity(intent);
//                                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/

                                                    Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("email", user_email);
                                                    intent.putExtra("redirection", "Communications");
                                                    Communications_Staff_New_Message.this.finish();
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(Communications_Staff_New_Message.this, "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
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
                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Communications_Staff_New_Message.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Communications_Staff_New_Message.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("student_id", student_id);
                params.put("parent_id", parent_id);
                params.put("message", message);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
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
                Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("redirection", "Communications");
                Communications_Staff_New_Message.this.finish();
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
        Intent intent = new Intent(Communications_Staff_New_Message.this, MainActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", user_email);
        intent.putExtra("redirection", "Communications");
        Communications_Staff_New_Message.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

}
