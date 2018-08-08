package com.yodaapp.live.Parent_Parsers;

import android.util.Log;

import com.yodaapp.live.Parent_Model.Branch_Details_Model;

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
                flower.setBranch_type(obj.getString("branch_type"));
                flower.setBranch_address(obj.getString("address"));
                flower.setBranch_phone(obj.getString("phone"));
                flower.setBranch_email(obj.getString("email"));
                flower.setBranch_website(obj.getString("website"));
                flower.setBranch_facebook(obj.getString("facebook"));
                flower.setBranch_timing(obj.getString("timing"));
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
