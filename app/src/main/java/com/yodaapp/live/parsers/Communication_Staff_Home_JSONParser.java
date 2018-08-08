package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Communication_Staff_Home_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Communication_Staff_Home_JSONParser {
    public static List<Communication_Staff_Home_Model> parserFeed(String content) {
        try {
            List<Communication_Staff_Home_Model> feedslist = new ArrayList<>();
            Communication_Staff_Home_Model flower = new Communication_Staff_Home_Model();
            JSONObject parentObject = new JSONObject(content);

            String role = parentObject.getString("role");
            flower.setRole(role);
            String branch = parentObject.getString("branch");
            flower.setBranch(branch);
            Log.d("branch",branch);

            String grade = parentObject.getString("grade");
            flower.setGrade(grade);
            Log.d("grade", grade);

            String section = parentObject.getString("section");
            flower.setSection(section);
            Log.d("section",section);

            feedslist.add(flower);

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
