package com.yodaapp.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yodaapp.live.Parent_Model.Communication_Home_Model;
import com.yodaapp.live.Parent_Model.Home_View_Model;
import com.yodaapp.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class ParentsCommunicationAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	List<Communication_Home_Model> person;
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	public ParentsCommunicationAdapter(Context context, List<Communication_Home_Model> person) {
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
		convertView = layoutInflater.inflate(R.layout.parents_comunication_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.rel_layout = (RelativeLayout) convertView.findViewById(R.id.rel_layout);

    holder.name.setText(person.get(position).getName());

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
		RelativeLayout rel_layout;

	}

}
