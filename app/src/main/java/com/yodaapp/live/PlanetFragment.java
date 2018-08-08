package com.yodaapp.live;

import android.app.ActionBar;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.HomePageList;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.MainPage_Model;
import com.yodaapp.live.parsers.MainPage_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlanetFragment extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    int min_role = 0;
    List<MainPage_Model> feedslist;
    ImageView b,b3,b2,addStudent;
    View rootView;
    ArrayList<String> rolesList=new ArrayList<>();
    ArrayList<String> branchList=new ArrayList<>();
    ArrayList<String> branchHeadList=new ArrayList<>();
    ArrayList<String> branchHeadIdList=new ArrayList<>();
    ArrayList<String> staffList=new ArrayList<>();
    ArrayList<String> staffIdList=new ArrayList<>();
    ArrayList<String> subjectList=new ArrayList<>();
    ArrayList<String> branchId=new ArrayList<>();
    ArrayList<String> ownersList=new ArrayList<>();
    ArrayList<String> ownersId=new ArrayList<>();
    ExpandableListView role_list,branch_list,staff_list,subject_list;
    String  owners_json;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planet1, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        role_list=(ExpandableListView)rootView.findViewById(R.id.role_list);
        branch_list=(ExpandableListView)rootView.findViewById(R.id.branch_list);
        staff_list=(ExpandableListView)rootView.findViewById(R.id.staff_list);
        subject_list=(ExpandableListView)rootView.findViewById(R.id.subject_list);

        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);




        b = (ImageView) rootView.findViewById(R.id.addbranch);

        b3 = (ImageView) rootView.findViewById(R.id.addstaff);

        addStudent = (ImageView) rootView.findViewById(R.id.addStudent);

        b2 = (ImageView) rootView.findViewById(R.id.addsubject);



        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            school_id = args.getString("school_id");

        } catch (Exception e) {
            e.printStackTrace();
            user_id = StaticVariable.user_id;
            user_email = StaticVariable.email;
            school_id = StaticVariable.school_id;

        }



        if (StaticVariable.school_id.equals("") || StaticVariable.user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }


        LinearLayout b4 = (LinearLayout) rootView.findViewById(R.id.main_student_view_all);
      //  b4.setPaintFlags(b4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Student_View_All.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role",min_role);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

 LinearLayout b9 = (LinearLayout) rootView.findViewById(R.id.main_admin_view_all);
      //  b4.setPaintFlags(b4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminLists.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role",min_role);
                intent.putExtra("owners_json",owners_json);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });




        LinearLayout s5 = (LinearLayout) rootView.findViewById(R.id.main_subject_view_all);
        //  b4.setPaintFlags(b4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        s5.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Student_View_All.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role",min_role);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });






        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_role == 0) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role >= 4) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(getActivity(), Branch_Creation.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("redirection", "");
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_role == 0) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role >= 4) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(getActivity(), Subject_Creation.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_role == 0) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role >= 4) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(getActivity(), Staff_Creation.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });



        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(min_role == 0) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }

                else if(min_role >= 4) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }

                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(getActivity(), Student_Creation.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("role_id",min_role);
                    intent.putExtra("redirection","home");
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }

            }
        });



        LinearLayout b5 = (LinearLayout) rootView.findViewById(R.id.assign_layout);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (min_role == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
                } else if (min_role >= 4) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.notallowed), Toast.LENGTH_LONG).show();
                } else if (min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(getActivity(), Role_Assign.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.roleerror), Toast.LENGTH_LONG).show();
                }
            }
        });

/*





        TextView b4 = (TextView) rootView.findViewById(R.id.main_student_view_all);
        b4.setPaintFlags(b4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Student_View_All.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("min_role",min_role);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
*/
        return rootView;

    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response1", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = MainPage_JSONParser.parserFeed(arg0);

                        if (feedslist != null) {

                            if (feedslist.get(0).getSchool_status().contains("1")) {
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

                            } else if (feedslist.get(0).getProfile_status().contains("1")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(getResources().getString(R.string.error_profile_inactive))
                                        .setCancelable(false)

                                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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

                                updatedisplay();
                            }


                        } else {


                            updatedisplay();
                        }
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(getActivity(), R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progress.show();
                                        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("user_email", StaticVariable.email);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            try {
                for (final MainPage_Model flower : feedslist) {
                    TextView tv = (TextView) rootView.findViewById(R.id.school_name);
                    tv.requestFocus();
                    tv.setText(flower.getSchool_name());
                    TextView tv2 = (TextView) rootView.findViewById(R.id.name);
                    tv2.setText(flower.getUser_name());
                    TextView tv3 = (TextView) rootView.findViewById(R.id.email);
                    tv3.setText(flower.getUser_email());
                    if(!flower.getUser_contact().equals("null")) {
                        TextView tv4 = (TextView) rootView.findViewById(R.id.contact);
                        tv4.setText(flower.getUser_contact());
                        StaticVariable.contact=flower.getUser_contact();
                    }
                    TextView tv5 = (TextView) rootView.findViewById(R.id.user_type);
                    if(flower.getProfile_type().equals("1")) {
                        tv5.setText("Staff");
                    }

                    try {
                        min_role = Integer.valueOf(flower.getMin_role());
                        StaticVariable.role_id=flower.getMin_role();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    String roles_data = flower.getRoles();
                    try {
                        JSONArray ar = new JSONArray(roles_data);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            String roles_get = obj.getString("role");
                            String roles_section = obj.getString("section");
                            String roles_subject = obj.getString("subject");

                            rolesList.add(roles_get);
//                            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.roles_user_layout);
//
//                            LinearLayout.LayoutParams layout_per = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                            TextView roles_tv = new TextView(getActivity());
//                            roles_tv.setLayoutParams(layout_per);
//                            roles_tv.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
//                            Drawable left = getResources().getDrawable(R.drawable.black_dot);
//                            roles_tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//                            if(!roles_subject.equals("null")) {
//                                roles_tv.setText(roles_get + " ( " + roles_subject + " - " + roles_section + " ) ");
//                            }
//                            else {
//                                roles_tv.setText(roles_get);
//                            }
//                            ll.addView(roles_tv);
                        }
                     //   role_list.setAdapter(new HomePageList(getActivity(),rolesList));
                     //   role_list.setDescendantFocusability(ViewGroup.FOCUS_DOWN);
                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }











                    String branches_get = flower.getBranches();
                    try {
                        JSONArray ar = new JSONArray(branches_get);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            final String branch_id = obj.getString("id");
                             String branch_name = obj.getString("name");
                            final String branch_head_name = obj.getString("branch_head");
                            final String branch_head_userid = obj.getString("branch_head_userid");
                            String branch_type = obj.getString("type");

                            if (branch_type.equals("1")){
                                branch_name=branch_name+" - Head Branch";
                            }
                            branchList.add(branch_name);



                            branchHeadList.add(branch_head_name);
                            branchHeadIdList.add(branch_head_userid);

                            branchId.add(branch_id);

//                            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.branch_address_main_layout);
//                            LinearLayout ll_hor = new LinearLayout(getActivity());
//                            LinearLayout.LayoutParams p_hor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                            ll_hor.setOrientation(LinearLayout.HORIZONTAL);
//                            ll_hor.setLayoutParams(p_hor);
//
//                            LinearLayout.LayoutParams layout_per = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                            final TextView b = new TextView(getActivity());
//                            b.setId(Integer.valueOf(branch_id));
//                            b.setText(branch_name);
//                            b.setTextAppearance(getActivity(),R.style.SimpleTextviewStyle);
//                            b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                            Drawable left = getResources().getDrawable(R.drawable.black_dot);
//                            b.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//                            b.setLayoutParams(layout_per);
//                            b.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getActivity(), Branch_Detail_View.class);
//                                    intent.putExtra("id", String.valueOf(b.getId()));
//                                    intent.putExtra("branch_name", b.getText().toString());
//                                    intent.putExtra("user_id", user_id);
//                                    intent.putExtra("school_id", school_id);
//                                    intent.putExtra("email", user_email);
//                                    intent.putExtra("min_role",min_role);
//                                    getActivity().finish();
//                                    startActivity(intent);
//                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                                }
//                            });
//                            ll_hor.addView(b);
//                            if (branch_type.equals("1")) {
//                                final CheckBox ch = new CheckBox(getActivity());
//                                ch.setText("Main");
//                                ch.setChecked(true);
//                                ch.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        ch.setChecked(true);
//                                    }
//                                });
//                                ll_hor.addView(ch);
//                            }
//                            ll.addView(ll_hor);
                        }
                        branch_list.setAdapter(new HomePageList(getActivity(),branchList));
                        branch_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Branch_Detail_View.class);
                                    intent.putExtra("branch_id",branchId.get(position));
                                    intent.putExtra("branch_name", branchList.get(position));
                                    intent.putExtra("branch_head", branchHeadList.get(position));
                                    intent.putExtra("branch_head_id", branchHeadIdList.get(position));
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("min_role",min_role);
                                    getActivity().finish();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });
                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }



                     owners_json = flower.getOwners();
                    try {
                        JSONArray ar = new JSONArray(owners_json);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            String staff_id = obj.getString("id");
                            String staff_name = obj.getString("name");

                            ownersList.add(staff_name);
                            ownersId.add(staff_id);


                        }
                        role_list.setAdapter(new HomePageList(getActivity(),ownersList));
                        role_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Staff_Details.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("min_role",min_role);
                                intent.putExtra("staff_id", ownersId.get(position));
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });

                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }





                    String branch_staff = flower.getStaff();
                    try {
                        JSONArray ar = new JSONArray(branch_staff);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            String staff_id = obj.getString("id");
                            String staff_name = obj.getString("name");

                            staffList.add(staff_name);
                            staffIdList.add(staff_id);


                        }
                        staff_list.setAdapter(new HomePageList(getActivity(),staffList));
                        staff_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Staff_Details.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("min_role",min_role);
                                    intent.putExtra("staff_id", staffIdList.get(position));
                                    getActivity().finish();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });

                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }

                    String school_subject = flower.getSubject();
                    try {
                        JSONArray ar = new JSONArray(school_subject);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            String subject_id = obj.getString("id");
                            String subject_name = obj.getString("name");
                            subjectList.add(subject_name);

//                            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.subject_main_layout);
//
//                            LinearLayout ll_hor = new LinearLayout(getActivity());
//                            LinearLayout.LayoutParams p_hor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                            ll_hor.setOrientation(LinearLayout.HORIZONTAL);
//                            ll_hor.setLayoutParams(p_hor);
//
//                            LinearLayout.LayoutParams layout_per = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//
//                            final TextView b = new TextView(getActivity());
//                            Drawable left = getResources().getDrawable(R.drawable.black_dot);
//                            b.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//                            b.setId(Integer.valueOf(subject_id));
//                            b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                            b.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
//                            b.setLayoutParams(layout_per);
//                            b.setText(subject_name);
//                            b.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getActivity(), Subject_Details_Home.class);
//                                    intent.putExtra("user_id", user_id);
//                                    intent.putExtra("school_id", school_id);
//                                    intent.putExtra("email", user_email);
//                                    intent.putExtra("min_role",min_role);
//                                    intent.putExtra("subject_id", String.valueOf(b.getId()));
//                                    getActivity().finish();
//                                    startActivity(intent);
//                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                                }
//                            });
//                            ll_hor.addView(b);
//
//                            ll.addView(ll_hor);

                        }
                        subject_list.setAdapter(new HomePageList(getActivity(),subjectList));

                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }
                }
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getActivity(),getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();

        Log.d("RO","RO"+min_role);

        if(min_role == 0 || min_role >= 4)
        {
            b.setVisibility(View.GONE);
            b3.setVisibility(View.GONE);
            addStudent.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
        }
        else
        {
            b.setVisibility(View.VISIBLE);
            b3.setVisibility(View.VISIBLE);
            addStudent.setVisibility(View.VISIBLE);
            b2.setVisibility(View.VISIBLE);
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
}
