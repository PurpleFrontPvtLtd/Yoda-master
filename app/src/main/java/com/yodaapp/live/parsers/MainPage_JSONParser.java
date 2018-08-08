package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.MainPage_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPage_JSONParser {

    public static List<MainPage_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<MainPage_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                MainPage_Model flower = new MainPage_Model();
                flower.setUser_name(obj.getString("user_name"));
                flower.setUser_email(obj.getString("user_email"));
                flower.setUser_contact(obj.getString("user_contact"));
                flower.setSchool_name(obj.getString("school_name"));
                flower.setProfile_type(obj.getString("profile_type"));
                flower.setMin_role(obj.getString("min_role"));
                flower.setRoles(obj.getString("roles"));
                flower.setBranches(obj.getString("branches"));
                flower.setStaff(obj.getString("staff"));
                flower.setSubject(obj.getString("subject"));
                flower.setOwners(obj.getString("owners"));
                flower.setSchool_status(obj.getString("school_status"));
                flower.setProfile_status(obj.getString("profile_status"));
                feedslist.add(flower);
            }
            return feedslist;
        } catch (JSONException e) {

            Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            Log.d("json connection", "No internet access" + e);
            return null;
        }
    }
}
