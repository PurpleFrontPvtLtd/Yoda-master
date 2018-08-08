package com.yodaapp.live.Parent;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent_Model.Communication_Home_Model;
import com.yodaapp.live.Parent_Parsers.Communication_home_JSONParser;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.adapter.ParentsCommunicationAdapter;
import com.yodaapp.live.controller.AppController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Communications_Parents extends Fragment {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    List<Communication_Home_Model> feedslist;
    LinearLayout ll;
    LinearLayout ll_n;
    ListView listview1,listview2;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_plannet_parents_communication, container, false);

        listview1=(ListView)rootView.findViewById(R.id.listview);
        listview2=(ListView)rootView.findViewById(R.id.listview1);
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
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            school_id = args.getString("school_id");
            school_name = args.getString("school_name");
        } catch (Exception e) {
            e.printStackTrace();
        }



      //  ll = (LinearLayout) rootView.findViewById(R.id.fragment_planet_parent_communication_layout);
       // ll_n = (LinearLayout) rootView.findViewById(R.id.parent_communication_individual_message_layout);
        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "parent_home/communication_home.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }

        return rootView;
    }


    void updatedata(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Communication_home_JSONParser.parserFeed(arg0);

                        Log.d("hereinsucess", feedslist.size()+"");

                        listview1.setAdapter(new ParentsCommunicationAdapter(getActivity(),feedslist));
                        listview2.setAdapter(new ParentsCommunicationAdapter(getActivity(),feedslist));

                        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Communication_View.class);
                                intent.putExtra("id", feedslist.get(position).getId());
                                intent.putExtra("name",  feedslist.get(position).getName());
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("school_name", school_name);
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });

                        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Communication_Parents_Staff_View.class);
                                intent.putExtra("id", feedslist.get(0).getId());
                                intent.putExtra("name",  feedslist.get(0).getName());
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("email", user_email);
                                intent.putExtra("school_name", school_name);
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        });



                      //  updatedisplay();
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
            for (final Communication_Home_Model flower : feedslist) {
                String student_name = flower.getName();

                // Setting Student Name and other details in row for school messages
                TextView b_branch_click = new TextView(getActivity());
                Drawable left = getResources().getDrawable(R.drawable.black_dot);
                b_branch_click.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                b_branch_click.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                b_branch_click.setText(flower.getName());
                if(flower.getUnread().equals("")) {
                    b_branch_click.setText(flower.getName());
                }
                if(!flower.getUnread() .equals("0")) {
                    b_branch_click.setText(flower.getName() + " ( " + flower.getUnread() + " ) ");
                    b_branch_click.setTypeface(Typeface.DEFAULT_BOLD);
                }
                else {
                    b_branch_click.setText(flower.getName());
                }
                b_branch_click.setPaintFlags(b_branch_click.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                b_branch_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Communication_View.class);
                        intent.putExtra("id", flower.getId());
                        intent.putExtra("name", flower.getName());
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("school_name", school_name);
                        getActivity().finish();
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                ll.addView(b_branch_click);

                // Setting Student Name and other details in row for individual message
                TextView tv2 = new TextView(getActivity());
                tv2.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                tv2.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                tv2.setText(flower.getName());
                tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Communication_Parents_Staff_View.class);
                        intent.putExtra("id", flower.getId());
                        intent.putExtra("name", flower.getName());
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("school_name", school_name);
                        getActivity().finish();
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                ll_n.addView(tv2);

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
}
