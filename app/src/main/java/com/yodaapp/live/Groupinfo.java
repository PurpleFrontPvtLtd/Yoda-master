package com.yodaapp.live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.GroupNameAdapter;
import com.yodaapp.live.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 8/22/2016.
 */
public class Groupinfo extends Activity {
    ProgressDialog progress;

    ArrayList<String> memberslist=new ArrayList<>();

    TextView created_by,created,group_members,group_name,members,member_tv;
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details);

        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        created_by=(TextView)findViewById(R.id.created_by);
        created=(TextView)findViewById(R.id.created_on);
        group_members=(TextView)findViewById(R.id.group_members);
        group_name = (TextView)findViewById(R.id.group_name);
        members = (TextView)findViewById(R.id.members);
        member_tv = (TextView)findViewById(R.id.textView56);

        created_by.setText(StaticVariable.createdBy);
        created.setText(StaticVariable.created);
        group_name.setText(StaticVariable.gr_name);
        String scopeId=StaticVariable.scopeId;
        if (scopeId.equals("1")){
            group_members.setText("School Level");
            member_tv.setVisibility(View.INVISIBLE);
        }
        else if (scopeId.equals("2")){
            group_members.setText("Branch -"+StaticVariable.branch_name);
            member_tv.setVisibility(View.INVISIBLE);
        }
        else if (scopeId.equals("3")){
            group_members.setText("Grade -"+StaticVariable.grade_name);
            member_tv.setVisibility(View.INVISIBLE);
        }
        else if (scopeId.equals("4")){

            group_members.setText("Section-"+StaticVariable.section_name);
            member_tv.setVisibility(View.INVISIBLE);
        }
        else if (scopeId.equals("8")){
            group_members.setText("Adhoc Level");
            group_members();
        }
        else if (scopeId.equals("9")){
            group_members.setText("one to one");
            group_members();
        }

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


    }

    public void group_members() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/group_members_info.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                     Log.d("response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int index=0;index<jsonArray.length();index++)
                            {
                                JSONObject jsonInner=jsonArray.getJSONObject(index);

                                String grpname = jsonInner.getString("name");
                                String created_on = jsonInner.getString("created");

                                created.setText(created_on);
                                memberslist.add(grpname);



                            }
                            members.setText(memberslist.toString());
                            //listAdapter = new DisplayAdapter1(getActivity(), database_message);





                        }
                        catch (Exception e)
                        {
                            Log.d("Exception", "" + e);
                            //  Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();

                        }


                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                             }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("group_id",  StaticVariable.group_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;



            default:
                return true;
        }
    }
}
