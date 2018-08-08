package com.yodaapp.live.Parent;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.R;
import com.yodaapp.live.SplashScreen;
import com.yodaapp.live.adapter.ParentCalendarAdapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.Parent_Model.Event_Home_Model;
import com.yodaapp.live.Parent_Parsers.Event_Home_JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Calander_Home_Parents extends Fragment {

    View rootView;
    ProgressDialog progress;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_name = "";
    List<Event_Home_Model> feedslist;
    ImageView imageAddButton;
    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calender_home1, container, false);
        setHasOptionsMenu(true);

        imageAddButton = (ImageView) rootView.findViewById(R.id.calander_event_create);
        listview = (ListView) rootView.findViewById(R.id.listview);
        imageAddButton.setVisibility(View.GONE);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_name = args.getString("email");
            school_id = args.getString("school_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        if (school_id.equals("") || user_id.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "parent_home/event_home.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        }

        return rootView;
    }

    protected void updatedisplay()
    {
        progress.show();
        if (feedslist != null) {
            try {
                String previos_date = "";
                for (final Event_Home_Model flower : feedslist) {
                    String start_date = flower.getEvent_start();
                    String event_start_date = flower.getEvent_start();
                    LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.calander_layout_adder);
                    SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        Date date = srcDf.parse(start_date);
                        SimpleDateFormat destDf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
                        start_date = destDf.format(date);

                        destDf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                        event_start_date = destDf.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(!previos_date.equals(start_date)) {
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
                            intent.putExtra("event_id",flower.getId());
                            getActivity().finish();
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    });
                    ll.addView(tv);

                    TextView tv2 = new TextView(getActivity());
                    tv2.setText("Event Start " + event_start_date);
                    tv2.setPadding(15,0,0,0);
                    tv2.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle);
                    ll.addView(tv2);


                    ImageView divider2 = new ImageView(getActivity());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                    lp2.setMargins(0, 0, 0, 0);
                    divider2.setLayoutParams(lp2);
                    divider2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ll.addView(divider2);

                    previos_date = start_date;
                }
            } catch (Exception e) {
//                e.printStackTrace();
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
//                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");
                        feedslist = Event_Home_JSONParser.parserFeed(arg0);
                        listview.setAdapter(new ParentCalendarAdapter(getActivity(),feedslist));

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Calender_Event_Details.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("username", user_name);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("event_id",feedslist.get(position).getId());
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                    //    updatedisplay();
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

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", user_name);

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
