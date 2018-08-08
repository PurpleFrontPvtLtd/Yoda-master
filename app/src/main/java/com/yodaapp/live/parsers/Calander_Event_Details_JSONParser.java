package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Calander_Event_Details_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Calander_Event_Details_JSONParser {

    public static List<Calander_Event_Details_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Calander_Event_Details_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Calander_Event_Details_Model flower = new Calander_Event_Details_Model();
                flower.setId(obj.getString("id"));
                flower.setName(obj.getString("name"));
                flower.setEvent_notes(obj.getString("event_notes"));
                flower.setEvent_start(obj.getString("event_start"));
                flower.setEvent_end(obj.getString("event_end"));
                flower.setSchool_name(obj.getString("school_name"));
                flower.setBranch_name(obj.getString("branch_name"));
                flower.setGrade_name(obj.getString("grade_name"));
                flower.setSection_name(obj.getString("section_name"));
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
