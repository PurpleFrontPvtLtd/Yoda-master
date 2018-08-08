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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


public class Section_Creation extends Activity {


    String school_id = "", branch_id = "", branch_name = "", grade_name = "", user_id = "", user_email = "", grade_id = "",staff_id="",old_staff_id="";
    private String tag_string_req_recieve2 = "string_req_recieve2";
    List<Createadmin> feedslist;
    int min_role = 0;
    String section_name = "",section_id="", section_head_id = "",section_heads_id="",section_heads_name="";
    ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();
    String tag_string_req_category = "string_req_category",redirection="",section_head="";
    Spinner spnr2;
    ProgressDialog progress;
    EditText et;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_creation);

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
        et = (EditText) findViewById(R.id.section_creation_name);
        b = (Button) findViewById(R.id.section_creation_submit);

        progress = new ProgressDialog(Section_Creation.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            grade_id = getIntent().getExtras().getString("grade_id");
            section_name = getIntent().getExtras().getString("section_name");
            section_id = getIntent().getExtras().getString("section_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            min_role = getIntent().getExtras().getInt("min_role");
            redirection = getIntent().getExtras().getString("redirection");
            section_head = getIntent().getExtras().getString("section_head");
            old_staff_id = getIntent().getExtras().getString("section_head_id");


            if (redirection.equals("edit_grade_section")){
                getActionBar().setTitle("Edit section");
                et.setText(section_name);

                b.setVisibility(View.GONE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        myarray.add("Select");
        myarray2.add("Select");
        spinnerfun();
        spnr2 = (Spinner) findViewById(R.id.section_staff_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Section_Creation.this, android.R.layout.simple_spinner_dropdown_item, myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
      //  spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                section_head_id = myarray2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView tv = (TextView) findViewById(R.id.section_creation_grade);
        tv.setText(grade_name);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Section_Creation.this);

                section_name = et.getText().toString();
                if (section_name.equals("")) {
                    Toast.makeText(Section_Creation.this, "Please enter section name", Toast.LENGTH_LONG).show();
                }
               else  if (spnr2.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(Section_Creation.this, "Please select section head", Toast.LENGTH_LONG).show();

                }

                else {
                    if (isonline()) {
                        progress.show();
                        upadedata(getResources().getString(R.string.url_reference) + "home/section_creation.php");
                    } else {
                        Toast.makeText(Section_Creation.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
    void upadedata1(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response",arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    onBackPressed();
                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.section_name_exists));
                                    Toast.makeText(Section_Creation.this, getResources().getString(R.string.section_name_exists), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Section_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(Section_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Section_Creation.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Section_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("branch_id", branch_id);
                params.put("grade_id",grade_id);
                params.put("grade_name", grade_name);
                params.put("section_id", section_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("section_name",section_name);
                params.put("old_staff_id",old_staff_id);
                params.put("staff_id",section_head_id);
//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_id);
//                Log.d("grade_name", grade_name);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_email);
                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void upadedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response",arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(Section_Creation.this, Grade_Detail_View.class);
                                    intent.putExtra("branch_id", branch_id);
                                    intent.putExtra("grade_id", grade_id);
                                    intent.putExtra("grade_name", grade_name);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("branch_name", branch_name);
                                    intent.putExtra("min_role",min_role);
                                    Section_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.section_name_exists));
                                    Toast.makeText(Section_Creation.this, getResources().getString(R.string.section_name_exists), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Section_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(Section_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Section_Creation.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Section_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("branch_id", branch_id);
                params.put("grade_id",grade_id);
                params.put("grade_name", grade_name);
                params.put("user_id", user_id);
                params.put("user_email", user_email);
                params.put("section_name",section_name);
                params.put("section_head_id",section_head_id);
//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_id);
//                Log.d("grade_name", grade_name);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_email);
                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    public void spinnerfun() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/branch_master_data.php",
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
                                String category = obj.getString("name");
                                myarray.add(category);
                                myarray2.add(id);
                               Log.d("res", category);
                            }

                            if (redirection.equals("edit_grade_section")){

                                try {
                                    Log.v("section_head",section_heads_name);

                                    int i = -1;
                                    for (String section : myarray) {
                                        i++;
                                        if (section.equals(section_head)) {
                                            spnr2.setSelection(i);
                                        }
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    spnr2.setSelection(0);

                                }


                            }


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
                        Toast.makeText(Section_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Section_Creation.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Section_Creation.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
//                Log.d("school_id", school_id);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Section_Creation.this, Grade_Detail_View.class);
                intent.putExtra("branch_id", branch_id);
                intent.putExtra("grade_id", grade_id);
                intent.putExtra("grade_name", grade_name);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("branch_name", branch_name);
                intent.putExtra("min_role",min_role);
                intent.putExtra("section_head",section_head);
                intent.putExtra("section_name",section_name);
                intent.putExtra("section_id",section_id);
                intent.putExtra("redirection","edit_grade_section");


                Section_Creation.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            case R.id.save:

                section_name = et.getText().toString();
                if (section_name.equals("")) {
                    Toast.makeText(Section_Creation.this, "Please enter section name", Toast.LENGTH_LONG).show();
                }
                else  if (spnr2.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(Section_Creation.this, "Please select section head", Toast.LENGTH_LONG).show();

                }

                else {
                    if (isonline()) {
                        progress.show();
                        upadedata1(getResources().getString(R.string.url_reference) + "home/edit_section.php");
                    } else {
                        Toast.makeText(Section_Creation.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                    }
                }
                return true;

            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (redirection.equals("edit_grade_section")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_save, menu);
            Drawable drawable = menu.findItem(R.id.save).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.blue));
            menu.findItem(R.id.save).setIcon(drawable);
        }


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Section_Creation.this, Grade_Detail_View.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("min_role",min_role);
        intent.putExtra("section_head",section_head);
        intent.putExtra("section_name",section_name);
        intent.putExtra("section_id",section_id);
        intent.putExtra("redirection","edit_grade_section");


        Section_Creation.this.finish();
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
