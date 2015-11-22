package com.tarena.allrun.util;

import java.io.File;

import com.tarena.allrun.TApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ChatUtil {
	public static final String TAG_TEXT = "<text>";
	public static final String TAG_FACE = "<face>";
	public static final String TAG_IMAGE = "<image>";
	public static final String TAG_AUDIO = "<audio>";
	public static final String TAG_MAP = "<map>";

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_FACE = 2;
	public static final int TYPE_IMAGE = 3;
	public static final int TYPE_AUDIO = 4;
	public static final int TYPE_MAP = 5;

	/**
	 * 
	 * @return /mnt/sdcard/allRun/audio/1.mp3
	 */
	public static File getAudioFile() {
		// 判断/mnt/sdcard/allRun/audio存不存在
		File filePath = new File(Const.AUDIO_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		return new File(Const.AUDIO_PATH, "1.mp3");
	}

	public static String addAudioTag() {
//读数据
		byte[] data=Tools.readFileFromSdcard(getAudioFile().getAbsolutePath());
		//base64把byte[]转成字符串
		String body = Base64.encodeToString(data, Base64.DEFAULT);
		//加tag
		return TAG_AUDIO+body;
	}
	
	/**
	 * 声音必须写到文件 
	 * @param body
	 * @return
	 */
	public static File getAudio(String body)
	{
		int index=TAG_AUDIO.length();
		body=body.substring(index);
		byte[] audioData=Base64.decode(body, Base64.DEFAULT);
		Tools.writeToSdcard(TApplication.instance, Const.AUDIO_PATH, "1.mp3", audioData);
		return getAudioFile();
	}

	public static String addFaceTag(String faceFileName) {
		return TAG_FACE + faceFileName;
	}

	public static int getType(String body) {
		if (body.startsWith(TAG_FACE)) {
			return TYPE_FACE;
		} else if (body.startsWith(TAG_IMAGE)) {
			return TYPE_IMAGE;
		} else if (body.startsWith(TAG_AUDIO)) {
			return TYPE_AUDIO;
		} else if (body.startsWith(TAG_MAP)) {
			return TYPE_MAP;
		}
		return TYPE_TEXT;
	}

	public static String getFaceFileName(String body) {
		int index = TAG_FACE.length();
		return body.substring(index);
	}

	public static String addImageTag(byte[] data) {
		// byte[]转成字符串
		// 1,java gbk
		// String s=new String(data);
		// 好友收到后
		// byte[] dat2=s.getBytes();

		// 这个软件支持iphone,iphone,android的编码方式不一样
		// 字符编码 ascii,1 49
		// 字符编码Base64是iphone,android都支持
		String string = Base64.encodeToString(data, Base64.DEFAULT);
		return TAG_IMAGE + string;
	}

	public static Bitmap getImage(String body) {
		int index = TAG_IMAGE.length();
		body = body.substring(index);
		byte[] imageData = Base64.decode(body, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

	}
}
