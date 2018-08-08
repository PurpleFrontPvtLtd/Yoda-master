package com.yodaapp.live.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.yodaapp.live.R;
import com.yodaapp.live.StaticVariable;
import com.yodaapp.live.model.MessageGroupDetails_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 6/29/2016.
 */
public class GroupNameAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> gname = new ArrayList<>();
    private ArrayList<String> gid = new ArrayList<>();
    private ArrayList<String> unread = new ArrayList<>();
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    List<MessageGroupDetails_Model> persons;


    public GroupNameAdapter(Context c, ArrayList<String> gname, ArrayList<String> gid,ArrayList<String> unread) {
        this.mContext = c;
        this.gname = gname;
        this.gid = gid;
        this.unread = unread;

    }
    public GroupNameAdapter(Context c, List<MessageGroupDetails_Model> persons) {
        this.mContext = c;
        this.persons = persons;
        this.gid = gid;
        this.unread = unread;

    }
    @Override
    public int getCount() {

        //Log.v("GIDSIZE","GIDSIZE"+gid.size());
        //return gid.size();
        return gname.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listcell, null);
            mHolder = new Holder();
            mHolder.txt_name = (TextView) child.findViewById(R.id.txt_groupName);
            mHolder.txt_phoneNumber = (TextView) child.findViewById(R.id.txt_schoolName);
            mHolder.mThumbnailImage = (ImageView) child.findViewById(R.id.thumbnail_image);
            mHolder.active_image = (ImageView) child.findViewById(R.id.active_image);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }

        String title=gname.get(pos);
        mHolder.txt_name.setText(gname.get(pos));

        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();
        int color1 =R.color.md_green_50;
        // Create a circular icon consisting of  a random background colour and first letter of title
        mHolder.mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);


         mHolder.mDrawableBuilder1 = TextDrawable.builder()
                .buildRound(unread.get(pos), color1);

        mHolder.mThumbnailImage.setImageDrawable(mHolder.mDrawableBuilder);

        if (unread.get(pos).equals("0")||unread.get(pos).equals("null")){
            mHolder.active_image.setVisibility(View.INVISIBLE);
        }
        else {
            mHolder.active_image.setVisibility(View.VISIBLE);

        }
        mHolder.active_image.setImageDrawable(mHolder.mDrawableBuilder1);

        String level=gid.get(pos);
        if (level.equals("1")) {
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

            mHolder.txt_phoneNumber.setText("School level");
        }
        else if (level.equals("2")){
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

try {


    mHolder.txt_phoneNumber.setText("Branch level -" + StaticVariable.branch_list.get(pos));
}catch (Exception e){
    mHolder.txt_phoneNumber.setText("Branch level");

    e.printStackTrace();
}
        }
        else if (level.equals("3")){
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

            try {
                mHolder.txt_phoneNumber.setText("Grade level-"+StaticVariable.grade_list.get(pos));

            }catch (Exception e){
                mHolder.txt_phoneNumber.setText("Grade level");

                e.printStackTrace();
            }
        }
        else if (level.equals("4")){
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

            try {
                mHolder.txt_phoneNumber.setText("Section level-"+StaticVariable.section_list.get(pos));

            }catch (Exception e){
                mHolder.txt_phoneNumber.setText("Section level-");

                e.printStackTrace();
            }

        }
        else if (level.equals("8")){
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

            mHolder.txt_phoneNumber.setText("Adhoc level");

        }
        else if (level.equals("9")){
            mHolder.txt_phoneNumber.setText("One to One level");

        }
        else {
            mHolder.txt_phoneNumber.setVisibility(View.VISIBLE);

            mHolder.txt_phoneNumber.setText("School level");

        }
        return child;


    }



    public class Holder {
        TextView txt_name;
        TextView txt_phoneNumber;
        private ImageView mThumbnailImage,active_image;
        private TextDrawable mDrawableBuilder,mDrawableBuilder1;

    }

}
