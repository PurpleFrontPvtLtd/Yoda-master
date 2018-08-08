package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yodaapp.live.CreateGroupActivity;
import com.yodaapp.live.GroupActivity1;
import com.yodaapp.live.MainActivity;
import com.yodaapp.live.MessageViewSent;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.adapter.DisplayAdapter2;
import com.yodaapp.live.adapter.DisplayAdapter3;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Group_Model1;
import com.yodaapp.live.model.ParentDetail_Model;
import com.yodaapp.live.model.Staff_Details_Model;
import com.yodaapp.live.parsers.IndividualContactsListParser1;
import com.yodaapp.live.parsers.Staff_Details_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersSelectActivity extends Activity implements
        SearchView.OnQueryTextListener{
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    int min_role = 0;
    String staff_id = "";
    List<Staff_Details_Model> feedslist;
    List<ParentDetail_Model> feedslist22=new ArrayList<>();
    Button inactivate_staff;
    Activity thisActivity=this;
    ExpandableListView listview3;
    LinearLayout layout,contact_layout;
    ArrayList<String> roleList=new ArrayList<>();

    ArrayList<String> myarray_branch = new ArrayList<>();
    ArrayList<String> myarray2_branch = new ArrayList<>();

    ArrayList<String> myarray_section = new ArrayList<>();
    ArrayList<String> myarray2_section = new ArrayList<>();

    ArrayList<String> myarray_subject = new ArrayList<>();
    ArrayList<String> myarray2_subject = new ArrayList<>();

    ArrayList<String> myarray_grade= new ArrayList<>();
    ArrayList<String> myarray2_grade = new ArrayList<>();

    ArrayList<String> group_types = new ArrayList<>();
    ArrayList<String> selectedContactList = new ArrayList<>();
    Spinner spnr2,spnr_role,spnr_branch,spnr_grade,spnr_section,spnr_members,group_type;
    String status="";
    String staff_id_set = "", role_id_set = "",branch_id_set = "";
    String school_id = "",user_id = "",user_email = "",user_role="";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    String branch_id = "", branch_name = "", grade_id = "", grade_name = "",redirection="";
    String selection_branch_id="";
    String grade_id_set = "";
    String Section_id_set = "";
    String subject_id_set = "";

    String tag_string_req_category3 = "string_req_category_branch";

    String tag_string_req_category4 = "string_req_category_grade";

    String tag_string_req_category5 = "string_req_category_section";

    String tag_string_req_category6 = "string_req_category_subject";

    String staff_branch,staff_section,staff_grade,groupName="",scopeId,branchId="",gradeId="",sectionId="";
    TextView tv_branch,tv_grade,tv_section,select_contact_text;
    LinearLayout sp_branch,sp_grade,sp_section;
    Button btnSubmit;
    RelativeLayout layout1;
    ExpandableListView listview;
    List<Group_Model1> groupFeedlist;
    private DisplayAdapter3 listAdapter ;
    String adhoc_group="";
    EditText group_name;
    LinearLayout group_name_layout;

    View view;
    SearchView search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_select);

        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        tv_branch=(TextView) findViewById(R.id.tv_select_branch);
        tv_grade=(TextView) findViewById(R.id.tv_select_grade);
        tv_section=(TextView) findViewById(R.id.tv_select_section);
        select_contact_text=(TextView) findViewById(R.id.select_contact_text);

        sp_branch=(LinearLayout)findViewById(R.id.sp_select_branch);
        sp_grade=(LinearLayout)findViewById(R.id.sp_select_grade);
        sp_section=(LinearLayout)findViewById(R.id.sp_select_section);
        contact_layout=(LinearLayout)findViewById(R.id.contact_layout);
        group_name_layout=(LinearLayout)findViewById(R.id.group_name_layout);
        btnSubmit =(Button)findViewById(R.id.create_group);
        spnr_section =(Spinner) findViewById(R.id.select_section);
        layout1 =(RelativeLayout) findViewById(R.id.layout1);
        group_name =(EditText) findViewById(R.id.group_name);
        view =(View) findViewById(R.id.view);
        search_view = (SearchView) findViewById(R.id.search_view);

        search_view.setOnQueryTextListener(this);

        listview = (ExpandableListView) findViewById(R.id.lv);


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

        try {
            staff_grade = getIntent().getExtras().getString("staff_grade");
            staff_branch = getIntent().getExtras().getString("staff_branch");
            staff_section = getIntent().getExtras().getString("staff_section");
            user_role = getIntent().getExtras().getString("role");

            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
       //     groupName = getIntent().getExtras().getString("group_name");

            spnr_members=(Spinner) findViewById(R.id.group_type);
            spnr_branch=(Spinner) findViewById(R.id.select_branch);

            spnr_grade=(Spinner) findViewById(R.id.spnr_grade);
            if ((user_role.equals("Teacher"))){
                adhoc_group="section";
                group_types.add("Section Group");
                group_types.add("Adhoc Group");
                group_types.add("One to One");

                spnr_branch.setClickable(false);
                spnr_grade.setClickable(false);
                spnr_branch.setEnabled(false);
                spnr_grade.setEnabled(false);
                spnr_section.setEnabled(false);
                spnr_section.setClickable(false);
            }
            else  if ((user_role.equals("Section Head"))){
                adhoc_group="section";
                  group_types.add("Section Group");
                  group_types.add("Adhoc Group");
                group_types.add("One to One");
                 spnr_branch.setClickable(false);
                 spnr_grade.setClickable(false);
                 spnr_branch.setEnabled(false);
                 spnr_grade.setEnabled(false);
                 spnr_section.setEnabled(false);
                 spnr_section.setClickable(false);
            }

            else if ((user_role.equals("Grade Head"))){
                adhoc_group="grade";

                group_types.add("Grade");
                group_types.add("Section Group");
                group_types.add("Adhoc Group");
                group_types.add("One to One");
                 spnr_branch.setClickable(false);
                 spnr_grade.setClickable(false);
                 spnr_branch.setEnabled(false);
                 spnr_grade.setEnabled(false);

            }
           else if ((user_role.equals("Branch Head"))){
                adhoc_group="branch";

                group_types.add("Branch Group");
                group_types.add("Grade Group");
                group_types.add("Section Group");
                group_types.add("Adhoc Group");
                group_types.add("One to One");
                 spnr_branch.setClickable(false);
                 spnr_branch.setEnabled(false);

             }

            else {
                adhoc_group="parent";

                group_types.add("One to One");
             }


        } catch (Exception e) {
            e.printStackTrace();
        }



        if (isonline()) {
           // progress.show();
           // updatedata(getResources().getString(R.string.url_reference) + "home/staff_details.php");
            spinnerfun_branch();

        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_grade.setAdapter(adapter3_grade);


        myarray_branch.add("Select");
        myarray2_branch.add("Select");

        myarray_grade.add("Select");
        myarray2_grade.add("Select");

        myarray_section.add("Select");
        myarray2_section.add("Select");

        myarray_subject.add("Select");
        myarray2_subject.add("Select");



        spnr_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_grade.get(position));
//                Log.d("myaray2", myarray2_grade.get(position));

                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                    // spnr_section.setVisibility(View.GONE);


                } else
                {
                    spnr_section.setVisibility(View.VISIBLE);
                }

                if(myarray_grade.get(position).equals("Select") || myarray_grade.get(position).equals("")) {
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    grade_id_set = myarray2_grade.get(position);
                    if (status.contains("clear")) {
                 //       progress.show();
                    }                    int pos = spnr_branch.getSelectedItemPosition();
                    spinnerfun_section(myarray2_branch.get(pos), myarray2_grade.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                grade_id_set = "";
                Section_id_set = "";
                subject_id_set = "";
            }
        });



        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_section);
        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_section.setAdapter(adapter3_section);
        // spnr_section.setFocusableInTouchMode(true);



        spnr_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));

                if (myarray_section.get(position).equals("Select") || myarray_section.get(position).equals("")) {
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    Section_id_set = myarray2_section.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Section_id_set = "";
                subject_id_set = "";
            }
        });

         spnr_members.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_section.get(position));
//                Log.d("myaray2", myarray2_section.get(position));
                spnr_branch.setSelection(0);

                spnr_grade.setSelection(0);
                spnr_section.setSelection(0);

                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                try {
               /*      staff_branch = feedslist22.get(position).getBranch();
                     staff_grade = feedslist22.get(position).getGrade();
                     staff_section = feedslist22.get(position).getSection();
*/
                  /*  staff_branch="Hoodi";
                    staff_grade=staff_grade;
                    staff_section="Nursery";
*/

                    spinnerfun_branch();

                    Log.d("branch", staff_branch);
                    Log.d("grade", staff_grade);
                    Log.d("section", staff_section);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (spnr_members.getSelectedItem().toString().contains("School Group")){
                    scopeId="1";
                    sp_branch.setVisibility(View.GONE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);


                    tv_branch.setVisibility(View.GONE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);
                    contact_layout.setVisibility(View.GONE);
                    group_name_layout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);

                }
               else if (spnr_members.getSelectedItem().toString().contains("Branch Group")){
                    scopeId="2";
                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    contact_layout.setVisibility(View.GONE);
                    group_name_layout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);

                } else if (spnr_members.getSelectedItem().toString().contains("Grade Group")){
                    scopeId="3";

                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.VISIBLE);
                    sp_section.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.VISIBLE);
                    contact_layout.setVisibility(View.GONE);
                    group_name_layout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);

                }
                else if (spnr_members.getSelectedItem().toString().contains("Section Group")){
                    scopeId="4";

                    sp_branch.setVisibility(View.VISIBLE);
                    sp_grade.setVisibility(View.VISIBLE);
                    sp_section.setVisibility(View.VISIBLE);

                    tv_branch.setVisibility(View.VISIBLE);
                    tv_section.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.VISIBLE);
                    contact_layout.setVisibility(View.GONE);
                    group_name_layout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);

                }

                else if (spnr_members.getSelectedItem().toString().contains("Adhoc Group")){
                    scopeId="8";
                    sp_branch.setVisibility(View.GONE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.GONE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);
                    school_level_contacts();
                    contact_layout.setVisibility(View.VISIBLE);
                    group_name_layout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    select_contact_text.setVisibility(View.VISIBLE);

                    btnSubmit.setVisibility(View.VISIBLE);

                }

                else if (spnr_members.getSelectedItem().toString().contains("One to One")){
                    scopeId="9";

                    sp_branch.setVisibility(View.GONE);
                    sp_grade.setVisibility(View.GONE);
                    sp_section.setVisibility(View.GONE);

                    tv_branch.setVisibility(View.GONE);
                    tv_section.setVisibility(View.GONE);
                    tv_grade.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);

                    contact_layout.setVisibility(View.VISIBLE);
                    select_contact_text.setVisibility(View.INVISIBLE);
                   group_name_layout.setVisibility(View.GONE);
                   // view.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.INVISIBLE);
//                    school_level_contacts();
                             school_level_contacts();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Log.d("myaray", myarray_branch.get(position));
//                Log.d("myaray2", myarray2_branch.get(position));
                spnr_section.setSelection(0);
                if (status.contains("clear")) {

                 //   progress.show();
                }
                branchId=myarray2_branch.get(position);
                spinnerfun_grade(myarray2_branch.get(position));
                if(myarray_branch.get(position).equals("Select") || myarray_branch.get(position).equals("")) {
                    //  spnr_grade.setVisibility(View.GONE);
                    //  spnr_section.setVisibility(View.GONE);
                } else
                {
                    spnr_grade.setVisibility(View.VISIBLE);
                }
                if(myarray_branch.get(position).equals("Select") || myarray_branch.get(position).equals("")) {
                    branch_id_set = "";
                    grade_id_set = "";
                    Section_id_set = "";
                    subject_id_set = "";
                } else {
                    branch_id_set = myarray2_branch.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                branch_id_set = "";
                grade_id_set = "";
                Section_id_set = "";
                subject_id_set = "";
            }
        });


        ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,group_types);
        adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_members.setAdapter(adapter3_branch);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String group_type=spnr_members.getSelectedItem().toString();
                String branch_type=spnr_branch.getSelectedItem().toString();
                String grade_type=spnr_grade.getSelectedItem().toString();
                String section_type=spnr_section.getSelectedItem().toString();
                groupName=group_name.getText().toString();

                group_type="Adhoc Group";
                if (group_type.contains("School Group")){


                    if (groupName.equals("")){
                        Toast.makeText(thisActivity,getResources().getString(R.string.enter_group_name),Toast.LENGTH_SHORT).show();

                    }
                    else {
                        groupCreatation();
                    }

                }

                else if (group_type.contains("Grade Group")){
                    if (branch_type.equals("")||branch_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                    }
                    else  if (grade_type.equals("")||grade_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select grade",Toast.LENGTH_SHORT).show();

                    }
                    else {

                        if (groupName.equals("")){
                            Toast.makeText(thisActivity,getResources().getString(R.string.enter_group_name),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            groupCreatation();
                        }

                    }

                }

                else if (group_type.contains("Branch Group")){
                      if (branch_type.equals("")||branch_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                    }
                    else {

                          if (groupName.equals("")){
                              Toast.makeText(thisActivity,getResources().getString(R.string.enter_group_name),Toast.LENGTH_SHORT).show();

                          }
                          else {
                              groupCreatation();
                          }

                    }


                }
                else if (group_type.contains("Section Group")){
                    if (branch_type.equals("")||branch_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select branch",Toast.LENGTH_SHORT).show();

                    }
                    else  if (grade_type.equals("")||grade_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select grade",Toast.LENGTH_SHORT).show();

                    }
                    else  if (section_type.equals("")||section_type.equals("Select")) {
                        Toast.makeText(thisActivity,"Please select section",Toast.LENGTH_SHORT).show();

                    }
                    else {

                        if (groupName.equals("")){
                            Toast.makeText(thisActivity,getResources().getString(R.string.enter_group_name),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            groupCreatation();
                        }
                       // Toast.makeText(thisActivity,"Succss",Toast.LENGTH_SHORT).show();

                    }


                }


                else if (group_type.contains("Adhoc Group")){


                    selectedContactList.clear();
                    for (int i = 0; i < groupFeedlist.size(); i++) {

//                        updatedSelectIdList.add(groupFeedlist.get(i).getChecked() + "");

                        if (groupFeedlist.get(i).getChecked() == true) {
                            selectedContactList.add(groupFeedlist.get(i).getId());
                        }
                    }

                    Log.v("list",selectedContactList.toString());
                    if (selectedContactList.isEmpty()){
                        Toast.makeText(thisActivity,"Please select atleast one contact",Toast.LENGTH_SHORT).show();
                    }
                    else {


                        groupName = group_name.getText().toString();

                        if (groupName.equals("")) {
                            Toast.makeText(thisActivity, getResources().getString(R.string.enter_group_name), Toast.LENGTH_SHORT).show();

                        } else {
                            selectedContactList.add(StaticVariable.user_id);

                            individualGroupCreatation();
                        }
                    }
                }

            }
        });

/*

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // Toast.makeText(thisActivity,position+"",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(thisActivity,MessageViewSent.class);
                startActivity(intent);

            }
        });
*/

    }

    public void school_level_contacts() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/individuals_contacts_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response1", response);


                        groupFeedlist= IndividualContactsListParser1.parserFeed(response);

                        listAdapter = new DisplayAdapter3(thisActivity,groupFeedlist,scopeId);
                        listview.setAdapter(listAdapter);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("individual_level", "parent");
                        Log.d("branchId", "" + branchId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category5);
    }


    public void spinnerfun_grade(final String branch_idd) {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/grade_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        if (status.contains("clear")) {
                            progress.hide();
                        }
                        myarray_grade.clear();
                        myarray2_grade.clear();
                        myarray_grade.add("Select");
                        myarray2_grade.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("name");
                                myarray_grade.add(category);
                                myarray2_grade.add(id);
//                                Log.d("res", category);
                            }


                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_grade=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_grade);
                        adapter3_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_grade.setAdapter(adapter3_grade);


                        try {
                            int i = -1;
                            for (String id : myarray_grade) {
                                i++;
                                if (id.equals(staff_grade)) {
                                    spnr_grade.setSelection(i);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            spnr_grade.setSelection(0);
                        }


                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        thisActivity.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("branch_id", branch_idd);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category5);
    }

    public void spinnerfun_section(final String branch, final String grade) {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/section_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();

                        myarray_section.clear();
                        myarray2_section.clear();
                        myarray_section.add("Select");
                        myarray2_section.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("section");
                                myarray_section.add(category);
                                myarray2_section.add(id);
//                                Log.d("res", category);
                            }

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        }
                        catch(Exception e)
                        {
//                            Log.d("json connection", "No internet access" + e);
                        }
                        ArrayAdapter<String> adapter3_section =new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_section);
                        adapter3_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_section.setAdapter(adapter3_section);

                      try {

                            int i = -1;
                            for (String section : myarray_section) {
                                i++;
                                if (section.equals(staff_section)) {
                                    spnr_section.setSelection(i);
                                    status = "clear";
                                }
                            }
                        }catch (Exception e){
                          e.printStackTrace();
                          spnr_section.setSelection(0);

                      }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(thisActivity, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        thisActivity.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("grade_id", grade);
                params.put("branch_id", branch);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category6);
    }
    public void spinnerfun_branch() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/branch_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);

                        myarray_branch.clear();
                        myarray2_branch.clear();
                        myarray_branch.add("Select");
                        myarray2_branch.add("Select");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i=0; i< ar.length(); i++)
                            {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String category = obj.getString("branch_name");
                                myarray_branch.add(category);
                                myarray2_branch.add(id);
//                                Log.d("res", category);
                            }

                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,myarray_branch);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_branch.setAdapter(adapter3_branch);

                            int i = -1;
                            for (String branch1 : myarray_branch) {
                                i++;
                                if (branch1.equals(staff_branch)) {
                                    spnr_branch.setSelection(i);

                                }
                            }
                            //  spnr_branch.setSelection(1);
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
                params.put("school_id", StaticVariable.school_id);
//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void clickGroupName(View view){
        group_name.requestFocus();
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    void updatedata(final String uri ) {

        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response",arg0);
                        feedslist = Staff_Details_JSONParser.parserFeed(arg0);

                        String branch_grade = feedslist.get(0).getDetails();
                        try {
                            JSONArray ar = new JSONArray(branch_grade);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String role = obj.getString("role");
                                String branch_name = obj.getString("branch_name");
                                String grade = obj.getString("grade");
                                String section = obj.getString("section");
                                String subject = obj.getString("subject");
                             /*   if (role.equals("Branch Head")){
                                    role=role+"-"+branch_name;
                                }
                                if (role.equals("Grade Head")){
                                    role=role+"-"+grade;
                                }
                                if (role.equals("Section Head")){
                                    role=role+"-"+section;
                                }
*/
                                roleList.add(role);

                                ParentDetail_Model model=new ParentDetail_Model();
                                model.setBranch(branch_name);
                                model.setRole(role);
                                model.setGrade(grade);
                                model.setSection(section);
                                model.setSubject(subject);

                                feedslist22.add(model);



                            }
                            ArrayAdapter<String> adapter3_branch=new ArrayAdapter<>(thisActivity,android.R.layout.simple_spinner_dropdown_item,roleList);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_members.setAdapter(adapter3_branch);
                        } catch (JSONException e) {
//                    e.printStackTrace();
                        }
                      //  updateddisplay();
//                        Log.d("here in sucess", "sucess");

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Staff_Details.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
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
                                        Intent intent = new Intent(thisActivity, SplashScreen.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        thisActivity.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", user_id);
                params.put("user_email",user_email);
                params.put("staff_id",user_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    public void individualGroupCreatation() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/create_individuals_group.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);


                           try {
                            JSONObject jObj = new JSONObject(response);

                            String group_id=jObj.getString("id");
                               StaticVariable.group_id=group_id;
                               if (scopeId.equals("8")){
                                   StaticVariable.scopeId="8";
                                   Intent intent = new Intent(thisActivity, com.yodaapp.live.Parent.MessageViewSent.class);
                                   intent.putExtra("group_id",group_id);
                                   intent.putExtra("group_name",group_name.getText().toString());
                                   startActivity(intent);
                                   finish();
                                   overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                               }else {
                                   Intent intent = new Intent(thisActivity, GroupActivity1.class);
                                   startActivity(intent);
                                   finish();
                                   overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                               }

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

                        intent.putExtra("group_name", groupName);
                        intent.putExtra("scope_id", scopeId);
                        intent.putExtra("branch_id", branch_id_set);
                        intent.putExtra("grade_id", grade_id_set);
                        intent.putExtra("section_id", Section_id_set);
                        intent.putExtra("section_subject_id", "");
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
                params.put("group_name", groupName);
                params.put("scope_id", scopeId);
                params.put("branch_id", branch_id_set);
                params.put("grade_id", grade_id_set);
                params.put("section_id", Section_id_set);
                params.put("contacts_id", contactId);
                params.put("section_subject_id", "");

                 Log.d("contactId",contactId);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void groupCreatation() {
        StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"home/create_communication_group.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Intent intent = new Intent(thisActivity, com.yodaapp.live.Parent.GroupActivity1.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            try {
                            JSONArray ar = new JSONArray(response);


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

                        intent.putExtra("group_name", groupName);
                        intent.putExtra("scope_id", scopeId);
                        intent.putExtra("branch_id", branch_id_set);
                        intent.putExtra("grade_id", grade_id_set);
                        intent.putExtra("section_id", Section_id_set);
                        intent.putExtra("section_subject_id", "");
                        thisActivity.finish();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id",StaticVariable.user_id);
                params.put("user_email",StaticVariable.email);
                params.put("group_name", groupName);
                params.put("scope_id", scopeId);
                params.put("branch_id", branch_id_set);
                params.put("grade_id", grade_id_set);
                params.put("section_id", Section_id_set);
                params.put("section_subject_id", "");


//                Log.d("school_id",school_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void onBack(){
        Intent intent=new Intent(thisActivity, com.yodaapp.live.Parent.MainActivity_Parent.class);
        intent.putExtra("redirection","Communications");
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.left_right, R.anim.right_left);


    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        onBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
                onBack();
                return true;
            }
            default:
                return true;
        }
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        listAdapter.getFilter().filter(newText);
        listview.setAdapter(listAdapter);
        //Toast.makeText(getApplicationContext(),"CallQuery",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

}
