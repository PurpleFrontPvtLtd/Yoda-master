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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.RemoveStudentAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Group_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveStudent extends Activity {
    ExpandableListView listview;
   String grade_student;
    List<Group_Model> feedlist=new ArrayList<>();
    ArrayList<String> selectedContactList=new ArrayList<>();
    Activity thisActivity=this;
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    int min_role = 0;

    String section_id = "", section_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_student);
        listview = (ExpandableListView) findViewById(R.id.lv);


        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            grade_student = getIntent().getExtras().getString("grade_student");
            section_id = getIntent().getExtras().getString("section_id");
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            section_name = getIntent().getExtras().getString("section_name");
            min_role = getIntent().getExtras().getInt("min_role");

            JSONArray ar = new JSONArray(grade_student);
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);

                Group_Model groupmodel=new Group_Model();

                final String student_id = obj.getString("student_id");
                final String student_name = obj.getString("student_name");
                groupmodel.setChecked(false);
                groupmodel.setId(student_id);
                groupmodel.setName(student_name);
                feedlist.add(groupmodel);

            }
            listview.setAdapter(new RemoveStudentAdapter(RemoveStudent.this,feedlist));

        }catch (Exception e){
            e.printStackTrace();
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
    public void removeStudents(View view){
        selectedContactList.clear();
        for (int i = 0; i < feedlist.size(); i++) {

//                        updatedSelectIdList.add(groupFeedlist.get(i).getChecked() + "");

            if (feedlist.get(i).getChecked() == true) {
                selectedContactList.add(feedlist.get(i).getId());
            }
        }

        Log.v("list",selectedContactList.toString());
        if (selectedContactList.isEmpty()){
            Toast.makeText(thisActivity,"Please select atleast one contact",Toast.LENGTH_SHORT).show();
        }
        else {

            removeStudentService();
        }
    }
    public void removeStudentService() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/remove_section_students.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);


                        try {
                            JSONObject jObj = new JSONObject(response);

                              Intent intent=new Intent(thisActivity,Grade_Section_view.class);
                            intent.putExtra("branch_id", branch_id);
                            intent.putExtra("section_id", section_id);
                            intent.putExtra("section_name", section_name);
                            intent.putExtra("branch_name", branch_name);
                            intent.putExtra("grade_id", grade_id);
                            intent.putExtra("grade_name", grade_name);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("min_role", min_role);
                            startActivity(intent);
                             finish();
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            //  spnr_branch.setSelection(1);
                        } catch (JSONException e) {
                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        }
                        catch(Exception e)
                        {
                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                        intent.putExtra("school_id", StaticVariable.school_id);
                        intent.putExtra("user_id",StaticVariable.user_id);
                        intent.putExtra("user_email",StaticVariable.email);

                             thisActivity.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Gson gson=new Gson();
                String contactId=gson.toJson(selectedContactList);

                params.put("school_id", StaticVariable.school_id);
                params.put("user_id",StaticVariable.user_id);
                params.put("user_email",StaticVariable.email);
                   params.put("students_ids", contactId);
                   params.put("section_id", section_id);

                Log.d("contactId",contactId);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(thisActivity,Grade_Section_view.class);
        intent.putExtra("branch_id", branch_id);
        intent.putExtra("section_id", section_id);
        intent.putExtra("section_name", section_name);
        intent.putExtra("branch_name", branch_name);
        intent.putExtra("grade_id", grade_id);
        intent.putExtra("grade_name", grade_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email", user_email);
        intent.putExtra("min_role", min_role);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
             onBackPressed();
                return true;
            }
            default:
                return true;
        }
    }

}
