package com.yodaapp.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yodaapp.live.R;
import com.yodaapp.live.model.ParentsDetailsModel;
import com.yodaapp.live.model.Section_Student_Model;
import com.yodaapp.live.model.Student_View_All_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class student_parents_adapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	List<ParentsDetailsModel> person;
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	public student_parents_adapter(Context context, List<ParentsDetailsModel> person) {
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
		convertView = layoutInflater.inflate(R.layout.student_parents_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();

		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.relation = (TextView) convertView.findViewById(R.id.relation);
		holder.contact = (TextView) convertView.findViewById(R.id.contact);
		holder.email = (TextView) convertView.findViewById(R.id.email);
		holder.profession = (TextView) convertView.findViewById(R.id.profession);




				holder.name.setText(person.get(position).getName());
				holder.relation.setText(person.get(position).getRole());
				holder.contact.setText(person.get(position).getContact());
				holder.email.setText(person.get(position).getEmail());






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
		TextView name,relation,contact,email,profession;
		RelativeLayout rel_layout;

	}

}
