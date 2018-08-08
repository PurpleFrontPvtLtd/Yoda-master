package com.yodaapp.live.parsers;

import android.util.Log;

import com.yodaapp.live.model.Group_Message_Receive_Model;
import com.yodaapp.live.model.MessageGroupDetails_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MessageGroupDetails_JSONParser {
    public static List<MessageGroupDetails_Model> parserFeed(String content)
    {
        try {
            JSONArray ar = new JSONArray(content);
            List<MessageGroupDetails_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                MessageGroupDetails_Model flower = new MessageGroupDetails_Model();
                flower.setId(obj.getString("id"));
                flower.setGroup_name(obj.getString("group_name"));
                flower.setScope_id(obj.getString("scope_id"));
                flower.setSchool_id(obj.getString("school_id"));
                flower.setBranch_id(obj.getString("branch_id"));
                flower.setGrade_id(obj.getString("grade_id"));
                flower.setSection_id(obj.getString("section_id"));
                flower.setSection_subject_id(obj.getString("section_subject_id"));
                flower.setCreated(obj.getString("created"));
                flower.setUpdated(obj.getString("updated"));
                flower.setCreated_by_name(obj.getString("created_by_name"));
                flower.setUnread(obj.getString("unread"));
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
