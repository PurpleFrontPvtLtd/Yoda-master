package com.yodaapp.live.parsers;

import android.util.Log;

import com.yodaapp.live.model.Feedsdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedsdataJSONParser {
	public static List<Feedsdata> parserFeed(String content)
	{
		try {
			JSONArray ar = new JSONArray(content);
			List<Feedsdata> feedslist = new ArrayList<>();
			for(int i=0; i< ar.length(); i++)
			{
				JSONObject obj = ar.getJSONObject(i);
				Feedsdata flower = new Feedsdata();
				flower.setId(obj.getString("id"));
				flower.setName(obj.getString("name"));
				flower.setEmail(obj.getString("email"));
				flower.setPassword(obj.getString("password"));
				flower.setPhone(obj.getString("contact"));
				flower.setSchool_id(obj.getString("school_id"));
				flower.setBsname(obj.getString("school_name"));
				flower.setUser_type(obj.getString("user_type"));
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