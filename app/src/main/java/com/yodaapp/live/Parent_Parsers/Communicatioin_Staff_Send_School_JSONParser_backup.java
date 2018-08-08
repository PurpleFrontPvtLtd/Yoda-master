package com.yodaapp.live.Parent_Parsers;

import android.util.Log;

import com.yodaapp.live.model.Communicatioin_Staff_Send_School_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Communicatioin_Staff_Send_School_JSONParser_backup {
    public static List<Communicatioin_Staff_Send_School_Model> parserFeed(String content)
    {
        try {
            JSONArray ar = new JSONArray(content);
            List<Communicatioin_Staff_Send_School_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Communicatioin_Staff_Send_School_Model flower = new Communicatioin_Staff_Send_School_Model();
                flower.setId(obj.getString("id"));
                flower.setMessages(obj.getString("messages"));
                flower.setSend_by_id(obj.getString("send_by_id"));
                flower.setSend_by(obj.getString("send_by"));
                flower.setSend_to(obj.getString("send_to"));
                flower.setSchool_id(obj.getString("school_id"));
                flower.setBranch_id(obj.getString("branch_id"));
                flower.setGrade_id(obj.getString("grade_id"));
                flower.setCreated(obj.getString("created"));
                flower.setAttachment_id(obj.getString("attachment_id"));
                flower.setFile_name(obj.getString("file_name"));
                flower.setAttachment_type(obj.getString("attachment_type"));
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
