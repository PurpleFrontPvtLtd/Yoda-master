package com.yodaapp.live.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Existinguser;
import com.yodaapp.live.MainActivity;
import com.yodaapp.live.R;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.ParentDetail_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class TeacherDetailsAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private int count;
	private ArrayAdapter<String> adapter;
	List<ParentDetail_Model> person;
	ArrayList<String> editQuantityList = new ArrayList<>();
	ArrayList<String> editQuantityList1 = new ArrayList<>();
	Integer pos=0;
	ProgressDialog progress;


	public TeacherDetailsAdapter(Context context, List<ParentDetail_Model> person) {
		this.context = context;
		this.person=person;
		this.layoutInflater = LayoutInflater.from(context);


		progress = new ProgressDialog(context);
		progress.setCancelable(false);
		progress.setMessage(context.getResources().getString(R.string.loading));
		progress.setTitle(R.string.please_wait);
		progress.getWindow().setGravity(Gravity.CENTER);
		progress.setIndeterminate(true);
		progress.hide();

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
		convertView = layoutInflater.inflate(R.layout.teacher_details_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

		holder = new ViewHolder();

		holder.branch = (TextView) convertView.findViewById(R.id.branch);
		holder.grade = (TextView) convertView.findViewById(R.id.grade);
		holder.section = (TextView) convertView.findViewById(R.id.section);
		holder.role = (TextView) convertView.findViewById(R.id.role);
		holder.subject = (TextView) convertView.findViewById(R.id.subject);
		holder.delete_role = (ImageView) convertView.findViewById(R.id.delete_role);

		holder.brachlay = (LinearLayout)convertView.findViewById(R.id.branchlay);
		holder.gradelay = (LinearLayout)convertView.findViewById(R.id.gradelay);
		holder.sectionlay = (LinearLayout)convertView.findViewById(R.id.sectlay);
		holder.subjectlay = (LinearLayout)convertView.findViewById(R.id.sublay);

		pos=position;

		holder.delete_role.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity activity=(Activity)context;
			//	Toast.makeText(activity,person.get(position).getId(),Toast.LENGTH_SHORT).show();


					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(activity.getResources().getString(R.string.confirm_inactivate_staff))
							.setCancelable(false)
							.setNegativeButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									removeStaffRole();
								}
							})
							.setPositiveButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {


								}
							});
					AlertDialog alert = builder.create();
					alert.show();


			}
		});


		 if (!person.get(position).getBranch().equals("null")){
			 holder.branch.setText(person.get(position).getBranch());

		 } if (!person.get(position).getGrade().equals("null")){
			holder.grade.setText(person.get(position).getGrade());

		 }
		if (!person.get(position).getSection().equals("null")){
			holder.section.setText(person.get(position).getSection());

		 }if (!person.get(position).getRole().equals("null")){
			holder.role.setText(person.get(position).getRole());

			Log.d("Rol","Rol"+person.get(position).getRole());

			if(person.get(position).getRole().contains("Owner") || person.get(position).getRole().contains("Principal") || person.get(position).getRole().contains("Administrator"))
			{

				Log.d("INSIDE","INSIDE");
				holder.brachlay.setVisibility(View.GONE);
				holder.gradelay.setVisibility(View.GONE);
				holder.sectionlay.setVisibility(View.GONE);
				//holder.role.setVisibility(View.GONE);
				holder.subjectlay.setVisibility(View.GONE);
			}
			else if(person.get(position).getRole().contains("Branch Head"))
			{
				holder.gradelay.setVisibility(View.GONE);
				holder.sectionlay.setVisibility(View.GONE);
				holder.subjectlay.setVisibility(View.GONE);
			}
			else if(person.get(position).getRole().contains("Grade Head"))
			{
				holder.sectionlay.setVisibility(View.GONE);
				holder.subjectlay.setVisibility(View.GONE);
			}
			else if(person.get(position).getRole().contains("Section Head"))
			{
				holder.subjectlay.setVisibility(View.GONE);
			}

		 }

		if (!person.get(position).getSubject().equals("null")){
			holder.subject.setText(person.get(position).getSubject());

		 }

		return convertView;
	}

	void removeStaffRole() {
		progress.show();

		StringRequest request = new StringRequest(Request.Method.POST,  context.getResources().getString(R.string.url_reference) + "home/remove_staff_role.php",

				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						Log.d("response", arg0);
                       Log.d("here in sucess", "sucess");
						progress.hide();

						try {
							JSONObject obj=new JSONObject(arg0);

							String status=obj.getString("sucess");

							if (status.equals("Profile updated")){
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setMessage(context.getResources().getString(R.string.student_inactivate_update))
										.setCancelable(false)
										.setNeutralButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
											}
										})
										.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {

												Activity activity=(Activity)context;

												Intent intent = new Intent(activity, MainActivity.class);

												activity.startActivity(intent);
												activity.finish();
												activity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

											}
										});
								AlertDialog alert = builder.create();
								alert.show();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				},


				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						progress.hide();

						Toast.makeText(context, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_View_All.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());

					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();

				params.put("school_id", StaticVariable.school_id);
				params.put("user_id", StaticVariable.user_id);
				params.put("staff_role_row_id", person.get(pos).getId());

				return params;
			}

		};
		AppController.getInstance().addToRequestQueue(request, "tag");
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
		TextView branch,grade,section,subject,role;
		LinearLayout brachlay,gradelay,sectionlay,subjectlay;
		RelativeLayout rel_layout;
        ImageView delete_role;
	}

}
