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


public class Grade_Edit extends Activity {

    ProgressDialog progress;
    String school_id = "", branch_id = "", branch_name = "", grade_name = "", grade_head_id = "", grade_head_name = "", user_id = "", user_email = "";
    String grade_id = "", branch_head = "",branch_head_id="";
    List<Createadmin> feedslist;
    Integer min_role =0;
    ArrayList<String> myarray = new ArrayList<>();
    ArrayList<String> myarray2 = new ArrayList<>();
    String tag_string_req_category = "string_req_category",redirection="";
    Spinner spnr2;
    private String tag_string_req_recieve2 = "string_req_recieve2";
    String grade_head_master = "",old_staff_id="",section_head="",section_name="",section_id="";
    EditText et;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_creation);

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

        et = (EditText) findViewById(R.id.grade_creation_name);

         b = (Button) findViewById(R.id.grade_creation_submit);

        progress = new ProgressDialog(Grade_Edit.this);
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
            grade_name = getIntent().getExtras().getString("grade_name");
            min_role = getIntent().getExtras().getInt("min_role");
            branch_head = getIntent().getExtras().getString("branch_head");
            section_id = getIntent().getExtras().getString("section_id");
            section_name = getIntent().getExtras().getString("section_name");
            section_head = getIntent().getExtras().getString("section_head");
            grade_head_master = getIntent().getExtras().getString("grade_head_master");
            redirection = getIntent().getExtras().getString("redirection");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.d("branch_name", branch_name);

        myarray.add("Select");
        myarray2.add("Select");
        spinnerfun();
        spnr2 = (Spinner) findViewById(R.id.grade_head_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Grade_Edit.this, android.R.layout.simple_spinner_dropdown_item, myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
       // spnr2.setFocusableInTouchMode(true);

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray.get(position));
//                Log.d("myaray2", myarray2.get(position));
                grade_head_id = myarray2.get(position);
                grade_head_name = myarray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView tv = (TextView) findViewById(R.id.grade_branch_name);
        tv.setText(branch_name);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Grade_Edit.this);

                grade_name = et.getText().toString();
                if (grade_name.equals("")) {
                    Toast.makeText(Grade_Edit.this, "Select Grade Name", Toast.LENGTH_SHORT).show();

                }

                else if (grade_head_name.equals("Select")){
                    Toast.makeText(Grade_Edit.this, "Select Grade Head", Toast.LENGTH_LONG).show();

                }
                else {
                    if (isonline()) {
                        progress.show();
                        upadedata(getResources().getString(R.string.url_reference) + "home/grade_creation.php");
                    } else {
                        Toast.makeText(Grade_Edit.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    public void spinnerfun() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/branch_master_data.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray.add(category);
                                myarray2.add(id);
//                                Log.d("res", category);
                            }

                            if (redirection.equals("edit_grade_section")){

                                try {
                                    Log.v("section_head",grade_head_master);

                                    int i = -1;
                                    for (String section : myarray) {
                                        i++;
                                        if (section.equals(grade_head_master)) {
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
                        Toast.makeText(Grade_Edit.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Edit.this, SplashScreen.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Edit.this.finish();
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

    void upadedata1(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(Grade_Edit.this, Branch_Detail_View.class);
                                    intent.putExtra("branch_id", branch_id);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("branch_name", branch_name);
                                    intent.putExtra("min_role", getIntent().getExtras().getInt("min_role"));

                                    startActivity(intent);
                                    Grade_Edit.this.finish();
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.grade_name_exists));
                                    Toast.makeText(Grade_Edit.this, getResources().getString(R.string.grade_name_exists), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Grade_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Log.d("Error", arg0.getMessage());
                        Toast.makeText(Grade_Edit.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Grade_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Edit.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Edit.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);

                params.put("grade_name", grade_name);
                params.put("staff_id", grade_head_id);
                params.put("old_staff_id", old_staff_id);
                params.put("user_id", user_id);
                params.put("grade_id", grade_id);

                Log.d("school_id", school_id);
                Log.d("branch_id", branch_id);
                Log.d("grade_name", grade_name);
                Log.d("gradehead_id", grade_head_id);
                Log.d("user_id", user_id);
                Log.d("grade_id", grade_id);
                Log.d("old_staff_id", old_staff_id);
                Log.d("gradehead_id", grade_head_id);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void upadedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
                        feedslist = CreateadminJSONParsers.parserFeed(arg0);
                        if (feedslist != null) {
                            for (final Createadmin flower : feedslist) {
                                if (flower.getSucess().equals("success")) {
                                    Intent intent = new Intent(Grade_Edit.this, Branch_Detail_View.class);
                                    intent.putExtra("id", branch_id);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("branch_name", branch_name);
                                    intent.putExtra("min_role", getIntent().getExtras().getInt("min_role"));
                                    startActivity(intent);
                                    Grade_Edit.this.finish();

                                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                                }
                                else if(flower.getSucess().equals("existing")) {
                                    et.setError(getResources().getString(R.string.grade_name_exists));
                                    Toast.makeText(Grade_Edit.this, getResources().getString(R.string.grade_name_exists), Toast.LENGTH_LONG).show();
                                }
//                                else {
//                                    Toast.makeText(Grade_Creation.this, flower.getId(), Toast.LENGTH_LONG).show();
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
                        Log.d("Error", arg0.getMessage());
                        Toast.makeText(Grade_Edit.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Grade_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Grade_Edit.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Grade_Edit.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("branch_id", branch_id);
                params.put("grade_name", grade_name);
                params.put("grade_head_id", grade_head_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);

//                Log.d("school_id", school_id);
//                Log.d("branch_id", branch_id);
//                Log.d("grade_name", grade_name);
//                Log.d("grade_head_id", grade_head_id);
//                Log.d("user_id", user_id);
//                Log.d("user_email", user_email);
                return params;
            }

            ;
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
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
                Intent intent1 = new Intent(Grade_Edit.this, Grade_Detail_View.class);
                intent1.putExtra("branch_id",branch_id);
                intent1.putExtra("branch_name",branch_name);
                intent1.putExtra("school_id",school_id);
                intent1.putExtra("user_id",user_id);
                intent1.putExtra("user_email",user_email);
                intent1.putExtra("grade_id",grade_id);
                intent1.putExtra("grade_name",grade_name);
                intent1.putExtra("section_name",section_name);
                intent1.putExtra("section_head",section_head);
                intent1.putExtra("grade_head_master",grade_head_master);
                intent1.putExtra("redirection",redirection);
                intent1.putExtra("section_id",section_id);
                intent1.putExtra("min_role",min_role);
                intent1.putExtra("branch_head",branch_head);
                intent1.putExtra("grade_head_id",grade_head_id);
                intent1.putExtra("grade_head_master",grade_head_master);

                Grade_Edit.this.finish();
                startActivity(intent1);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);

                return true;

            case R.id.save:

                grade_name = et.getText().toString();
                if (grade_name.equals("")) {
                    Toast.makeText(Grade_Edit.this, "Select Grade Name", Toast.LENGTH_SHORT).show();

                }

                else if (grade_head_name.equals("Select")){
                    Toast.makeText(Grade_Edit.this, "Select Grade Head", Toast.LENGTH_LONG).show();

                }
                else {
                    if (isonline()) {
                        progress.show();
                        upadedata1(getResources().getString(R.string.url_reference) + "home/edit_grade.php");
                    } else {
                        Toast.makeText(Grade_Edit.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }

                }

            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_save, menu);
            Drawable drawable = menu.findItem(R.id.save).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.blue));
            menu.findItem(R.id.save).setIcon(drawable);



        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Grade_Edit.this, Grade_Detail_View.class);
        intent1.putExtra("branch_id",branch_id);
        intent1.putExtra("branch_name",branch_name);
        intent1.putExtra("school_id",school_id);
        intent1.putExtra("user_id",user_id);
        intent1.putExtra("user_email",user_email);
        intent1.putExtra("grade_id",grade_id);
        intent1.putExtra("grade_name",grade_name);
        intent1.putExtra("section_name",section_name);
        intent1.putExtra("section_head",section_head);
        intent1.putExtra("grade_head_master",grade_head_master);
        intent1.putExtra("redirection",redirection);
        intent1.putExtra("section_id",section_id);
        intent1.putExtra("min_role",min_role);
        intent1.putExtra("branch_head",branch_head);
        intent1.putExtra("redirection",redirection);
        intent1.putExtra("grade_head_master",grade_head_master);


        startActivity(intent1);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
        Grade_Edit.this.finish();

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
