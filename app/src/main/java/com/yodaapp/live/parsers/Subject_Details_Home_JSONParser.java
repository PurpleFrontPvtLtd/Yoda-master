package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Subject_Details_Home_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Subject_Details_Home_JSONParser {

    public static List<Subject_Details_Home_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Subject_Details_Home_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Subject_Details_Home_Model flower = new Subject_Details_Home_Model();
                flower.setSubject(obj.getString("subject"));
                flower.setSection(obj.getString("section"));
                flower.setGrade(obj.getString("grade"));
                flower.setBranch(obj.getString("branch_name"));
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
