package com.yodaapp.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yodaapp.live.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class SchoolCalendarAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	List<com.yodaapp.live.model.Event_Home_Model> person;
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	public SchoolCalendarAdapter(Context context, List<com.yodaapp.live.model.Event_Home_Model> person) {
		this.context = context;
		this.person=person;
		this.layoutInflater = LayoutInflater.from(context);
		if (person.get(0).getName().equals("")){
			person.clear();
		}
	}
	@Override
	public int getCount() {
		return person.size();
	}

	@Override
	public String getItem(int position) {
		return "";
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// if (convertView == null) {
		final ViewHolder holder;
		convertView = layoutInflater.inflate(R.layout.parents_calendar_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.time = (TextView) convertView.findViewById(R.id.textView54);
		//holder.end_time = (TextView) convertView.findViewById(R.id.end_time);

		SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String start_date="",end_dates="";
		try {
			Date date = srcDf.parse(person.get(position).getEvent_start());
			Date end_date = srcDf.parse(person.get(position).getEvent_end());
			SimpleDateFormat destDf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
			start_date = destDf.format(date);
			end_dates = destDf.format(end_date);

			destDf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
			start_date = destDf.format(date);
			end_dates = destDf.format(end_date);

		} catch (ParseException e) {
			holder.name.setVisibility(View.GONE);
			holder.time.setVisibility(View.GONE);
			e.printStackTrace();
		}

		if (holder.name.equals("")){
			holder.name.setVisibility(View.GONE);
			holder.time.setVisibility(View.GONE);
		}
		else {
			holder.name.setText(person.get(position).getName());
			holder.time.setText(start_date+ "-" +end_dates);
			//holder.end_time.setText("end at "+end_dates);
		}


		return convertView;
	}

	private void initializeViews(String object, ViewHolder holder) {
		//TODO implement
	}

	/**
	 * This class contains all butterknife-injected Views & Layouts from layout file 'inflate_assignmentlist.xml'
	 * for easy to all layout elements.
	 *
	 * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
	 */

	private class ViewHolder {
		TextView name,time,end_time;

	}

}
