package com.yodaapp.live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yodaapp.live.adapter.DisplayAdapter1;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Group_Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class create_group2 extends Activity {

    List<Group_Model> database_existing;

    List<String> trueval = new ArrayList<>();
    ListView listview;
    ProgressDialog progress;
    String group_id = "", group_user_id = "", group_name = "";
    private DisplayAdapter1 listAdapter ;
    private String TAG = create_group2.class.getSimpleName();
    private String tag_string_req_recieve2 = "string_req_recieve2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listview = (ListView) findViewById(R.id.lv);

        listAdapter = new DisplayAdapter1(create_group2.this, database_existing,"crtegrp");
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }




/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create:
                    Intent intent = getIntent();
                    group_name = intent.getStringExtra("group_name");

                    Log.v("STATVAlues","STATVA"+StaticVariables.getdata);

                    for(int index =0 ; index < StaticVariables.getdata.size();index++)
                    {

                        if(StaticVariables.getdata.get(index)!= ("false"))
                        {
                            trueval.add(StaticVariables.getdata.get(index));
                        }
                    }

                    Log.v("Final Array","Final Array"+trueval);
                    //Log.v("GNAME","Gname"+group_name);
                   creategroup(getResources().getString(R.string.url_reference) + "home/create_group.php");
                   Toast.makeText(create_group2.this, "Group Created Successfully", Toast.LENGTH_LONG).show();
                  return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    public void creategroup(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Error: " + volleyError.getMessage());
                Toast.makeText(create_group2.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                Toast.makeText(create_group2.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Gson gson = new Gson();

                String grpcrt = gson.toJson(trueval);

                Map<String, String> params = new HashMap<>();


                //  params.put("user_id", group_user_id);
                //Log.v("GrpName","GPName"+group_name);

               // params.put("contact_array", group_name);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

}
