package com.tarena.allrun.util;

import android.content.Intent;
import android.os.Environment;

public class Const {
	public static final String KEY_DATA="data";

public static final String ACTION_REGISTER="com.tarena.allrun.register";
//状态码
public static final int STATUS_OK=0;
public static final int STATUS_REGISTER_FAILURE=100;
public static final int STATUS_CONNECT_FAILURE=400;
public static final int STATUS_PASSWORD_ERROR=200;


public static final String ACTION_LOGIN = "ACTION_LOGIN";

public static final String ACTION_GET_NEARBY_USER = "ACTION_GET_NEARBY_USER";
public static final String ACTION_SHOW_GROUP_CHAT_MESSAGE = "SHOW_GROUP_CHAT_MESSAGE";
public static final String ACTION_UPDATE_MESSAGE = "ACTION_UPDATE_MESSAGE";

//mnt/sdcard/allRun/apk
//mnt/sdcard/allRun/audio

public static final String SDCARD_ROOT=Environment.getExternalStorageDirectory().getAbsolutePath();
public static final String  APK_PATH=SDCARD_ROOT+"/allRun/apk";
public static final String  AUDIO_PATH=SDCARD_ROOT+"/allRun/audio";
//访问这个url,给坐标，返回 json,中有地址
public static final String
MAP_QUERY_ADDRESS = "http://api.map.baidu.com/geocoder/v2/";

public final static String BAIDU_SERVER_KEY= "TWmMlH7LGdaSnWV06cjv3Dvl";

public static final String ACTION_GET_Address = "ACTION_GET_Address";

public static final String ACTION_CREATE_TOPIC_OVER = "ACTION_CREATE_TOPIC_OVER";

public static final String ACTION_SHOW_TOPIC = "ACTION_SHOW_TOPIC";


}
