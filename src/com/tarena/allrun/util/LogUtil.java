package com.tarena.allrun.util;

import android.util.Log;

import com.tarena.allrun.TApplication;

/**
 * ͳһ������־
 * baidu android log4j
 * @author tarena
 * 
 */
public class LogUtil {
	public static void i(String tag, Object msg) {
		if (TApplication.isRelease) {
			return;
		}
		Log.i(tag, String.valueOf(msg));
	}

}
