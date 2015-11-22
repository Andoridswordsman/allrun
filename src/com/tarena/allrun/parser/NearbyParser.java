package com.tarena.allrun.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tarena.allrun.entity.UserEntity;

public class NearbyParser {
	//http://124.207.192.18:8080/allRunServer/  ∑µªÿjson∏Ò Ω
	public static ArrayList<UserEntity> parser(String jsonString)
	{
		ArrayList<UserEntity> list=new ArrayList<UserEntity>();
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			String status=jsonObject.getString("status");
			if ("0".equals(status))
			{
				JSONArray jsonArray=jsonObject.getJSONArray("data");
				for (int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObjectUser=jsonArray.getJSONObject(i);
					String username="",nickname="",iconUrl="";
					try {
						username=jsonObjectUser.getString("username");
					} catch (Exception e) {
						
					}
					 try {
						 nickname=jsonObjectUser.getString("nickname");
					} catch (Exception e) {
					}
					 try {
						 iconUrl=jsonObjectUser.getString("iconUrl");
					 } catch (Exception e) {
					 }
					 
					 UserEntity userEntity=new UserEntity();
					 userEntity.setUsername(username);
					 userEntity.setName(nickname);
					 userEntity.setIconUrl(iconUrl);
					 list.add(userEntity);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
