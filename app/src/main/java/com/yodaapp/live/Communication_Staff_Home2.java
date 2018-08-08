package com.yodaapp.live;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;


public class Communication_Staff_Home2 extends Fragment {

    View rootView;


    String school_id = "", user_id = "", user_email = "";

//    public static String VIEW_COLOR= "color";
//
//    private CollectionView mCollectionView;
//
//    public interface Callbacks {
//        public void onTopicSelected(MenuEntry menuEntry, View clickedView);
//    }
//
//    private static Callbacks sDummyCallbacks = new Callbacks() {
//
//        @Override
//        public void onTopicSelected(MenuEntry menuEntry, View clickedView) {
//
//        }
//    };

//    private Callbacks mCallbacks = sDummyCallbacks;
ImageButton group_create;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.communication_staff_home2, container, false);
        group_create=(ImageButton) rootView.findViewById(R.id.group_create);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Bundle args = getArguments();
        try {
            user_id = args.getString("user_id");
            user_email = args.getString("email");
            school_id = args.getString("school_id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        group_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MembersSelectActivity.class);

                intent.putExtra("user_id",user_id);
                intent.putExtra("school_id", school_id);
                intent.putExtra("email", user_email);

                startActivity(intent);
            }
        });

        return rootView;
    }







//    private static List<MenuEntry> buildMenuList(){
//        ArrayList<MenuEntry> list = new ArrayList<MenuEntry>();
//
//        list.add(new MenuEntry(branch_name.get(0),R.color.md_red_800,"Open school or branch message", Communications_Staff.class));
//
//        for(int i = 1;i<branch_name.size();i++) {
//            list.add(new MenuEntry(branch_name.get(i), R.color.md_green_300, "Open school or branch message", Communications_Staff.class));
//        }
//
//        for (int i = 0;i<grade_name.size();i++) {
//            list.add(new MenuEntry(grade_name.get(i), R.color.md_orange_300, "Open grade messages", Communications_Staff.class));
//        }
//
//        for(int i = 0 ; i < section_name.size(); i++) {
//            list.add(new MenuEntry(section_name.get(i), R.color.md_pink_A200, "Open section messages", Communications_Staff.class));
//        }
//
//        return list;
//    }
}
