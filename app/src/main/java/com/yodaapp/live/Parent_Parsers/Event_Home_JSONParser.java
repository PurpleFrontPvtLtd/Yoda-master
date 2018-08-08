package com.yodaapp.live.Parent_Parsers;


import android.util.Log;

import com.yodaapp.live.Parent_Model.Event_Home_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Event_Home_JSONParser {
    public static List<Event_Home_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Event_Home_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Event_Home_Model flower = new Event_Home_Model();
                flower.setId(obj.getString("id"));
                flower.setName(obj.getString("name"));
                flower.setDetails(obj.getString("event_notes"));
                flower.setEvent_start(obj.getString("event_start"));
                flower.setEvent_end(obj.getString("event_end"));
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
