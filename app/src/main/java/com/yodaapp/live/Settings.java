package com.yodaapp.live;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.WindowManager;


public class Settings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            getActivity().getActionBar().setTitle("Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}