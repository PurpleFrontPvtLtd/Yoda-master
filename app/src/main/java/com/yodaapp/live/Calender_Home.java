package com.yodaapp.live;

import android.app.ActionBar;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.adapter.ParentCalendarAdapter;
import com.yodaapp.live.adapter.SchoolCalendarAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Event_Home_Model;
import com.yodaapp.live.parsers.Event_Home_JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;


public class Calender_Home extends Fragment {

    View rootView;
    ProgressDialog progress;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_name = "";
    String branch_id = "";
    String branch_name = "";
    TextView tv;
    String school_name;
    String role_id = "";
    List<Event_Home_Model> feedslist;
    ImageView imageAddButton;
    ListView listview;
    ImageButton ig;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calender_home1, container, false);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_name = args.getString("email");
            school_id = args.getString("school_id");
        } catch (Exception e) {
//            e.printStackTrace();
            user_id = StaticVariable.user_id;
            user_name = StaticVariable.email;
            school_id = StaticVariable.school_id;
        }

        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();
        listview = (ListView) rootView.findViewById(R.id.listview);

        if (StaticVariable.school_id.equals("") || StaticVariable.user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/event_home.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }
        ig = (ImageButton) rootView.findViewById(R.id.calander_event_create);

        int cont = Integer.valueOf(StaticVariable.role_id);
               if (cont < 4) {
            ig.setVisibility(View.VISIBLE);
             } else {
            ig.setVisibility(View.GONE);
            //  Toast.makeText(getActivity(), getResources().getString(R.string.calander_validation), Toast.LENGTH_LONG).show();
        }


        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Calander_Event_Add.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("username", user_name);
                intent.putExtra("school_id", school_id);
                intent.putExtra("role_id", StaticVariable.role_id);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


            }
        });

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.


        // Specify that we will be displaying tabs in the action bar.

        return rootView;
    }

    protected void updatedisplay() {
        progress.show();
        if (feedslist != null) {
            try {
                String previos_date = "";
                for (final Event_Home_Model flower : feedslist) {
                    role_id = flower.getRole_id();
                    Log.d("role_id", role_id);
                    if (flower.getName().equals("") && flower.getDetails().equals("") && flower.getEvent_end().equals("") && flower.getEvent_start().equals("") && flower.getId().equals("")) {

                    } else {
                        String start_date = flower.getEvent_start();
                /*        String event_start_date = flower.getEvent_start();
                        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.calander_layout_adder);
                        SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = srcDf.parse(start_date);
                            SimpleDateFormat destDf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
                            start_date = destDf.format(date);

                            destDf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                            event_start_date = destDf.format(date);

                        } catch (ParseException e) {
//                        e.printStackTrace();
                        }

                        if (!previos_date.equals(start_date)) {
                            TextView tv2 = new TextView(getActivity());
                            tv2.setText(start_date);
                            tv2.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle_Mediunbig);
                            ll.addView(tv2);
                        }


                        TextView tv = new TextView(getActivity());
                        Drawable left = getResources().getDrawable(R.drawable.black_dot);
                        tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                        tv.setText(flower.getName());
                        tv.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), Calender_Event_Details.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("username", user_name);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("role_id", role_id);
                                intent.putExtra("event_id", flower.getId());
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                        ll.addView(tv);

                        TextView tv2 = new TextView(getActivity());
                        tv2.setText("Event Start " + event_start_date);
                        tv2.setPadding(15, 0, 0, 0);
                        tv2.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                        ll.addView(tv2);


                        ImageView divider2 = new ImageView(getActivity());
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                        lp2.setMargins(0, 0, 0, 0);
                        divider2.setLayoutParams(lp2);
                        divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ll.addView(divider2);*/

                        previos_date = start_date;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        progress.hide();
    }

    void updatedata(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Event_Home_JSONParser.parserFeed(arg0);



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
                            listview.setAdapter(new SchoolCalendarAdapter(getActivity(), feedslist));
                        }
                        try {
                            role_id = feedslist.get(0).getRole_id();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Calender_Event_Details.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("username", user_name);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("role_id", feedslist.get(position).getRole_id());
                                intent.putExtra("event_id", feedslist.get(position).getId());
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                 //      updatedisplay();
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

                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("user_email", StaticVariable.email);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     /*   invalidateOptionsMenu(getActivity());
        inflater.inflate(R.menu.calander_home_menu, menu);
*/        super.onCreateOptionsMenu(menu, inflater);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calander_event_add:
                int cont = Integer.valueOf(role_id);
                if (cont < 4) {
                    Intent intent = new Intent(getActivity(), Calander_Event_Add.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("username", user_name);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("role_id", StaticVariable.role_id);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_error_calender), Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return true;
        }
    }*/
}
