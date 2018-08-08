package com.yodaapp.live.parsers;

import android.util.Log;

import com.yodaapp.live.model.Createadmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

;

public class CreateadminJSONParsers {

	public static List<Createadmin> parserFeed(String content)
	{
		try {
			
			JSONObject parentObject = new JSONObject(content);
			List<Createadmin> createadmin = new ArrayList<>();
				Createadmin flower = new Createadmin();
				flower.setId(parentObject.getString("id"));
				flower.setSchool_id(parentObject.getString("school_id"));
				flower.setSucess(parentObject.getString("success"));

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
