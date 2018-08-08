package com.yodaapp.live;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Communication_Staff_Home_Model;
import com.yodaapp.live.model.Databaseaccess;
import com.yodaapp.live.parsers.Communication_Staff_Home_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communication_Staff_Home extends Fragment {

    View rootView;

    ProgressDialog progress;
    List<Communication_Staff_Home_Model> feedslist;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "";
    String school_name;
    String role_id = "";
    ArrayList<String> branch_id = new ArrayList<>();
    ArrayList<String> branch_name = new ArrayList<>();
    ArrayList<String> grade_id = new ArrayList<>();
    ArrayList<String> grade_name = new ArrayList<>();
    ArrayList<String> section_id = new ArrayList<>();
    ArrayList<String> section_name = new ArrayList<>();

    int grade_size = 0;
    int branch_size = 0;
    int section_size = 0;
    int set_size = 0;
    List<Databaseaccess> database;
    LinearLayout llo;
    int screen = 0;

//    public static String VIEW_COLOR= "color";
//
//    private CollectionView mCollectionView;
//
//    public interface Callbacks {
//        public void onTopicSelected(MenuEntry menuEntry, View clickedView);
//    }
//
//    private static Callbacks sDummyCallbacks = new Callbacks() {
//
//        @Override
//        public void onTopicSelected(MenuEntry menuEntry, View clickedView) {
//
//        }
//    };

//    private Callbacks mCallbacks = sDummyCallbacks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.communications_staff_home, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            school_id = args.getString("school_id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            screen = 1;
        }
        else if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            screen = 1;
        }

        dbhelp.DatabaseHelper2 dbhep = new dbhelp.DatabaseHelper2(getActivity());
        database = dbhep.getTodo();

        school_name = database.get(0).getSchool_name();

        branch_id.add(database.get(0).getSchool_id());
        branch_name.add(school_name);

        llo = (LinearLayout) rootView.findViewById(R.id.communications_staff_home_layout);

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/staff_communication_home.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        LinearLayout b3 = (LinearLayout) rootView.findViewById(R.id.communication_staff_parent_message);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Communication_Staff_Message_Home.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        LinearLayout b4 = (LinearLayout) rootView.findViewById(R.id.communication_staff_send);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Communications_Staff_New_Message.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        LinearLayout b5 = (LinearLayout) rootView.findViewById(R.id.communication_staff_view_all_group);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Communications_Staff.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                intent.putExtra("ddefault", "ddefault");
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        LinearLayout b6 = (LinearLayout) rootView.findViewById(R.id.communication_staff_messg_send);

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Communications_Staff_to_Staff_New_Message.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);            }
        });
        LinearLayout b7 = (LinearLayout) rootView.findViewById(R.id.communication_staff_to_staff_message);

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Communication_toStaff_View_Messages.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id", role_id);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);            }
        });

        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//
//        mCollectionView = (CollectionView) view.findViewById(R.id.menu_collection_vew);
//
//    }


    void updatedata(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_Staff_Home_JSONParser.parserFeed(arg0);
                        updatedisplay();
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
                                        updatedata(uri);
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

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_email);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            for (final Communication_Staff_Home_Model flower : feedslist) {
                role_id = flower.getRole();
                String branch = flower.getBranch();
                if(!branch.equals("")) {
                    try {
                        JSONArray ar = new JSONArray(branch);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            branch_id.add(obj.getString("id"));
                            branch_name.add(obj.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String grade = flower.getGrade();
                Log.d("grade staff", flower.getGrade());
                if(!grade.equals("")) {
                    try {
                        JSONArray ar = new JSONArray(grade);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            grade_id.add(obj.getString("id"));
                            grade_name.add(obj.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String section = flower.getSection();
                Log.d("section staff", flower.getSection());
                if(!section.equals("")) {
                    try {
                        JSONArray ar = new JSONArray(section);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            section_id.add(obj.getString("id"));
                            section_name.add(obj.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("branch", "" + branch_name);
                Log.d("grade", "" + grade_name);
                Log.d("section", "" + section_name);

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, 200);
                p3.weight = 1;
                p3.setMargins(4, 4, 4, 4);

                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p4.setMargins(5, 0, 0, 0);

                LinearLayout.LayoutParams p5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p5.setMargins(0, 0, 0, 0);
                p5.gravity = Gravity.CENTER;

                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lp2.setMargins(30, 10, 30, 10);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lp.setMargins(20, 70, 20, 10);

                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lp3.setMargins(20, 40, 20, 10);

                TextView tv22 = new TextView(getActivity());
                tv22.setText(getResources().getString(R.string.school_branch_message));
                if(screen == 1) {
                    tv22.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig);
                } else {
                    tv22.setTextAppearance(getActivity(), R.style.TextviewStyle_space);
                }
                tv22.setGravity(Gravity.CENTER);
                llo.addView(tv22);


                for(int i = 0; i < branch_name.size(); i++) {

                    LinearLayout ll = new LinearLayout(getActivity());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                    final LinearLayout llm = new LinearLayout(getActivity());
                    llm.setOrientation(LinearLayout.HORIZONTAL);
                    llm.setLayoutParams(p3);
                    try {
                        if (i == 0) {
                            llm.setId(Integer.valueOf(branch_id.get(i)));
                            llm.setClickable(true);
                            llm.setBackgroundResource(R.drawable.layout_click);
                            llm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), Communication_Staff_Send_School.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("school_id", school_id);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("school_name", school_name);
                                    intent.putExtra("role_id", role_id);
                                    intent.putExtra("ddefault", "ddefault");
                                    getActivity().finish();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                            });

//                            ImageView divider2 = new ImageView(getActivity());
//                            divider2.setLayoutParams(lp);
//                            divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                            llm.addView(divider2);

                            TextView tv = new TextView(getActivity());
                            tv.setText(branch_name.get(i));
                            tv.setLayoutParams(p5);
                            if(screen == 1) {
                                tv.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                            } else {
                                tv.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                            }
                            tv.setGravity(Gravity.CENTER);
                            llm.addView(tv);

//                            ImageView divider3 = new ImageView(getActivity());
//                            divider3.setLayoutParams(lp2);
//                            divider3.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                            llm.addView(divider3);
                        } else {

                            llm.setId(Integer.valueOf(branch_id.get(i)));
                            llm.setClickable(true);
                            llm.setBackgroundResource(R.drawable.layout_click_green);
                            llm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String branch_id_str = String.valueOf(llm.getId());

                                    for (int j = 1; j < branch_id.size(); j++) {
                                        if (branch_id.get(j).equals(branch_id_str)) {
                                            String branch_name_str = branch_name.get(j);
                                            Intent intent = new Intent(getActivity(), Communication_Staff_Branches_send.class);
                                            intent.putExtra("id", branch_id_str);
                                            intent.putExtra("branch_name", branch_name_str);
                                            intent.putExtra("user_id", user_id);
                                            intent.putExtra("school_id", school_id);
                                            intent.putExtra("email", user_email);
                                            intent.putExtra("school_name", school_name);
                                            intent.putExtra("role_id", role_id);
                                            intent.putExtra("ddefault", "ddefault");
                                            startActivity(intent);
                                            getActivity().finish();
                                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                        }
                                    }
                                }
                            });

//                            ImageView divider2 = new ImageView(getActivity());
//                            divider2.setLayoutParams(lp);
//                            divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                            llm.addView(divider2);

                            TextView tv = new TextView(getActivity());
                            tv.setText(branch_name.get(i));
                            tv.setLayoutParams(p5);
                            if(screen == 1) {
                                tv.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                            } else {
                                tv.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                            }
                            tv.setGravity(Gravity.CENTER);
                            llm.addView(tv);

//                            ImageView divider3 = new ImageView(getActivity());
//                            divider3.setLayoutParams(lp2);
//                            divider3.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                            llm.addView(divider3);
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    ll.addView(llm);

                    final LinearLayout llm2 = new LinearLayout(getActivity());
                    llm2.setOrientation(LinearLayout.HORIZONTAL);
                    llm2.setLayoutParams(p3);
                    llm2.setClickable(true);
                    try {
                        i++;
                        llm2.setId(Integer.valueOf(branch_id.get(i)));
                        llm2.setBackgroundResource(R.drawable.layout_click);
                        llm2.setBackgroundResource(R.drawable.layout_click_green);
                        llm2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String branch_id_str = String.valueOf(llm2.getId());

                                for (int j = 1; j < branch_id.size(); j++) {
                                    if (branch_id.get(j).equals(branch_id_str)) {
                                        String branch_name_str = branch_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Branches_send.class);
                                        intent.putExtra("id", branch_id_str);
                                        intent.putExtra("branch_name", branch_name_str);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("ddefault", "ddefault");
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(branch_name.get(i));
                        tv2.setLayoutParams(p5);
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setGravity(Gravity.CENTER);
                        llm2.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll.addView(llm2);

                    final LinearLayout llm3 = new LinearLayout(getActivity());
                    llm3.setOrientation(LinearLayout.HORIZONTAL);
                    llm3.setLayoutParams(p3);
                    llm3.setClickable(true);
                    try {
                        i++;
                        llm3.setId(Integer.valueOf(branch_id.get(i)));
                        llm3.setBackgroundResource(R.drawable.layout_click);
                        llm3.setBackgroundResource(R.drawable.layout_click_green);
                        llm3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String branch_id_str = String.valueOf(llm3.getId());

                                for (int j = 1; j < branch_id.size(); j++) {
                                    if (branch_id.get(j).equals(branch_id_str)) {
                                        String branch_name_str = branch_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Branches_send.class);
                                        intent.putExtra("id", branch_id_str);
                                        intent.putExtra("branch_name", branch_name_str);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("ddefault", "ddefault");
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(branch_name.get(i));
                        tv2.setLayoutParams(p5);
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setGravity(Gravity.CENTER);
                        llm3.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll.addView(llm3);

                    llo.addView(ll);
                }

                if(!grade_name.isEmpty()) {
                    TextView tv33 = new TextView(getActivity());
                    tv33.setText(getResources().getString(R.string.grade_message));
                    if (screen == 1) {
                        tv33.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig);
                    } else {
                        tv33.setTextAppearance(getActivity(), R.style.TextviewStyle_space);
                    }
                    tv33.setGravity(Gravity.CENTER);
                    llo.addView(tv33);
                }

                for(int i = 0;i < grade_name.size();i++) {

                    LinearLayout ll2 = new LinearLayout(getActivity());
                    ll2.setOrientation(LinearLayout.HORIZONTAL);
                    ll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    final LinearLayout llm2 = new LinearLayout(getActivity());
                    llm2.setOrientation(LinearLayout.HORIZONTAL);
                    llm2.setLayoutParams(p3);
                    try {
                        llm2.setId(Integer.valueOf(grade_id.get(i)));
                        llm2.setBackgroundResource(R.drawable.layout_click_orange);
                        llm2.setClickable(true);
                        llm2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String grade_id_str = String.valueOf(llm2.getId());

                                for (int j = 0; j < grade_id.size(); j++) {
                                    if (grade_id.get(j).equals(grade_id_str)) {
                                        String grade_name_str = grade_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Grades_Send.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("grade_id", grade_id_str);
                                        intent.putExtra("grade_name", grade_name_str);
                                        intent.putExtra("ddefault", "ddefault");
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(grade_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm2.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll2.addView(llm2);

                    final LinearLayout llm3 = new LinearLayout(getActivity());
                    llm3.setOrientation(LinearLayout.HORIZONTAL);
                    llm3.setLayoutParams(p3);
                    try {
                        i++;
                        llm3.setId(Integer.valueOf(grade_id.get(i)));
                        llm3.setBackgroundResource(R.drawable.layout_click_orange);
                        llm3.setClickable(true);
                        llm3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String grade_id_str = String.valueOf(llm3.getId());

                                for (int j = 0; j < grade_id.size(); j++) {
                                    if (grade_id.get(j).equals(grade_id_str)) {
                                        String grade_name_str = grade_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Grades_Send.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("grade_id", grade_id_str);
                                        intent.putExtra("grade_name", grade_name_str);
                                        intent.putExtra("ddefault", "ddefault");
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(grade_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm3.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll2.addView(llm3);

                    final LinearLayout llm4 = new LinearLayout(getActivity());
                    llm4.setOrientation(LinearLayout.HORIZONTAL);
                    llm4.setLayoutParams(p3);
                    try {
                        i++;
                        llm4.setId(Integer.valueOf(grade_id.get(i)));
                        llm4.setBackgroundResource(R.drawable.layout_click_orange);
                        llm4.setClickable(true);
                        llm4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String grade_id_str = String.valueOf(llm4.getId());

                                for (int j = 0; j < grade_id.size(); j++) {
                                    if (grade_id.get(j).equals(grade_id_str)) {
                                        String grade_name_str = grade_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Grades_Send.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("grade_id", grade_id_str);
                                        intent.putExtra("grade_name", grade_name_str);
                                        intent.putExtra("ddefault", "ddefault");
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm4.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(grade_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm4.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm4.addView(divider5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll2.addView(llm4);

                    llo.addView(ll2);
                }


                if(!section_name.isEmpty()) {
                    TextView tv44 = new TextView(getActivity());
                    tv44.setText(getResources().getString(R.string.section_message));
                    if (screen == 1) {
                        tv44.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig);
                    } else {
                        tv44.setTextAppearance(getActivity(), R.style.TextviewStyle_space);
                    }
                    tv44.setGravity(Gravity.CENTER);
                    llo.addView(tv44);
                }

                for(int i = 0; i < section_name.size(); i++) {



                    LinearLayout ll3 = new LinearLayout(getActivity());
                    ll3.setOrientation(LinearLayout.HORIZONTAL);
                    ll3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                    final LinearLayout llm2 = new LinearLayout(getActivity());
                    llm2.setOrientation(LinearLayout.HORIZONTAL);
                    llm2.setLayoutParams(p3);

                    try {
                        llm2.setId(Integer.valueOf(section_id.get(i)));
                        llm2.setBackgroundResource(R.drawable.layout_click_pink);
                        llm2.setClickable(true);
                        llm2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String section_id_str = String.valueOf(llm2.getId());

                                for (int j = 0; j < section_id.size(); j++) {
                                    if (section_id.get(j).equals(section_id_str)) {
                                        String section_name_str = section_name.get(j);
                                            Intent intent = new Intent(getActivity(), Communication_Staff_Section_Send.class);
                                            intent.putExtra("user_id", user_id);
                                            intent.putExtra("school_id", school_id);
                                            intent.putExtra("email", user_email);
                                            intent.putExtra("school_name", school_name);
                                            intent.putExtra("role_id", role_id);
                                            intent.putExtra("section_id", section_id_str);
                                            intent.putExtra("section_name", section_name_str);
                                            intent.putExtra("ddefault", "ddefault");
                                            getActivity().finish();
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(section_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm2.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm2.addView(divider5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll3.addView(llm2);

                    final LinearLayout llm3 = new LinearLayout(getActivity());
                    llm3.setOrientation(LinearLayout.HORIZONTAL);
                    llm3.setLayoutParams(p3);

                    try {
                        i++;
                        llm3.setId(Integer.valueOf(section_id.get(i)));
                        llm3.setBackgroundResource(R.drawable.layout_click_pink);
                        llm3.setClickable(true);
                        llm3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String section_id_str = String.valueOf(llm3.getId());

                                for (int j = 0; j < section_id.size(); j++) {
                                    if (section_id.get(j).equals(section_id_str)) {
                                        String section_name_str = section_name.get(j);
                                            Intent intent = new Intent(getActivity(), Communication_Staff_Section_Send.class);
                                            intent.putExtra("user_id", user_id);
                                            intent.putExtra("school_id", school_id);
                                            intent.putExtra("email", user_email);
                                            intent.putExtra("school_name", school_name);
                                            intent.putExtra("role_id", role_id);
                                            intent.putExtra("section_id", section_id_str);
                                            intent.putExtra("section_name", section_name_str);
                                            intent.putExtra("ddefault", "ddefault");
                                            getActivity().finish();
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(section_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm3.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm3.addView(divider5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll3.addView(llm3);


                    final LinearLayout llm4 = new LinearLayout(getActivity());
                    llm4.setOrientation(LinearLayout.HORIZONTAL);
                    llm4.setLayoutParams(p3);

                    try {
                        i++;
                        llm4.setId(Integer.valueOf(section_id.get(i)));
                        llm4.setBackgroundResource(R.drawable.layout_click_pink);
                        llm4.setClickable(true);
                        llm4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String section_id_str = String.valueOf(llm4.getId());

                                for (int j = 0; j < section_id.size(); j++) {
                                    if (section_id.get(j).equals(section_id_str)) {
                                        String section_name_str = section_name.get(j);
                                        Intent intent = new Intent(getActivity(), Communication_Staff_Section_Send.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("school_name", school_name);
                                        intent.putExtra("role_id", role_id);
                                        intent.putExtra("section_id", section_id_str);
                                        intent.putExtra("section_name", section_name_str);
                                        intent.putExtra("ddefault", "ddefault");
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                }
                            }
                        });

//                        ImageView divider4 = new ImageView(getActivity());
//                        divider4.setLayoutParams(lp);
//                        divider4.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm4.addView(divider4);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(section_name.get(i));
                        if(screen == 1) {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white);
                        } else {
                            tv2.setTextAppearance(getActivity(), R.style.TextviewStyle_space_MediumBig_white_mobile);
                        }
                        tv2.setLayoutParams(p5);
                        tv2.setGravity(Gravity.CENTER);
                        llm4.addView(tv2);

//                        ImageView divider5 = new ImageView(getActivity());
//                        divider5.setLayoutParams(lp2);
//                        divider5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        llm4.addView(divider5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ll3.addView(llm4);
                    llo.addView(ll3);
                }


                if(branch_name.isEmpty()) {
                    branch_size = 1;
                    set_size = 1;
                } else {
                    branch_size = 1 + branch_name.size();
                    set_size = 1;
                }

                if(grade_name.isEmpty()) {
                    grade_size = 0;
                    set_size = 1;
                } else {
                    grade_size = grade_name.size();
                    set_size = 2;
                }
                if(section_name.isEmpty()) {
                    section_size = 0;
                    set_size = 1;
                } else {
                    section_size = section_name.size();
                    set_size = 3;
                }

//                final NativeDashAdapter adapter = new NativeDashAdapter(getActivity(),buildMenuList());
// //             adapter.setCallbacks(mCallbacks);
//                mCollectionView.setCollectionAdapter(adapter);
//                mCollectionView.updateInventory(adapter.getInventory(set_size,branch_size,grade_size,section_size));
//                mCollectionView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(AbsListView view, int scrollState) {
//                        adapter.hideDescriptionToast();
//                    }
//
//                    @Override
//                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                    }
//                });

            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }


    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }



//    private static List<MenuEntry> buildMenuList(){
//        ArrayList<MenuEntry> list = new ArrayList<MenuEntry>();
//
//        list.add(new MenuEntry(branch_name.get(0),R.color.md_red_800,"Open school or branch message", Communications_Staff.class));
//
//        for(int i = 1;i<branch_name.size();i++) {
//            list.add(new MenuEntry(branch_name.get(i), R.color.md_green_300, "Open school or branch message", Communications_Staff.class));
//        }
//
//        for (int i = 0;i<grade_name.size();i++) {
//            list.add(new MenuEntry(grade_name.get(i), R.color.md_orange_300, "Open grade messages", Communications_Staff.class));
//        }
//
//        for(int i = 0 ; i < section_name.size(); i++) {
//            list.add(new MenuEntry(section_name.get(i), R.color.md_pink_A200, "Open section messages", Communications_Staff.class));
//        }
//
//        return list;
//    }
}
