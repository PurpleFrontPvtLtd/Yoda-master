package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Staff_Details_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Staff_Details_JSONParser {

    public static List<Staff_Details_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Staff_Details_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Staff_Details_Model flower = new Staff_Details_Model();
                flower.setName(obj.getString("user_name"));
                flower.setEmail(obj.getString("user_email"));
                flower.setContact(obj.getString("user_contact"));
                flower.setDetails(obj.getString("details"));
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
