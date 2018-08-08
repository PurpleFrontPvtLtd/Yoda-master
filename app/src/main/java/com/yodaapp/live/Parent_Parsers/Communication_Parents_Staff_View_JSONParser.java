package com.yodaapp.live.Parent_Parsers;


import android.util.Log;

import com.yodaapp.live.Parent_Model.Communication_Parents_Staff_View_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Communication_Parents_Staff_View_JSONParser {

    public static List<Communication_Parents_Staff_View_Model> parserFeed(String content)
    {
        try {
            JSONArray ar = new JSONArray(content);
            List<Communication_Parents_Staff_View_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Communication_Parents_Staff_View_Model flower = new Communication_Parents_Staff_View_Model();
                flower.setId(obj.getString("id"));
                flower.setName(obj.getString("name"));
                flower.setMessage_id(obj.getString("message_id"));
                flower.setStatus(obj.getString("status"));
                feedslist.add(flower);

            }
            return feedslist;
        } catch (JSONException e) {

            Log.d("error in json", "l " + e);
            return null;
        }
        catch(Exception e)
        {
            Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
