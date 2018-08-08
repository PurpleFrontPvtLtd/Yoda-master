package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Branch_Details_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Branch_Details_JSONParser {
    public static List<Branch_Details_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Branch_Details_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Branch_Details_Model flower = new Branch_Details_Model();
                flower.setId(obj.getString("id"));
                flower.setName(obj.getString("name"));
                flower.setType(obj.getString("type"));
                flower.setUnread(obj.getString("unread"));
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
