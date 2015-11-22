package com.tarena.allrun.biz.implAsmack;

import java.util.ArrayList;

import android.content.Intent;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.parser.NearbyParser;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.util.MD5Util;

public class NearbyUserBiz {
	public static void query(final UserEntity userEntity) {
		String url = "http://" + TApplication.host
				+ ":8080/allRunServer/queryNearbyUser.jsp";
		HttpUtils httpUtils = new HttpUtils(60000);
		RequestParams requestParams = new RequestParams();
		requestParams.addQueryStringParameter("username",
				userEntity.getUsername());
		String pwd = MD5Util.getStringMD5(userEntity.getPassword());
		requestParams.addQueryStringParameter("md5password", pwd);
		requestParams.addQueryStringParameter("latitude", "0.000000");
		requestParams.addQueryStringParameter("longitude", "0.000000");

		httpUtils.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						ArrayList<UserEntity> list = null;
						try {
							// 测试服务器是否正确返回数据，必须打印服务器返回的结果
							LogUtil.i("NearbyUserBiz", responseInfo.result);
							list = NearbyParser.parser(responseInfo.result);
						} catch (Exception e) {

						} finally {
							Intent intent = new Intent(
									Const.ACTION_GET_NEARBY_USER);
							intent.putExtra(Const.KEY_DATA, list);
							TApplication.instance.sendBroadcast(intent);
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {

						Intent intent = new Intent(Const.ACTION_GET_NEARBY_USER);
						TApplication.instance.sendBroadcast(intent);
					}
				});
	}

}
