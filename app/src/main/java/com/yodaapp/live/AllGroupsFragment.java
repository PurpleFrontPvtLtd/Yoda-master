package com.yodaapp.live;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.GroupNameAdapter;
import com.yodaapp.live.adapter.GroupNameAdapter1;
import com.yodaapp.live.controller.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AllGroupsFragment extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String id = "";
    String name = "";
    String phoneNumber = "";
    public static String db_id = "";

    String school_status="";
    String profile_status="";

    ArrayList<String> unreadlist = new ArrayList<>();


    ArrayList<String> gname = new ArrayList<>();
    ArrayList<String> group_id = new ArrayList<>();
    ArrayList<String> scope_id = new ArrayList<>();
    ArrayList<String> branch_name_list = new ArrayList<>();
    ArrayList<String> grade_name_list = new ArrayList<>();
    ArrayList<String> section_name_list = new ArrayList<>();

    ListView listview;
    View rootView;
    //DisplayAdapter disadpt1, dispadpt2;
    //List<Group_Model> database_message;
    //private DisplayAdapter1 listAdapter;
    private GroupNameAdapter1 grpAdapter;
    //List<Databaseaccess> database;
    ImageButton group_create;
    String groupId="";

    ArrayList<String> CreatedOnList = new ArrayList<>();
    ArrayList<String> createdByList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmentgroup_planet, container, false);

        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        listview = (ListView) rootView.findViewById(R.id.listview_message);
        group_create = (ImageButton) rootView.findViewById(R.id.group_create);
        send_data();
        group_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                groupId=group_id.get(i);
              //  update_read_messages();
                Intent intent = new Intent(getActivity(),MessageViewSent.class);
                intent.putExtra("group_id",group_id.get(i));
                intent.putExtra("group_name",gname.get(i));
                StaticVariable.created=CreatedOnList.get(i);
                StaticVariable.createdBy=createdByList.get(i);
                StaticVariable.scopeId=scope_id.get(i);
                StaticVariable.group_id=group_id.get(i);
                StaticVariable.branch_name=branch_name_list.get(i);
                StaticVariable.grade_name=grade_name_list.get(i);
                StaticVariable.section_name=section_name_list.get(i);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });


        return rootView;
    }

    protected void update_read_messages() {
        StringRequest request = new StringRequest(Request.Method.GET, getString(R.string.url_reference)+"home/update_read_message.php?user_id="+StaticVariable.user_id+"&group_id="+groupId+"&school_id="+StaticVariable.school_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response","resDATA"+ s);
                try {
                    JSONArray jsonArray = new JSONArray(s);


                }
                catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                    //  Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(getActivity(),getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("error",volleyError.getMessage());
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id","269");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    protected void send_data() {
        StringRequest request = new StringRequest(Request.Method.GET, getString(R.string.url_reference)+"home/all_group_list.php?user_id="+StaticVariable.user_id+"&school_id="+StaticVariable.school_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response","resDATA"+ s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int index=0;index<jsonArray.length();index++)
                    {
                        JSONObject jsonInner=jsonArray.getJSONObject(index);

                        String grpname = jsonInner.getString("group_name");

                        Log.v("GRPNAME","GUP"+grpname);
                        String grpid = jsonInner.getString("id");
                        String scopeid = jsonInner.getString("scope_id");
                        String unread = jsonInner.getString("unread");
                        String branch_name = jsonInner.getString("branch_name");
                        String grade_name = jsonInner.getString("grade_name");
                        String section_name = jsonInner.getString("section_name");
                        profile_status=jsonInner.getString("profile_status");
                        school_status=jsonInner.getString("school_status");
                        Log.v("MEM","MEMID"+grpid);

                        String createdon = jsonInner.getString("created");
                        String createdby = jsonInner.getString("created_by_name");

                        gname.add(grpname);
                        group_id.add(grpid);
                        scope_id.add(scopeid);
                        unreadlist.add(unread);
                        branch_name_list.add(branch_name);
                        grade_name_list.add(grade_name);
                        section_name_list.add(section_name);


                        createdByList.add(createdby);
                        CreatedOnList.add(createdon);

                    }
                    StaticVariable.branch_list1=branch_name_list;
                    StaticVariable.grade_list1=grade_name_list;
                    StaticVariable.section_list1=section_name_list;

                    //listAdapter = new DisplayAdapter1(getActivity(), database_message);

                    grpAdapter =  new GroupNameAdapter1(getActivity(),gname, scope_id,unreadlist);




                    if (school_status.contains("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.error_school_inactive))
                                .setCancelable(false)

                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbhelp entry= new dbhelp(getActivity());
                                        entry.open();
                                        entry.logout_user();
                                        entry.close();
                                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        getActivity().finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else if (profile_status.contains("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.error_profile_inactive))
                                .setCancelable(false)

                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbhelp entry= new dbhelp(getActivity());
                                        entry.open();
                                        entry.logout_user();
                                        entry.close();
                                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        getActivity().finish();

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        listview.setAdapter(grpAdapter);
                    }//listview.setAdapter(listAdapter);





                }
                catch (Exception e)
                {
                    Log.d("Exception", "" + e);
                  //  Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(getActivity(),getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("error",volleyError.getMessage());
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id","269");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.chat_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //case R.id.create_group:
//                Intent intent = new Intent(getActivity(), Create_group.class);
//                getActivity().finish();
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
