package com.yodaapp.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yodaapp.live.Parent.Edit_Student_Details;
import com.yodaapp.live.Parent_Model.Home_View_Model;
import com.yodaapp.live.R;
import com.yodaapp.live.StaticVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class ParentsChildrenDetails extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private ArrayAdapter<String> adapter;
    List<Home_View_Model> person;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();
    public ParentsChildrenDetails(Context context, List<Home_View_Model> person) {
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
        convertView = layoutInflater.inflate(R.layout.parent_home1, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/

            holder = new ViewHolder();
            holder.allergic = (TextView) convertView.findViewById(R.id.tv_allergic);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.branch = (TextView) convertView.findViewById(R.id.tv_branch);
            holder.grade = (TextView) convertView.findViewById(R.id.tv_grade);
            holder.section = (TextView) convertView.findViewById(R.id.tv_section);
            holder.grade_teacher = (TextView) convertView.findViewById(R.id.tv_gteacher);
            holder.section_teacher = (TextView) convertView.findViewById(R.id.tv_steacher);
            holder.edit_user = (ImageView) convertView.findViewById(R.id.edit_user);


           holder.edit_user.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Activity activity = (Activity) context;
                   Intent intent=new Intent(context,Edit_Student_Details.class);
                   intent.putExtra("student_id",person.get(position).getStudent_id());
                   intent.putExtra("name",person.get(position).getStudent_name());
                   intent.putExtra("allergies",person.get(position).getAllergies());
                   intent.putExtra("school_id", StaticVariable.school_id);
                   intent.putExtra("user_id",StaticVariable.user_id);
                   activity.finish();
                   activity.startActivity(intent);
                   activity.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
               }
           });


            if (person.get(position).getStudent_name().equals("")||person.get(position).getStudent_name().equals(null))
            {
                holder.name.setText("");
            }else {
                holder.name.setText(person.get(position).getStudent_name());
            }
        if (person.get(position).getBranch_name().equals("")||person.get(position).getBranch_name().equals(null))
            {
                holder.branch.setText("");
            }else {
                holder.branch.setText(person.get(position).getBranch_name());
            }

        if (person.get(position).getGrade_name().equals("")||person.get(position).getGrade_name().equals(null))
            {
                holder.grade.setText("");
            }else {
                holder.grade.setText(person.get(position).getGrade_name());
            }

        if (person.get(position).getSection_name().equals("")||person.get(position).getSection_name().equals(null))
            {
                holder.section.setText("");
            }else {
                holder.section.setText(person.get(position).getSection_name());
            }


        if (person.get(position).getSection_master().equals("")||person.get(position).getSection_master().equals(null))
            {
                holder.section_teacher.setText("");
            }else {
                holder.section_teacher.setText(person.get(position).getSection_master());
            }
           if (person.get(position).getGrade_master().equals("")||person.get(position).getGrade_master().equals(null))
            {
                holder.grade_teacher.setText("");
            }else {
                holder.grade_teacher.setText(person.get(position).getGrade_master());
            }

        if (person.get(position).getAllergies().equals("")||person.get(position).getAllergies().equals(null))
            {
                holder.allergic.setText("");
            }else {
                holder.allergic.setText(person.get(position).getAllergies());
            }

        int no_line=holder.allergic.getLineCount();
        if (no_line>1){
            holder.allergic.setGravity(Gravity.LEFT);
        }
        else {
            holder.allergic.setGravity(Gravity.RIGHT);

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
        ImageView edit_user;
        TextView name, branch, grade, section, grade_teacher, section_teacher, allergic;

    }

}
