package com.yodaapp.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yodaapp.live.MessageViewSentOneToOne;
import com.yodaapp.live.R;
import com.yodaapp.live.model.Group_Model;
import com.yodaapp.live.model.Group_Model1;

import java.util.ArrayList;
import java.util.List;

public class DisplayAdapter2 extends BaseAdapter implements Filterable {
	List<Group_Model1> database_existing;
	List<Group_Model1> mStringFilterList;
	//List<Group_Model> countrylist;
	private Context mContext;
	private String from= " ";
	private String contact_id;
	ValueFilter valueFilter;
	List<String> contarr = new ArrayList<>();

	public DisplayAdapter2(Context c, List<Group_Model1>database_existing) {

		this.mContext = c;
		this.database_existing = database_existing;
		mStringFilterList = database_existing;
	}

	public DisplayAdapter2(Context c, List<Group_Model1> database_existing, String from)
	{
		this.mContext = c;
		this.database_existing = database_existing;
		mStringFilterList = database_existing;
		this.from = from;
	}


	public int getCount() {
		return database_existing.size();
	}

	public Object getItem(int position) {
		return database_existing.get(position);
	}

	public long getItemId(int position) {
		return database_existing.indexOf(getItem(position));
	}

	public View getView(final int pos, View child, ViewGroup parent) {
		final Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.listcell1, null);
			mHolder = new Holder();


			final Group_Model1 group_model = database_existing.get(pos);


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

			//mHolder.txt_name.setText(database_existing.get(pos).getName());

			mHolder.txt_name.setText(group_model.getName());


			if (database_existing.get(pos).getChecked()){
				mHolder.txt_checkbox.setChecked(true);
			}


			//mHolder.txt_phoneNumber.setText(database_existing.get(pos).getPhone());

			mHolder.txt_phoneNumber.setText(group_model.getPhone());

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
						activity.finish();
					}
				});
			}


			mHolder.txt_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					InputMethodManager imm = (InputMethodManager) buttonView.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(buttonView.getWindowToken(), 0);
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

	@Override
	public Filter getFilter()
	{
		if (valueFilter == null)
		{
			valueFilter = new ValueFilter();
		}

		return valueFilter;
	}

	private class ValueFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			FilterResults results = new FilterResults();

			if (constraint != null && constraint.length() > 0)
			{
				List<Group_Model1> filterList = new ArrayList<>();

				for (int i = 0; i < mStringFilterList.size(); i++)
				{

					if ( (mStringFilterList.get(i).getName().toUpperCase() )
							.contains(constraint.toString().toUpperCase()))
					{
						Group_Model1 group_model = new Group_Model1(mStringFilterList.get(i).getId(),
								mStringFilterList.get(i).getName(),
								mStringFilterList.get(i).getPhone(),
								mStringFilterList.get(i).getStatus(),
								mStringFilterList.get(i).getChecked());

						filterList.add(group_model);

					}


				}

				results.count = filterList.size();
				results.values = filterList;
			}
			else {
				results.count = mStringFilterList.size();
				results.values = mStringFilterList;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			database_existing = (List<Group_Model1>) results.values;
			notifyDataSetChanged();

		}
	}

	public class Holder {
		TextView txt_phoneNumber,txt_name;
		CheckBox txt_checkbox;
		LinearLayout layout;

	}

}
