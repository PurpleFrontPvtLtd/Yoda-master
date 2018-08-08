package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.GradeView_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GradeView_JSONParser {

    public static List<GradeView_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<GradeView_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                GradeView_Model flower = new GradeView_Model();
                flower.setHead_master_name(obj.getString("head_master"));
                flower.setHead_master_id(obj.getString("head_master_id"));
                flower.setSections(obj.getString("sections"));
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
