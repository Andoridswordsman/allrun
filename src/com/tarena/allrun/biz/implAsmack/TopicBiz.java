package com.tarena.allrun.biz.implAsmack;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONObject;

import android.content.Intent;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.TopicEntity;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.parser.TopicParser;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.util.MD5Util;

public class TopicBiz {
public static void queryAddress
(String latitude, String longitude) {
		
		HttpUtils httpUtils = new HttpUtils(60000);
		RequestParams requestParams = new RequestParams();
		
		requestParams.addQueryStringParameter("ak", Const.BAIDU_SERVER_KEY);
		requestParams.addQueryStringParameter("location", latitude + "," + longitude);
		requestParams.addQueryStringParameter("output", "json");
		requestParams.addQueryStringParameter("pois", "0");

		

		httpUtils.send(HttpMethod.GET, Const.MAP_QUERY_ADDRESS, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						String address = "";
						try {
							LogUtil.i("QueryAddress", responseInfo.result);

							try {
								JSONObject jsonObject = new JSONObject(responseInfo.result);
								JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
								address = jsonObjectResult.getString("formatted_address");
							} catch (Exception e) {
								// TODO: handle exception
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							Intent intent = new Intent(Const.ACTION_GET_Address);
							intent.putExtra(Const.KEY_DATA, address);
							TApplication.instance.sendBroadcast(intent);
						}

					
					}

					@Override
					public void onFailure(HttpException error, String msg) {

						
					}
				});

	}

public static void createTopic
(final UserEntity userEntity, 
		String content, 
		byte[] imageData,
		String address, 
		String latitude, 
		String longitude,
		String time) {
	String url="http://"+TApplication.host+":8080/allRunServer/addTopic.jsp";
	RequestParams requestParams=new RequestParams();
	requestParams.addBodyParameter("username", userEntity.getUsername());
	String md5password=userEntity.getPassword();
	md5password=MD5Util.getStringMD5(md5password);

	requestParams.addBodyParameter("md5password", md5password);

	requestParams.addBodyParameter("content", content);
	requestParams.addBodyParameter("address", address);

	ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(imageData);
	long length=byteArrayInputStream.available();
	String fileName=UUID.randomUUID().toString()+".png";
	String mimeType="image/png";
	requestParams.addBodyParameter("file", byteArrayInputStream, length, fileName, mimeType);
	HttpUtils httpUtils=new HttpUtils();
	//httpUtils.send(method, url, params, callBack)
	LogUtil.i("上传话题", "state send=" + System.currentTimeMillis());

	httpUtils.send(HttpMethod.POST, url,requestParams ,new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			try {
				LogUtil.i("上传话题", "onSuccess=" + System.currentTimeMillis());

				String jsonString=responseInfo.result;
				
				JSONObject jsonObject=new JSONObject(jsonString);
				String status=jsonObject.getString("status");
				if("0".equals(status))
				{
					Intent intentToReceiver=new Intent(Const.ACTION_CREATE_TOPIC_OVER);
					intentToReceiver.putExtra(Const.KEY_DATA, status);
					TApplication.instance.sendBroadcast(intentToReceiver);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			// TODO Auto-generated method stub
			
		}
	});

}
public static void getAllData(final UserEntity entity) {
	String url = "http://" + TApplication.host
			+ ":8080/allRunServer/queryTopic.jsp";
	HttpUtils httpUtils = new HttpUtils(60000);
	RequestParams requestParams = new RequestParams();
	requestParams.addQueryStringParameter("username", entity.getUsername());
	requestParams.addQueryStringParameter("md5password",
			MD5Util.getStringMD5(entity.getPassword()));

	httpUtils.send(HttpMethod.GET, url, requestParams,
			new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					ArrayList<TopicEntity> list = null;
					try {
						TopicParser mapParser = new TopicParser();
						 list = mapParser
								.parserAllData(responseInfo.result);
						Intent intent = new Intent(Const.ACTION_SHOW_TOPIC);
						intent.putExtra(Const.KEY_DATA, list);
						TApplication.instance.sendBroadcast(intent);
					} catch (Exception e) {
						ExceptionUtil.handleException(e);
					} finally {
						Intent intent = new Intent(
								Const.ACTION_SHOW_TOPIC);
						intent.putExtra(Const.KEY_DATA, list);
						TApplication.instance.sendBroadcast(intent);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

					Intent intent = new Intent(
							Const.ACTION_SHOW_TOPIC);

					TApplication.instance.sendBroadcast(intent);
				}
			});

	
	
	
	
	
}
}
