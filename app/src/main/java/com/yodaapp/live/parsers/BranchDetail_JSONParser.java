package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.BranchDetail_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BranchDetail_JSONParser {

    public static List<BranchDetail_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<BranchDetail_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                BranchDetail_Model flower = new BranchDetail_Model();
                flower.setBranch_type(obj.getString("branch_type"));
                flower.setBranch_address(obj.getString("address"));
                flower.setBranch_phone(obj.getString("phone"));
                flower.setBranch_email(obj.getString("email"));
                flower.setBranch_website(obj.getString("website"));
                flower.setBranch_facebook(obj.getString("facebook"));
                flower.setBranch_timing(obj.getString("timing"));
                flower.setBranch_grade(obj.getString("grades"));
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
