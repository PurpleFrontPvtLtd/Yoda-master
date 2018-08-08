package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Group_Model;
import com.yodaapp.live.model.Remove_role_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemoveStaffRolesParser {

    public static List<Remove_role_model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Remove_role_model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Remove_role_model flower = new Remove_role_model();
                flower.setId(obj.getString("id"));
                flower.setName(obj.getString("name"));
                flower.setRole(obj.getString("role"));
                flower.setRole_id(obj.getString("role_id"));
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
