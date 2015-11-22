package com.tarena.allrun.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tarena.allrun.entity.TopicEntity;
import com.tarena.allrun.util.ExceptionUtil;

public class TopicParser {
	public ArrayList<TopicEntity> parserAllData(String string) {
		ArrayList<TopicEntity> list = new ArrayList<TopicEntity>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jsonObjectTopic = array.getJSONObject(i);
					double longitude = 0, latitude = 0;
					String address = "", username = "", imageUrl = "", content = "";
					try {
						
						longitude = jsonObjectTopic.getDouble("longitude");
						latitude = jsonObjectTopic.getDouble("latitude");
					} catch (Exception e) {

					}
					try {
						address = jsonObjectTopic.getString("address");
					} catch (Exception e) {

					}
					try {
						imageUrl = jsonObjectTopic.getString("imageUrl");
					} catch (Exception e) {

					}
					try {
						username = jsonObjectTopic.getString("username");
					} catch (Exception e) {

					}
					try {
						content = jsonObjectTopic.getString("content");
					} catch (Exception e) {

					}

					TopicEntity topicEntity = new TopicEntity(username, content,
							imageUrl, address, 0.0, longitude, latitude);
					list.add(topicEntity);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return list;
	}
}
