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
			// �û��Ǵ򿪻��ǹر�
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				// �û�������
				TApplication.networkType = TApplication.NETWORKTYPE_NONE;
				LogUtil.i("ChangedReceiver", "�û�������");
			} else {
				// �ж���������
				// ��Ӱ
				// 4g ���� 500M ,����500M 1mһ��Ǯ
				// 1��100MB 24*60*60*100
				NetworkInfo wifiNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
					TApplication.networkType = TApplication.NETWORKTYPE_WIFI;
					LogUtil.i("ChangedReceiver", "�û���wifi");

				}

				NetworkInfo mobileNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobileNetworkInfo != null
						&& mobileNetworkInfo.isConnected()) {
					TApplication.networkType = TApplication.NETWORKTYPE_MOBILE;
					LogUtil.i("ChangedReceiver", "�û���mobile");

				}

			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

}
