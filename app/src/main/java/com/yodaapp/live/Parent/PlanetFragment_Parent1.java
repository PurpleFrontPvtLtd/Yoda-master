package com.yodaapp.live.Parent;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Existinguser;
import com.yodaapp.live.Parent_Model.Home_View_Model;
import com.yodaapp.live.Parent_Parsers.Home_View_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.adapter.ParentsChildrenDetails;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.dbhelp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class PlanetFragment_Parent1 extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    List<Home_View_Model> feedslist;
    LinearLayout ll;
    ListView listview;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planet_parents1, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listview=(ListView)rootView.findViewById(R.id.listView);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            school_id = args.getString("school_id");
            StaticVariable.school_id=school_id;
            StaticVariable.user_id=user_id;
            school_name = args.getString("school_name");
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   TextView tv = (TextView) rootView.findViewById(R.id.planet_parent_school_name);
        tv.setText(school_name);
*/
        ll = (LinearLayout) rootView.findViewById(R.id.fragment_planet_parent_layout);

try {


    if (StaticVariable.school_id.equals("") || StaticVariable.user_id.equals("")) {
        Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
    } else {
        if (isonline()) {
            progress.show();
            updatedata(getResources().getString(R.string.url_reference) + "parent_home/home_view.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

    }
}catch (Exception e){
    e.printStackTrace();
}
        return rootView;
    }

    void updatedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Home_View_JSONParser.parserFeed(arg0);


          try {
              if (feedslist.get(0).getSchool_status().contains("1")) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                  builder.setMessage(getResources().getString(R.string.error_school_inactive))
                          .setCancelable(false)

                          .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dbhelp entry = new dbhelp(getActivity());
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
                                  dbhelp entry = new dbhelp(getActivity());
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
              } else {

                  listview.setAdapter(new ParentsChildrenDetails(getActivity(), feedslist));
              }
          }catch (Exception e){
              e.printStackTrace();
          }


                       // updatedisplay();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(getActivity(), R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.error_config)
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "parent_home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), Existinguser.class);
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
            String prevoius_student = "";
            String student_id = "";
            for (final Home_View_Model flower : feedslist) {
                String student_name = flower.getStudent_name();

                student_id = flower.getStudent_id();

                // Setting student name
                if(!prevoius_student.equals(student_id)) {
                    TextView tv = new TextView(getActivity());
                    tv.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle_22);
                    tv.setText(student_name);
                    ll.addView(tv);
                }

                // Setting branch name and other details in row
                LinearLayout ll_hor = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor.setLayoutParams(p_hor);

                LinearLayout.LayoutParams layout_per = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                TextView tv_branch = new TextView(getActivity());
                tv_branch.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                tv_branch.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_branch.setLayoutParams(layout_per);
                tv_branch.setText("Branch : ");
                ll_hor.addView(tv_branch);

                TextView b_branch_click = new TextView(getActivity());
                b_branch_click.setLayoutParams(layout_per);
                b_branch_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getBranch_name().equals("null")) {
                    b_branch_click.setText(flower.getBranch_name());
                }
                b_branch_click.setPaintFlags(b_branch_click.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                b_branch_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Branch_Deatils.class);
                        intent.putExtra("id",flower.getBranch_id());
                        intent.putExtra("branch_name",flower.getBranch_name());
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("school_name",school_name);
                        getActivity().finish();
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                ll_hor.addView(b_branch_click);
                ll.addView(ll_hor);






                // Setting Grade name and other details in row
                LinearLayout ll_hor2 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor2.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor2.setLayoutParams(p_hor2);

                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(10, 10);
                p2.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_grade = new TextView(getActivity());
                tv_grade.setLayoutParams(layout_per2);
                tv_grade.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_grade.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_grade.setText("Grade : ");
                ll_hor2.addView(tv_grade);

                TextView b_grade_click = new TextView(getActivity());
                b_grade_click.setLayoutParams(layout_per2);
                b_grade_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getGrade_name().equals("null")) {
                    b_grade_click.setText(flower.getGrade_name());
                }
                ll_hor2.addView(b_grade_click);
                ll.addView(ll_hor2);


                // Setting Section name and other details in row
                LinearLayout ll_hor3 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor3.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor3.setLayoutParams(p_hor3);

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(10, 10);
                p3.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_section = new TextView(getActivity());
                tv_section.setLayoutParams(layout_per3);
                tv_section.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_section.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_section.setText("Section : ");
                ll_hor3.addView(tv_section);

                TextView b_section_click = new TextView(getActivity());
                b_section_click.setLayoutParams(layout_per3);
                b_section_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getSection_name().equals("null")) {
                    b_section_click.setText(flower.getSection_name());
                }
                ll_hor3.addView(b_section_click);
                ll.addView(ll_hor3);


                // Setting Section Teacher name and other details in row
                LinearLayout ll_hor4 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor4.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor4.setLayoutParams(p_hor4);

                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(10, 10);
                p4.setMargins(0, 25, 0, 0);
                LinearLayout.LayoutParams layout_per4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_section_teacher = new TextView(getActivity());
                tv_section_teacher.setLayoutParams(layout_per4);
                tv_section_teacher.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_section_teacher.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_section_teacher.setText("Section Teacher : ");
                ll_hor4.addView(tv_section_teacher);

                TextView b_section_teacher_click = new TextView(getActivity());
                b_section_teacher_click.setLayoutParams(layout_per4);
                b_section_teacher_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getSection_master().equals("null")) {
                    b_section_teacher_click.setText(flower.getSection_master());
                }
                ll_hor4.addView(b_section_teacher_click);
                ll.addView(ll_hor4);


                // Setting Grade Teacher name and other details in row
                LinearLayout ll_hor5 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor5.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor5.setLayoutParams(p_hor5);

                LinearLayout.LayoutParams p5 = new LinearLayout.LayoutParams(10, 10);
                p5.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_grade_teacher = new TextView(getActivity());
                tv_grade_teacher.setLayoutParams(layout_per5);
                tv_grade_teacher.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_grade_teacher.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_grade_teacher.setText("Grade Teacher : ");
                ll_hor5.addView(tv_grade_teacher);

                TextView b_grade_teacher_click = new TextView(getActivity());
                b_grade_teacher_click.setLayoutParams(layout_per5);
                b_grade_teacher_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getGrade_master().equals("null")) {
                    b_grade_teacher_click.setText(flower.getGrade_master());
                }
                ll_hor5.addView(b_grade_teacher_click);
                ll.addView(ll_hor5);


                // Setting Branch Head name and other details in row
                LinearLayout ll_hor6 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor6.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor6.setLayoutParams(p_hor6);

                LinearLayout.LayoutParams p6 = new LinearLayout.LayoutParams(10, 10);
                p6.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_branch_head = new TextView(getActivity());
                tv_branch_head.setLayoutParams(layout_per6);
                tv_branch_head.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_branch_head.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_branch_head.setText("Branch Head : ");
                ll_hor6.addView(tv_branch_head);

                TextView b_branch_head_click = new TextView(getActivity());
                b_branch_head_click.setLayoutParams(layout_per6);
                b_branch_head_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getBranch_master().equals("null")) {
                    b_branch_head_click.setText(flower.getBranch_master());
                }
                ll_hor6.addView(b_branch_head_click);
                ll.addView(ll_hor6);


                // Setting Principal name and other details in row
                LinearLayout ll_hor7 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor7.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor7.setLayoutParams(p_hor7);

                LinearLayout.LayoutParams p7 = new LinearLayout.LayoutParams(10, 10);
                p7.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_principal = new TextView(getActivity());
                tv_principal.setLayoutParams(layout_per7);
                tv_principal.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_principal.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_principal.setText("Principal : ");
                ll_hor7.addView(tv_principal);

                TextView b_principal_click = new TextView(getActivity());
                b_principal_click.setLayoutParams(layout_per7);
                b_principal_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(!flower.getPrinciple().equals("null")) {
                    b_principal_click.setText(flower.getPrinciple());
                }
                ll_hor7.addView(b_principal_click);
                ll.addView(ll_hor7);

                ImageView divider2 = new ImageView(getActivity());
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30);
                lp2.setMargins(0, 0, 0, 0);
                divider2.setLayoutParams(lp2);
                divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ll.addView(divider2);

                // Setting Allergic name and other details in row
                LinearLayout ll_hor12 = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p_hor12 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_hor12.setOrientation(LinearLayout.HORIZONTAL);
                ll_hor12.setLayoutParams(p_hor12);

                LinearLayout.LayoutParams p12 = new LinearLayout.LayoutParams(10, 10);
                p12.setMargins(0, 25, 0, 0);

                LinearLayout.LayoutParams layout_per12 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv_grade12 = new TextView(getActivity());
                tv_grade12.setLayoutParams(layout_per12);
                tv_grade12.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv_grade12.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv_grade12.setText("Allergies : ");
                ll_hor12.addView(tv_grade12);

                TextView b_allegrgic_click = new TextView(getActivity());
                b_allegrgic_click.setLayoutParams(layout_per12);
                b_allegrgic_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                if(flower.getAllergies().equals("")) {
                    b_allegrgic_click.setText("none");
                }
                else {
                    b_allegrgic_click.setText(flower.getAllergies());

                }
                ll_hor12.addView(b_allegrgic_click);
                ll.addView(ll_hor12);

                prevoius_student = student_id;
            }
        }
        else {
            Toast.makeText(getActivity(),getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        invalidateOptionsMenu(getActivity());
        inflater.inflate(R.menu.viewedit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.edit:
                if(isonline())
                {
                    Intent intent=new Intent(getActivity(),Edit_Student_Details.class);
                    intent.putExtra("student_id",feedslist.get(0).getStudent_id());
                    intent.putExtra("name",feedslist.get(0).getStudent_name());
                    intent.putExtra("allergies",feedslist.get(0).getAllergies());
                    intent.putExtra("school_id",school_id);
                    intent.putExtra("user_id",user_id);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return true;
        }
    }*/




}
