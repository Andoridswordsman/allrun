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
		// �ж�/mnt/sdcard/allRun/audio�治����
		File filePath = new File(Const.AUDIO_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		return new File(Const.AUDIO_PATH, "1.mp3");
	}

	public static String addAudioTag() {
//������
		byte[] data=Tools.readFileFromSdcard(getAudioFile().getAbsolutePath());
		//base64��byte[]ת���ַ���
		String body = Base64.encodeToString(data, Base64.DEFAULT);
		//��tag
		return TAG_AUDIO+body;
	}
	
	/**
	 * ��������д���ļ� 
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
		// byte[]ת���ַ���
		// 1,java gbk
		// String s=new String(data);
		// �����յ���
		// byte[] dat2=s.getBytes();

		// ������֧��iphone,iphone,android�ı��뷽ʽ��һ��
		// �ַ����� ascii,1 49
		// �ַ�����Base64��iphone,android��֧��
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
