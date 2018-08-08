package com.yodaapp.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yodaapp.live.Parent_Model.Event_Home_Model;
import com.yodaapp.live.R;
import com.yodaapp.live.model.Student_View_All_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class ViewAllStudentsAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	List<Student_View_All_Model> person;
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	public ViewAllStudentsAdapter(Context context, List<Student_View_All_Model> person) {
		this.context = context;
		this.person=person;
		this.layoutInflater = LayoutInflater.from(context);
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
		convertView = layoutInflater.inflate(R.layout.view_allstudents_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();

		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.time = (TextView) convertView.findViewById(R.id.section);

		if(person.get(position).getGrade_id().equals("null") && person.get(position).getSection_id().equals("null")) {

			holder.time.setText("UnAssigned");

		}
		else
		{
			holder.time.setText(person.get(position).getGrade_name()+"-"+person.get(position).getSection_name());

		}

		holder.name.setText(person.get(position).getStudent_name());


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
		TextView name,time;
		RelativeLayout rel_layout;

	}

}
