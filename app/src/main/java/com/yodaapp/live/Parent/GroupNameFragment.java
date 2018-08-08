package com.yodaapp.live.Parent;


import android.app.ProgressDialog;
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
import com.yodaapp.live.CreateGroupActivity;
import com.yodaapp.live.MessageViewSent;
import com.yodaapp.live.R;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.adapter.GroupNameAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.MessageGroupDetails_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupNameFragment extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String id = "";
    String name = "";
    String phoneNumber = "";
    public static String db_id = "";
    List<MessageGroupDetails_Model> feedlist;




    ArrayList<String> gname = new ArrayList<>();
    ArrayList<String> unreadlist = new ArrayList<>();
    ArrayList<String> group_id = new ArrayList<>();
    ArrayList<String> CreatedOnList = new ArrayList<>();
    ArrayList<String> createdByList = new ArrayList<>();
    ArrayList<String> scope_id = new ArrayList<>();



    ListView listview;
    View rootView;
    //DisplayAdapter disadpt1, dispadpt2;
    //List<Group_Model> database_message;
    //private DisplayAdapter1 listAdapter;
    private GroupNameAdapter grpAdapter;
    //List<Databaseaccess> database;
    ImageButton group_create;
    String groupId="";

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
//                Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
//                startActivity(intent);
//                getActivity().finish();

                Toast.makeText(getActivity(),"You don't have permission to create a group",Toast.LENGTH_LONG).show();

            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                groupId=group_id.get(i);
                update_read_messages();
                Intent intent = new Intent(getActivity(), com.yodaapp.live.Parent.MessageViewSent.class);
                intent.putExtra("group_id",group_id.get(i));
                intent.putExtra("group_name",gname.get(i));
                intent.putExtra("scopeid",scope_id.get(i));
                StaticVariable.group_id=group_id.get(i);

                Log.v("scopeid",scope_id.get(i));

                StaticVariable.created=CreatedOnList.get(i);
                StaticVariable.createdBy=createdByList.get(i);
                StaticVariable.scopeId=scope_id.get(i);

                startActivity(intent);
                getActivity().finish();

                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);



            }
        });


        return rootView;
    }


   public void createGroup(View view){

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

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
        Log.v("req",request+"");
    }

    protected void send_data() {
        StringRequest request = new StringRequest(Request.Method.GET, getString(R.string.url_reference)+"home/group_list.php?user_id="+StaticVariable.user_id+"&school_id="+StaticVariable.school_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response","resDATA"+ s);
                try {
                    JSONArray jsonArray = new JSONArray(s);

                  //  feedlist= MessageGroupDetails_JSONParser.parserFeed(s);


                    for(int index=0;index<jsonArray.length();index++)
                    {



                        JSONObject jsonInner=jsonArray.getJSONObject(index);

                        String grpname = jsonInner.getString("group_name");

                        Log.v("GRPNAME","GUP"+grpname);
                        String grpid = jsonInner.getString("id");
                        String scopeid = jsonInner.getString("scope_id");
                        String unread = jsonInner.getString("unread");
                        String createdon = jsonInner.getString("created");
                        String createdby = jsonInner.getString("created_by_name");

                        Log.v("MEM","MEMID"+grpid);


                        gname.add(grpname);
                        group_id.add(grpid);
                        scope_id.add(scopeid);
                        unreadlist.add(unread);
                        createdByList.add(createdby);
                        CreatedOnList.add(createdon);

                    }
                    //listAdapter = new DisplayAdapter1(getActivity(), database_message);

                    grpAdapter =  new GroupNameAdapter(getActivity(),gname, scope_id,unreadlist);

                    listview.setAdapter(grpAdapter);
                   // listview.setAdapter(new GroupNameAdapter(getActivity(),feedlist));
                    //listview.setAdapter(listAdapter);





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
