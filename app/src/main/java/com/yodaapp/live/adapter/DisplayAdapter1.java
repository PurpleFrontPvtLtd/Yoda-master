package com.yodaapp.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yodaapp.live.MessageViewSent;
import com.yodaapp.live.MessageViewSentOneToOne;
import com.yodaapp.live.R;
import com.yodaapp.live.model.Group_Model;

import java.util.ArrayList;
import java.util.List;

public class DisplayAdapter1 extends BaseAdapter {
	List<Group_Model> database_existing;
	private Context mContext;
	private String from= " ";
	private String contact_id;
	List<String> contarr = new ArrayList<>();

	public DisplayAdapter1(Context c, List<Group_Model> database_existing) {

		this.mContext = c;
		this.database_existing = database_existing;
	}

	public DisplayAdapter1(Context c, List<Group_Model> database_existing, String from)
	{
		this.mContext = c;
		this.database_existing = database_existing;
		this.from = from;
	}


	public int getCount() {
		return database_existing.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int pos, View child, ViewGroup parent) {
		final Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.listcell1, null);
			mHolder = new Holder();


		//	contact_id = database_existing.get(pos).getId();

			contarr.add("false");

			Log.v("IDDETA","IDDEA"+contact_id);


//			if(!from.equals("crtegrp"))
//			{
//				mHolder.txt_name.setCheckMarkDrawable(null);
//			} else {
//				mHolder.txt_name.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (mHolder.txt_name.isChecked()) {
//							contarr.remove(pos);
//							contarr.add(pos,"false");
//							mHolder.txt_name.setChecked(false);
//							database_existing.get(pos).setStatus(false);
//
//
//						} else {
//							contarr.remove(pos);
//							contarr.add(pos,database_existing.get(pos).getId());
//							mHolder.txt_name.setChecked(true);
//							database_existing.get(pos).setStatus(true);
//							Log.v("SELID","SELID"+contarr);
//						}
//					}
//
//
//				});
//			}

			mHolder.txt_phoneNumber = (TextView) child.findViewById(R.id.txt_phoneNumber);
			mHolder.txt_checkbox = (CheckBox) child.findViewById(R.id.txt_checkbox);
			mHolder.txt_name = (TextView) child.findViewById(R.id.txt_name);
			mHolder.layout = (LinearLayout) child.findViewById(R.id.layout);





			mHolder.txt_name.setText(database_existing.get(pos).getName()+" - "+database_existing.get(pos).getProfile());
			mHolder.txt_phoneNumber.setText(database_existing.get(pos).getPhone());

			if (from.equals("9")){

				mHolder.txt_checkbox.setVisibility(View.INVISIBLE);


				mHolder.layout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Activity activity=(Activity)mContext;

						Intent intent=new Intent(activity, MessageViewSentOneToOne.class);
						intent.putExtra("contact_id",database_existing.get(pos).getId());
						intent.putExtra("contact_name",database_existing.get(pos).getName());

						activity.startActivity(intent);
					}
				});
			}


			mHolder.txt_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					database_existing.get(pos).setChecked(isChecked);
				}
			});


			child.setTag(mHolder);
		}else {
			mHolder = (Holder) child.getTag();
		}
	//	mHolder.txt_name.setText(database_existing.get(pos).getName());
	//	mHolder.txt_phoneNumber.setText(database_existing.get(pos).getPhone());
		return child;
	}

	public class Holder {
		TextView txt_phoneNumber,txt_name;
		//CheckedTextView txt_name;
		CheckBox txt_checkbox;
		LinearLayout layout;
	}

}
