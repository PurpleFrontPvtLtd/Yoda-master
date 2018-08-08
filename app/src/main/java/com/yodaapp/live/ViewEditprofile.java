package com.yodaapp.live;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yodaapp.live.model.Databaseaccess;

import java.util.List;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class ViewEditprofile extends Fragment {

	String name = "",email = "", phone = "";
	String id = "",profile_id = "", school_name = "";
	TextView nametv,emailtv,phonetv;

    View view;
    List<Databaseaccess> database;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.view_profile1, container, false);
        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getActionBar().setTitle("Profile");
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		nametv = (TextView) view.findViewById(R.id.viewedit_name);
		emailtv = (TextView) view.findViewById(R.id.viewedit_email);
		phonetv = (TextView) view.findViewById(R.id.viewedit_phone);

		try
		{
			dbhelp.DatabaseHelper2 dbhep = new dbhelp.DatabaseHelper2(getActivity());
			database = dbhep.getTodo();

            if (database != null)
            {
                for (final Databaseaccess flower : database)
                {
                    id = flower.getId();
                    name = flower.getName();
                    email = flower.getEmail_real();
                    phone = flower.getPhone();
					profile_id = flower.getUser_type();

                }

            }
		}
		catch(Exception e)
		{
			Log.d("Exception : ", "" + e);
			Log.d("exception", "user does not exist");
		}

		if(profile_id.equals("3")) {
			Bundle args = getArguments();
			try {
				school_name = args.getString("school_name");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		nametv.setText(name);
		emailtv.setText(email);
		try {


			if (phone.equals("")) {
				phonetv.setText(StaticVariable.contact);
			}
			else
			{
				phonetv.setText(phone);
				StaticVariable.contact=phone;
			}
		}
		
		catch (Exception e){
			e.printStackTrace();
			phonetv.setText(StaticVariable.contact);

		}
        return view;

	}

	protected boolean isonline()
	{
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		return netinfo != null && netinfo.isConnectedOrConnecting();
	}


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		invalidateOptionsMenu(getActivity());
        inflater.inflate(R.menu.viewedit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle("Profile");
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.edit:
			if(isonline())
			{

                Intent intent=new Intent(getActivity(),Editprofile.class);
				intent.putExtra("school_name",school_name);
				intent.putExtra("phone",phonetv.getText().toString());
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
	}

}