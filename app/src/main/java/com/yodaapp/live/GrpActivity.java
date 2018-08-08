package com.yodaapp.live;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;;

/**
 * Created by Home on 8/22/2016.
 */
public class GrpActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Fragment test;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        test = new GroupNameFragment();
        Bundle args = new Bundle();
        //args.putString("user_id", user_id);
        //args.putString("email", user_email);
       // args.putString("school_id", school_id);
        test.setArguments(args);

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        fragmentTransaction.replace(R.id.content_frame, test);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }
}
