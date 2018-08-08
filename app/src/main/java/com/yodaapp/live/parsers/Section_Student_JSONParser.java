package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Section_Student_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Section_Student_JSONParser {

    public static List<Section_Student_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Section_Student_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Section_Student_Model flower = new Section_Student_Model();
                flower.setBranch_id(obj.getString("branch_id"));
                flower.setBranch_name(obj.getString("branch_name"));
                flower.setGrade_id(obj.getString("grade_id"));
                flower.setGrade_name(obj.getString("grade_name"));
                flower.setSection_id(obj.getString("section_id"));
                flower.setSection_name(obj.getString("section_name"));
                flower.setYear(obj.getString("year"));
                flower.setAllergies(obj.getString("allergies"));
                flower.setImage(obj.getString("image"));
                flower.setParents(obj.getString("parents"));
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
