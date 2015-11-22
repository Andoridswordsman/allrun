package com.tarena.allrun.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tarena.allrun.entity.VersionEntity;

public class UpdateParser {
	public VersionEntity parser(String string) {
		VersionEntity versionEntity = null;
		try {
			versionEntity=JSON.parseObject(string,VersionEntity.class);
//			JSONObject jsonObject = new JSONObject(string);
//			String status = jsonObject.getString("status");
//			String version = jsonObject.getString("version");
//			String changeLog = jsonObject.getString("changeLog");
//			String apkUrl = jsonObject.getString("apkUrl");
//
//			if ("0".equals(status)) {
//				versionEntity = new VersionEntity();
//				versionEntity.setStatus(status);
//				versionEntity.setNewVersion(version);
//				versionEntity.setChangeLog(changeLog);
//				versionEntity.setApkUrl(apkUrl);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionEntity;

	}
}
