package com.tarena.allrun.receiver;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// 用户是打开还是关闭
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				// 用户关网了
				TApplication.networkType = TApplication.NETWORKTYPE_NONE;
				LogUtil.i("ChangedReceiver", "用户关网了");
			} else {
				// 判断网络类型
				// 电影
				// 4g 包月 500M ,超过500M 1m一块钱
				// 1秒100MB 24*60*60*100
				NetworkInfo wifiNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
					TApplication.networkType = TApplication.NETWORKTYPE_WIFI;
					LogUtil.i("ChangedReceiver", "用户开wifi");

				}

				NetworkInfo mobileNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobileNetworkInfo != null
						&& mobileNetworkInfo.isConnected()) {
					TApplication.networkType = TApplication.NETWORKTYPE_MOBILE;
					LogUtil.i("ChangedReceiver", "用户开mobile");

				}

			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

}
