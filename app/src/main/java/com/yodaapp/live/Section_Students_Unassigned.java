package com.yodaapp.live;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.model.Section_Student_Assign_Model;
import com.yodaapp.live.parsers.CreateadminJSONParsers;
import com.yodaapp.live.parsers.Section_Student_Assign_JSPONParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Section_Students_Unassigned extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "", user_id = "", user_email = "";
    String section_id = "", grade_id = "", branch_id = "";
    String section_name = "", grade_name = "", branch_name = "";
    int min_role = 0;
    List<Section_Student_Assign_Model> feedslist;
    LinearLayout ll;
    ArrayList<String> myarray= new ArrayList<>();
    JSONArray json;
    List<Createadmin> feedslist2;
    TextView select_tv;
     Button b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_students_unassigned, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        Bundle args = getArguments();
        try {
            school_id = args.getString("school_id");
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            section_id = args.getString("section_id");
            branch_id = args.getString("branch_id");
            grade_id = args.getString("grade_id");
            section_name = args.getString("section_name");
            branch_name = args.getString("branch_name");
            grade_name = args.getString("grade_name");
            min_role = args.getInt("min_role");

        } catch (Exception e) {
            e.printStackTrace();
        }
        ll = (LinearLayout) rootView.findViewById(R.id.section_students_unassigned_layout);
        select_tv = (TextView) rootView.findViewById(R.id.select_tv);
        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/students_view_unassigned.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
         b = (Button) rootView.findViewById(R.id.section_student_unassigned_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myarray.clear();
                for(Section_Student_Assign_Model flower : feedslist)
                {
                    if(flower.getChecked().equals("true")) {
                        String checked_id = flower.getStudent_id();
                        Log.d("Student id",flower.getStudent_id());
                        Log.d("Student name",flower.getStudent_name());
                        Log.d("branch id",branch_id);
                        Log.d("grade id", grade_id);
                        Log.d("section id", section_id);
                        myarray.add(flower.getStudent_id());
                    }
                }
                if(!myarray.isEmpty()) {
                    if (isonline()) {
                        json = new JSONArray(myarray);
                        update_student(getResources().getString(R.string.url_reference) + "home/student_assign.php");
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return rootView;
    }


    void update_student(String uri) {
        progress.show();
        StringRequest request = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("student_response", "student_response"+response);
                        feedslist2 = CreateadminJSONParsers.parserFeed(response);
                        if (feedslist2 != null) {
                            for (final Createadmin flower : feedslist2) {
                                if (flower.getSucess().equals("success") || flower.getSucess().equals("existing")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    if(flower.getSucess().equals("success")) {
                                        builder.setMessage(getResources().getString(R.string.student_section_assign));
                                    } else {
                                        builder.setMessage(getResources().getString(R.string.student_section_assign_existing));
                                    }
                                            builder.setCancelable(false)
                                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Intent intent = new Intent(getActivity(), Grade_Section_view.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("school_id", school_id);
                                                    intent.putExtra("email", user_email);
                                                    intent.putExtra("branch_id", branch_id);
                                                    intent.putExtra("branch_name", branch_name);
                                                    intent.putExtra("grade_id", grade_id);
                                                    intent.putExtra("grade_name", grade_name);
                                                    intent.putExtra("section_id",section_id);
                                                    intent.putExtra("section_name",section_name);
                                                    intent.putExtra("min_role",min_role);
                                                    getActivity().finish();
                                                    startActivity(intent);
                                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
//                                else {
//                                    Toast.makeText(getActivity(), "Insert id - " + flower.getId(), Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.hide();
                        Toast.makeText(getActivity(),getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.unknownerror7))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        update_student(getResources().getString(R.string.url_reference) + "home/student_assign.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
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
                params.put("branch_id",branch_id);
                params.put("grade_id",grade_id);
                params.put("section_id",section_id);
                params.put("student_id",json.toString());

                Log.d("school_id", school_id);
                Log.d("user_id", user_id);
                Log.d("user_email", user_email);
                Log.d("branch_id", branch_id);
                Log.d("grade_id", grade_id);
                Log.d("section_id", section_id);
                Log.d("student_id", json.toString());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

    }

    void updatedisplay() {
        progress.show();
        if(feedslist != null) {
            for(final Section_Student_Assign_Model flower : feedslist)
            {
                if(flower.getGrade_id().equals("null") && flower.getSection_id().equals("null")) {
                    final CheckBox b = new CheckBox(getActivity());
                    b.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                    b.setId(Integer.valueOf(flower.getStudent_id()));
                    b.setText(flower.getStudent_name());
                    b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (b.isChecked()) {
                                flower.setChecked("true");
                            } else {
                                flower.setChecked("false");
                            }
                        }
                    });
                    ll.addView(b);
                }
            }
        }



        progress.hide();
    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Section_Student_Assign_JSPONParser.parserFeed(arg0);
                        updatedisplay();

                       if (feedslist.size()==0||feedslist.isEmpty()){
                            b.setVisibility(View.GONE);
                            select_tv.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"No students found",Toast.LENGTH_SHORT).show();
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
                        builder.setMessage(R.string.error_config)
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
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
    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
}
