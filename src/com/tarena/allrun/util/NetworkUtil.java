package com.tarena.allrun.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	
	public static void checkNetworkState(final Context context)
	{
		try {
			//�ж���û����
			ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
			if (activeNetworkInfo==null)
			{
			//û����ʾһ��dialog
				AlertDialog.Builder dialog=new Builder(context);
				dialog.setMessage("�ף�����û��");
			
			//������
				dialog.setPositiveButton("������", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//�ص� callback
						try {
							//�ж�android�İ汾
							int version=android.os.Build.VERSION.SDK_INT;
							if (version>10)
							{
								Intent intent=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								context.startActivity(intent);
								dialog.cancel();
							}
						} catch (Exception e) {
							ExceptionUtil.handleException(e);
						}
						
					}
				});
				
				dialog.setNegativeButton("ȡ��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				dialog.show();
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
