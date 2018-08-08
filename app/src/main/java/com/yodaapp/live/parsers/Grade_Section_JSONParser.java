package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Grade_section_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Grade_Section_JSONParser {

    public static List<Grade_section_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Grade_section_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Grade_section_Model flower = new Grade_section_Model();
                flower.setSection_head_id(obj.getString("section_head_id"));
                flower.setSection_head_name(obj.getString("section_head_name"));
                flower.setBranch_name(obj.getString("branch_name"));
                flower.setGrade_name(obj.getString("grade_name"));
                flower.setSchool_name(obj.getString("school_name"));
                flower.setSubjects(obj.getString("subjects"));
                flower.setStudents(obj.getString("students"));
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
