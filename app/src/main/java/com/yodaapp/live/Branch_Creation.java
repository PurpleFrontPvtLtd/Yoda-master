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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.model.Databaseaccess;
import com.yodaapp.live.parsers.CreateadminJSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Branch_Creation extends Activity {

    ProgressDialog progress;
    String branch_type = "";
    ArrayList<String> myarray= new ArrayList<>();
    ArrayList<String> myarray2= new ArrayList<>();
    List<Databaseaccess> database;
    Spinner spnr2;
    String tag_string_req_category = "string_req_category";
    String branch_head_id = "",redirection="",min_role="",branch_head="",old_staff_id="";
    List<Createadmin> feedslist;
    String branch_name="",branch_address="",branch_email="",branch_phone="",branch_facebook="",branch_website="",branch_timing="";
    String school_id = "", user_id = "", user_email = "",branch_id = "",school_name = "";
    protected String tag_string_req_recieve2 = "string_req_recieve2";
    EditText et,et2,et3,et4,et5,et6,et7;
    CheckBox branch_checkbok;
    String branch_fb ="";
    Button b1;
    Boolean success=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_creation1);

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

        progress = new ProgressDialog(Branch_Creation.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) findViewById(R.id.branch_name);
        et2 = (EditText) findViewById(R.id.branch_address);
        et3 = (EditText) findViewById(R.id.branch_email);
        et4 = (EditText) findViewById(R.id.branch_phone);
        et5 = (EditText) findViewById(R.id.branch_facebook);
        et6 = (EditText) findViewById(R.id.branch_website);
        et7 = (EditText) findViewById(R.id.branch_timing);
        branch_checkbok = (CheckBox)findViewById(R.id.branch_checkbok);
        b1 = (Button) findViewById(R.id.branch_submit);

        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            branch_name = getIntent().getExtras().getString("branch_name");
            branch_head = getIntent().getExtras().getString("branch_head");
            school_name = getIntent().getExtras().getString("school_name");
            min_role = getIntent().getExtras().getString("min_role");
            branch_address = getIntent().getExtras().getString("branch_address");
            branch_email = getIntent().getExtras().getString("branch_email");
            branch_phone = getIntent().getExtras().getString("branch_phone");
            branch_website = getIntent().getExtras().getString("branch_website");
            branch_timing = getIntent().getExtras().getString("branch_timing");
            branch_fb = getIntent().getExtras().getString("branch_fb");
            old_staff_id = getIntent().getExtras().getString("branch_head_id");

            Log.d("BNA","BNA"+branch_name);

            redirection = getIntent().getExtras().getString("redirection");



            if (redirection.equals("edit_branch_section")){
                getActionBar().setTitle("Edit Branch Section");
                et.setText(branch_name);
                et2.setText(branch_address);
                et3.setText(branch_email);
                et4.setText(branch_phone);
                et5.setText(branch_fb);
                et6.setText(branch_website);
                et7.setText(branch_timing);

                b1.setVisibility(View.GONE);
                branch_checkbok.setVisibility(View.GONE);


            }








        } catch (Exception e) {
            e.printStackTrace();
        }

        myarray.add("Select");
        myarray2.add("Select");
        spinnerfun();
        spnr2=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter3=new ArrayAdapter<>(Branch_Creation.this,android.R.layout.simple_spinner_dropdown_item,myarray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr2.setAdapter(adapter3);
//        spnr2.setFocusableInTouchMode(true);



        CheckBox ch = (CheckBox) findViewById(R.id.branch_checkbok);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    branch_type = "1";
                }
                else {
                    branch_type = "0";
                }
            }
        });

        spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

  //              Log.d("myaray",myarray.get(position));
  //              Log.d("myaray2",myarray2.get(position));
                branch_head_id = myarray2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                branch_name = et.getText().toString();
                branch_address = et2.getText().toString();
                branch_email = et3.getText().toString();
                branch_phone =et4.getText().toString();
                branch_facebook = et5.getText().toString();
                branch_website = et6.getText().toString();
                branch_timing = et7.getText().toString();




                if(branch_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.branch_creation_branch_name));
                    Toast.makeText(Branch_Creation.this,getResources().getString(R.string.branch_creation_branch_name),Toast.LENGTH_LONG).show();
                }

                else if (spnr2.getSelectedItem().toString().contains("Select")){
                    Toast.makeText(getApplicationContext(), "Please select branch head", Toast.LENGTH_LONG).show();

                }
          //      ||!branch_phone.trim().equals("")||!branch_website.trim().equals("")
                else {
                    success=true;

                    if(!branch_email.trim().equals("")) {

                         if (!android.util.Patterns.EMAIL_ADDRESS.matcher(branch_email).matches()) {
                             Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                             et3.setError("Invalid Email");
                             success=false;
                         }
                         else{
                             success=true;
                         }


                     }

                    if(!branch_phone.trim().equals("")) {

                        if (!android.util.Patterns.PHONE.matcher(et4.getText().toString()).matches()) {
                            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                            et4.setError("Invalid PhoneNumber");
                            success=false;
                        }
                        else{
                            success=true;
                        }
                    }


                    if(!branch_website.trim().equals("")) {

                        if (!android.util.Patterns.PHONE.matcher(et6.getText().toString()).matches()) {
                            Toast.makeText(getApplicationContext(), "Invalid Website", Toast.LENGTH_LONG).show();
                            et6.setError("Invalid Website");
                            success=false;
                        }
                        else{
                            success=true;
                        }
                    }



                    if (success) {

                        if (isonline()) {
                            progress.show();
                            updatedisplay(getResources().getString(R.string.url_reference) + "home/branch_creation.php", branch_name, branch_address, branch_email, branch_phone, branch_facebook, branch_website, branch_timing);
                        } else {
                            Toast.makeText(Branch_Creation.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                        }
                    }


                }
}







        });

    }

    public void spinnerfun()
    {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_master_data.php?school_id="+school_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
   //                     Log.d("response",response);
                        progress.hide();
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray.add(category);
                                myarray2.add(id);
  //                              Log.d("res", category);
                            }

                            if (redirection.equals("edit_branch_section")){

                                try {
                                    Log.v("section_head",branch_head);

                                    int i = -1;
                                    for (String section : myarray) {
                                        i++;
                                        if (section.equals(branch_head)) {
                                            spnr2.setSelection(i);
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    spnr2.setSelection(0);

                                }


                            }




                        } catch (JSONException e) {
     //                       Log.d("response",response);
    //                        Log.d("error in json", "l "+ e);

                        }
                        catch(Exception e)
                        {
      //                      Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
	//			        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Branch_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Branch_Creation.this.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id",school_id);
     //           Log.d("school_id",school_id);
                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }
    void updatedisplay1(String uri, final String branch_name, final String branch_address, final String branch_email, final String branch_phone, final String branch_facebook, final String branch_website, final String branch_timing) {

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
                                    Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    Branch_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                            }
                        }
                        //                  Log.d("here in sucess", "sucess");
                        progress.hide();

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Branch_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        //                       Toast.makeText(Branch_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Branch_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("branch_name", branch_name);
                params.put("branch_address", branch_address);
                params.put("branch_email", branch_email);
                params.put("branch_phone", branch_phone);
                params.put("branch_facebook", branch_facebook);
                params.put("branch_website", branch_website);
                params.put("branch_timing", branch_timing);
                params.put("staff_id", branch_head_id);
                params.put("old_staff_id", old_staff_id);
                params.put("branch_type", branch_type);
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("branch_id", branch_id);


                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updatedisplay(String uri, final String branch_name, final String branch_address, final String branch_email, final String branch_phone, final String branch_facebook, final String branch_website, final String branch_timing) {

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
                                    Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    Branch_Creation.this.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }

                                else if(flower.getSucess().equals("existing userid")) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.branch_existing_message), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
      //                  Log.d("here in sucess", "sucess");
                        progress.hide();

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Branch_Creation.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
 //                       Toast.makeText(Branch_Creation.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        Branch_Creation.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("branch_name", branch_name);
                params.put("branch_address", branch_address);
                params.put("branch_email", branch_email);
                params.put("branch_phone", branch_phone);
                params.put("branch_facebook", branch_facebook);
                params.put("branch_website", branch_website);
                params.put("branch_timing", branch_timing);
                params.put("branch_head_id", branch_head_id);
                params.put("branch_type", branch_type);
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);

                return params;
            };
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);*/

        if (redirection.equals("edit_branch_section")) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:


                branch_name = et.getText().toString();
                branch_address = et2.getText().toString();
                branch_email = et3.getText().toString();
                branch_phone =et4.getText().toString();
                branch_facebook = et5.getText().toString();
                branch_website = et6.getText().toString();
                branch_timing = et7.getText().toString();



                if(!branch_name.trim().equals("")) {
                    if (isonline()) {
                        progress.show();
                        updatedisplay1(getResources().getString(R.string.url_reference) + "home/edit_branch.php", branch_name, branch_address, branch_email, branch_phone, branch_facebook, branch_website, branch_timing);
                    } else {
                        Toast.makeText(Branch_Creation.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    et.setError(getResources().getString(R.string.branch_creation_branch_name));
                    Toast.makeText(Branch_Creation.this,getResources().getString(R.string.branch_creation_branch_name),Toast.LENGTH_LONG).show();
                }
                return true;



            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(redirection.equals("edit_branch_section"))
        {
            Intent intent =  new Intent(Branch_Creation.this,Branch_Detail_View.class);

            intent.putExtra("branch_id", branch_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            intent.putExtra("min_role", min_role);
            intent.putExtra("branch_name", branch_name);
            intent.putExtra("min_role", getIntent().getExtras().getInt("min_role"));
            Branch_Creation.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);

        }
        else {

            Intent intent = new Intent(Branch_Creation.this, MainActivity.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            Branch_Creation.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
    }


    public void clickBranch(View view)
    {

        et.requestFocus();
    }

    public void clickAdd(View view)
    {
        et2.requestFocus();
    }
    public void clickMail(View view)
    {
        et3.requestFocus();
    }
    public void clickPh(View view)
    {
        et4.requestFocus();
    }
    public void clickFb(View view)
    {
        et5.requestFocus();
    }
    public void clickWebsite(View view)
    {
        et6.requestFocus();
    }
    public void clickTiming(View view)
    {
        et7.requestFocus();
    }

}
