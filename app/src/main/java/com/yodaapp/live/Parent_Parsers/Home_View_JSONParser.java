package com.yodaapp.live.Parent_Parsers;

import android.util.Log;

import com.yodaapp.live.Parent_Model.Home_View_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Home_View_JSONParser {
    public static List<Home_View_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Home_View_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Home_View_Model flower = new Home_View_Model();
                flower.setStudent_id(obj.getString("student_id"));
                flower.setStudent_name(obj.getString("user_name"));
                flower.setBranch_id(obj.getString("branch_id"));
                flower.setBranch_name(obj.getString("branch_name"));
                flower.setGrade_id(obj.getString("grade_id"));
                flower.setGrade_name(obj.getString("grade_name"));
                flower.setSection_id(obj.getString("section_id"));
                flower.setSection_name(obj.getString("section_name"));
                flower.setPrinciple_id(obj.getString("principle_id"));
                flower.setPrinciple(obj.getString("principle"));
                flower.setBranch_master_id(obj.getString("branch_master_id"));
                flower.setBranch_master(obj.getString("branch_master"));
                flower.setGrade_master_id(obj.getString("grade_master_id"));
                flower.setGrade_master(obj.getString("grade_master"));
                flower.setSection_master_id(obj.getString("section_master_id"));
                flower.setSection_master(obj.getString("section_master"));
                flower.setAllergies(obj.getString("allergies"));
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
