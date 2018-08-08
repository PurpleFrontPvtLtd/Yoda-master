package com.yodaapp.live;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;

import java.util.HashMap;
import java.util.Map;


public class ChangePassword extends Fragment {

    EditText et,et2,et3;
    String password = "",newpassword = "",confirmpassword = "";
    Button b;
    ProgressDialog progress;
    CheckBox ch;
    private String TAG = ChangePassword.class.getSimpleName();
    private String tag_string_req = "string_req";
    String school_id = "" , school_name = "";
    String user_id = "";
    String user_email = "";
    View view;
    LinearLayout lincurpwd,linnewpw,linconpwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.changepassword, container, false);
        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getActionBar().setTitle("Change Password");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        try
        {
            Bundle args = getArguments();
            try {
                user_id = args.getString("user_id");
                user_email = args.getString("email");
                school_id = args.getString("school_id");

            } catch (Exception e) {
                e.printStackTrace();
            }
     /*       TextView tv = (TextView) view.findViewById(R.id.changepassword_username);
            tv.setText(user_email);
    */    }
        catch (Exception e)
        {
//            Log.d("Exception in database access","Exception : "+e);
            Toast.makeText(getActivity(), R.string.unknownerror4, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(),MainActivity.class);
            getActivity().finish();
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) view.findViewById(R.id.changepassword_password);
        et2 = (EditText) view.findViewById(R.id.changepassword_newpassword);
        et3 = (EditText) view.findViewById(R.id.changepassword_confirmpassword);

        lincurpwd = (LinearLayout)view.findViewById(R.id.lincurpwd);
        linnewpw = (LinearLayout)view.findViewById(R.id.linnewpw);
        linconpwd = (LinearLayout)view.findViewById(R.id.linconpwd);

        lincurpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.requestFocus();
            }
        });

        linnewpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et2.requestFocus();
            }
        });

        linconpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et3.requestFocus();
            }
        });



        ch = (CheckBox) view.findViewById(R.id.changepassword_showpassword);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    et.setTransformationMethod(null);
                    et2.setTransformationMethod(null);
                    et3.setTransformationMethod(null);
                    ch.setText("Show Password");
                }
                else
                {
                    et.setTransformationMethod(new PasswordTransformationMethod());
                    et2.setTransformationMethod(new PasswordTransformationMethod());
                    et3.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText("Show Password");
                }
            }
        });

        b = (Button) view.findViewById(R.id.changepassword_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = et.getText().toString();
                newpassword = et2.getText().toString();
                confirmpassword = et3.getText().toString();
                if(password.equals("") || password.isEmpty() || password.trim().isEmpty())
                {
                    et.setError(getResources().getString(R.string.correct_password));
                    Toast.makeText(getActivity(), R.string.correct_password, Toast.LENGTH_LONG).show();
                }
                else if(newpassword.equals("") || newpassword.isEmpty() || newpassword.trim().isEmpty())
                {
                    et2.setError(getResources().getString(R.string.password_new));
                    Toast.makeText(getActivity(), R.string.password_new, Toast.LENGTH_LONG).show();
                }
                else if(confirmpassword.equals("") || confirmpassword.isEmpty() || confirmpassword.trim().isEmpty())
                {
                    et3.setError(getResources().getString(R.string.correct_confirm_password));
                    Toast.makeText(getActivity(), R.string.correct_confirm_password, Toast.LENGTH_LONG).show();
                }
                else if(password.length() < 6)
                {
                    et.setError(getResources().getString(R.string.password_limit_correct));
                    Toast.makeText(getActivity(), R.string.password_limit_correct, Toast.LENGTH_LONG).show();
                }
                else if(newpassword.length() < 6)
                {
                    et2.setError(getResources().getString(R.string.password_new_limit));
                    Toast.makeText(getActivity(), R.string.password_new_limit, Toast.LENGTH_LONG).show();
                }
                else if(confirmpassword.length() < 6)
                {
                    et3.setError(getResources().getString(R.string.confirm_password_limit_correct));
                    Toast.makeText(getActivity(), R.string.confirm_password_limit_correct, Toast.LENGTH_LONG).show();
                }
                else if(!password.trim().equals(password))
                {
                    et.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(getActivity(), R.string.password_space, Toast.LENGTH_LONG).show();
                }
                else if(!newpassword.trim().equals(newpassword))
                {
                    et2.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(getActivity(), R.string.password_space, Toast.LENGTH_LONG).show();
                }
                else if(!confirmpassword.trim().equals(confirmpassword))
                {
                    et3.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(getActivity(), R.string.password_space, Toast.LENGTH_LONG).show();
                }
                else if(newpassword.equals(confirmpassword))
                {
                    if(newpassword.trim().equals(newpassword))
                    {
                        try
                        {
                            MCrypt mcrypt = new MCrypt();
                               // progress.show();
                                newpassword = MCrypt.bytesToHex( mcrypt.encrypt(newpassword) );
                                password = MCrypt.bytesToHex( mcrypt.encrypt(password) );
                                Toast.makeText(getActivity(), R.string.password_match, Toast.LENGTH_LONG).show();
                                if(isonline()) {
                                    StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference)+"home/password_change.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    Log.d("password response", s);
                                                    updatedislay(s);
                                                    progress.hide();
                                                }
                                            },

                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                                                    progress.hide();
                                                    Toast.makeText(getActivity(), R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                                          //          Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    getActivity().finish();
                                                    startActivity(intent);
                                                    getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                                                }
                                            }) {

                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("id", StaticVariable.user_id);
                                            params.put("email", StaticVariable.email);
                                            params.put("school_id",StaticVariable.school_id);
                                            params.put("password", password);
                                            params.put("newpassword", newpassword);
                                            Log.v("url","id="+user_id+"email="+user_email+"school_id="+school_id+"password="+password+"newpassword="+newpassword);
                                            return params;
                                        }

                                    };
                                    AppController.getInstance().addToRequestQueue(request, tag_string_req);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), R.string.password_correct2, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getActivity(), R.string.password_correct2, Toast.LENGTH_LONG).show();
        //                    Log.d("Exception while password decryption : ",""+e);
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(), R.string.password_space, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.password_confirm_password_not_match, Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public void updatedislay(String res)
    {
        switch (res) {
            case "password updated":
                dbhelp entry = new dbhelp(getActivity());
                entry.open();
                entry.updatepassword(user_id, newpassword);
                entry.close();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.changepassword_success))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress.show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("school_id", school_id);
                                intent.putExtra("email", user_email);
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case "Passwords does not match":
                Toast.makeText(getActivity(), R.string.password_online_local_different, Toast.LENGTH_LONG).show();
                break;
            case "Email not set":
                Toast.makeText(getActivity(), R.string.unknownerror, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getActivity(), R.string.unknownerror3, Toast.LENGTH_LONG).show();
                break;
        }
        }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

/*    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        view = inflater.inflate(R.layout.changepassword, viewGroup);
    }*/


}