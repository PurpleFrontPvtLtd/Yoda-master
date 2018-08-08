package com.yodaapp.live.parsers;


import android.util.Log;

import com.yodaapp.live.model.ExistingUser_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExistingUser_JSONParser {

    public static List<ExistingUser_Model> parserFeed(String content)
    {
        try {
            JSONObject parentObject = new JSONObject(content);
            List<ExistingUser_Model> createadmin = new ArrayList<>();
            ExistingUser_Model flower = new ExistingUser_Model();
            flower.setId(parentObject.getString("id"));
            flower.setSchool_id(parentObject.getString("school_id"));
            flower.setName(parentObject.getString("name"));
            flower.setEmail_real(parentObject.getString("email"));
            flower.setProfile_id(parentObject.getString("profile_id"));
            flower.setSchool_name(parentObject.getString("school_name"));
            flower.setMessage(parentObject.getString("success"));
            flower.setContact(parentObject.getString("contact"));
            flower.setStatus(parentObject.getString("status"));
            flower.setSchool_status(parentObject.getString("school_status"));

            createadmin.add(flower);
            return createadmin;
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
