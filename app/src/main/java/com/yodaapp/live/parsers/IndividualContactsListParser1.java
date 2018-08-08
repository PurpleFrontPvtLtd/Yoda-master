package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.Group_Model;
import com.yodaapp.live.model.Group_Model1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndividualContactsListParser1 {

    public static List<Group_Model1> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Group_Model1> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Group_Model1 flower = new Group_Model1(obj.getString("id"),
                obj.getString("name"),obj.getString("contact"),false,false);



//                flower.setId(obj.getString("id"));
//                flower.setName(obj.getString("name"));
//                flower.setPhone(obj.getString("contact"));
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
