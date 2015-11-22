package com.tarena.allrun.biz.implAsmack;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.UUID;

import org.jivesoftware.smack.AccountManager;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.MD5Util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RegisterBiz extends IntentService {
	int status = Const.STATUS_OK;

	// �޲ε�
	public RegisterBiz() {
		super("RegisterBiz");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			int threadId = (int) Thread.currentThread().getId();
			// ������Ϣ
			UserEntity userEntity = (UserEntity) intent
					.getSerializableExtra(Const.KEY_DATA);
			// ��ע����Ϣ����openfire
			AccountManager accountManager = TApplication.xmppConnection
					.getAccountManager();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", userEntity.getName());
			accountManager.createAccount(userEntity.getUsername(),
					userEntity.getPassword(), map);

			// ��ע����Ϣ����tomcat
			// 1,�ύ��˭
			String url = "http://" + TApplication.host
					+ ":8080/allRunServer/register.jsp";
			// 2,��ʲô����
			// httpClient,asyncHttpClient,java.net.HttpUrlConnection
			HttpUtils httpUtils = new HttpUtils(60000);
			RequestParams requestEntity = new RequestParams();
			requestEntity
					.addBodyParameter("username", userEntity.getUsername());
			String pwd = userEntity.getPassword();
			pwd = MD5Util.getStringMD5(pwd);
			requestEntity.addBodyParameter("md5password", pwd);
			requestEntity.addBodyParameter("nickname", userEntity.getName());

			// ���ļ�
			byte[] imageData = intent.getByteArrayExtra("imageData");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					imageData);
			String fileName = UUID.randomUUID().toString() + ".png";
			requestEntity.addBodyParameter("file", byteArrayInputStream,
					byteArrayInputStream.available(), fileName, "image/png");

			httpUtils.send(HttpMethod.POST, url, requestEntity,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String jsonString = responseInfo.result;
							Log.i("ע��", jsonString);
							try {
								JSONObject jsonObject = new JSONObject();
								String strStatus = jsonObject
										.getString("status");
								if ("0".equals(strStatus)) {
									status = Integer.parseInt(strStatus);
								}
							} catch (Exception e) {
								// TODO: handle exception
							} finally {
								// ���㲥
								Intent intent2 = new Intent(
										Const.ACTION_REGISTER);
								intent2.putExtra(Const.KEY_DATA, status);
								sendBroadcast(intent2);
							}

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							// ���㲥
							Intent intent2 = new Intent(Const.ACTION_REGISTER);
							intent2.putExtra(Const.KEY_DATA,
									Const.STATUS_REGISTER_FAILURE);
							sendBroadcast(intent2);
						}
					});

			// 3,�������������ݴ���
		} catch (Exception e) {
			status = Const.STATUS_REGISTER_FAILURE;
			// ���㲥
			Intent intent2 = new Intent(
					Const.ACTION_REGISTER);
			intent2.putExtra(Const.KEY_DATA, status);
			sendBroadcast(intent2);
		} finally {

		}

	}

}
