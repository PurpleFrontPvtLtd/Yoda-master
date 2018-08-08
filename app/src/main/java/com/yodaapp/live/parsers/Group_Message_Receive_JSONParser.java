package com.yodaapp.live.parsers;

import android.util.Log;

import com.yodaapp.live.model.Communicatioin_Staff_Send_School_Model;
import com.yodaapp.live.model.Group_Message_Receive_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Group_Message_Receive_JSONParser {
    public static List<Group_Message_Receive_Model> parserFeed(String content)
    {
        try {
            JSONArray ar = new JSONArray(content);
            List<Group_Message_Receive_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Group_Message_Receive_Model flower = new Group_Message_Receive_Model();
                flower.setId(obj.getString("id"));
                flower.setMessage(obj.getString("message"));
                flower.setSent_by_id(obj.getString("sent_by"));
                flower.setSent_by(obj.getString("name"));
                flower.setGroupName(obj.getString("group_name"));
                flower.setGroup_id(obj.getString("group_id"));
                flower.setCreated(obj.getString("created"));
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
