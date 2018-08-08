package com.yodaapp.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yodaapp.live.Parent_Model.Communication_Home_Model;
import com.yodaapp.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class HomePageList extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	ArrayList<String>person =new ArrayList<>();
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	ArrayList<String> teachersList = new ArrayList<>();
	public HomePageList(Context context,  ArrayList<String> person) {
		this.context = context;
		this.person=person;
		this.layoutInflater = LayoutInflater.from(context);
	}
	public HomePageList(Context context,  ArrayList<String> person,ArrayList<String> teachers) {
		this.context = context;
		this.person=person;
		this.teachersList=teachers;
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
		convertView = layoutInflater.inflate(R.layout.parents_comunication_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.name);


		if (teachersList.size()>0)
		{
			holder.name.setText(person.get(position)+"("+teachersList.get(position)+")");
		//	holder.name.setText(person.get(position)+"("+teachersList.get(position)+")");

		}
		else {
			holder.name.setText(person.get(position));
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
		TextView name;


	}

}
