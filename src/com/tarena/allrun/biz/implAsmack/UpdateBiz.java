package com.tarena.allrun.biz.implAsmack;

import java.io.File;
import java.util.UUID;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.VersionEntity;
import com.tarena.allrun.parser.UpdateParser;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.view.MeFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateBiz {
	/**
	 * 
	 * @param apkUrl "http://192.168.0.101:8080/allRunServer/allRunSample.apk"
	 */
	public static void downloadApk(final Handler handler,final String apkUrl)
	{
		String fileName=apkUrl.substring(apkUrl.lastIndexOf("/")+1);
		HttpUtils httpUtils=new HttpUtils();
		final String target=Const.APK_PATH+"/"+UUID.randomUUID().toString()+fileName;
		httpUtils.download(apkUrl, target, true, true, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
			//测试一下sdcard上有没有apk文件
				Message message=handler.obtainMessage();
				message.what=MeFragment.INSTALL;
				Bundle bundle=new Bundle();
				bundle.putString(Const.KEY_DATA, target);
				message.setData(bundle);
				handler.sendMessage(message);
				
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				Message message=handler.obtainMessage();
				message.what=MeFragment.ERROR;
				handler.sendMessage(message);
				
			}
		});
	}
	public static void getNewVersionInfo(final Handler handler,
			final String username) {
		String url = "http://" + TApplication.host
				+ ":8080/allRunServer/apkUpdate.jsp?username="+username;
		HttpUtils httpUtils = new HttpUtils(60000);
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				VersionEntity versionEntity = null;
				try {
					UpdateParser updateParser = new UpdateParser();
					versionEntity = updateParser.parser(responseInfo.result);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				} finally {
					// 发消息
					Message message = handler.obtainMessage();
					message.what = MeFragment.SHOW_VERSION_INFO;
					// message.obj=versionEntity;

					Bundle bundle = new Bundle();
					bundle.putSerializable(Const.KEY_DATA, versionEntity);
					message.setData(bundle);

					handler.sendMessage(message);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub

			}
		});
	}

}
