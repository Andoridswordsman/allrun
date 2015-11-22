package com.tarena.allrun.biz.implAsmack;

import android.content.Intent;
import android.util.Log;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.ILoginBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;

public class LoginBiz implements ILoginBiz {
	/**
	 * 面试题1:ui控件是不是线程安全的 类分线程安全和线程不安全 StringBuffer是安全的 StringBuilder是不安全的
	 * vector是安全的,arrayList 是不安全的 线程安全：用同步实现的，速度慢 答案：ui控件不是线程安全的
	 * 
	 * 解决方法 1，handler.sendMessage() 2,发广播 3，runOnUiThread(runnable)
	 * 4,handler.post/postDelayed
	 * 
	 * 面试题 发消息和发广播有什么区别 发广播，别的应用能收到，
	 * 
	 * @param userEntity
	 */

	public void login(final UserEntity userEntity) {
		int threadId = (int) Thread.currentThread().getId();
		Log.i("loginBiz", "login " + threadId);

		new Thread("login thread") {
			public void run() {
				int status = -1;
				try {
					long time = System.currentTimeMillis();

					int threadId = (int) Thread.currentThread().getId();

					long startLoginTime = System.currentTimeMillis();
					Log.i("loginBiz", "login thread " + threadId + "time="
							+ (startLoginTime - TApplication.appStartTime));
					// 使用xmppConnection对象，判断是否连接上
					// 1,wait
					// if (TApplication.xmppConnection.isConnected()==false)
					// {
					// synchronized (TApplication.instance) {
					// TApplication.instance.wait();
					// Log.i("loginBiz", "login thread 开始wait" );
					//
					// }
					// }
					// 2:while
					int count = 0;
					while (count < 6000
							&& TApplication.xmppConnection.isConnected() == false) {
						count++;
						this.sleep(10);
					}
					// 再次判断是否连接上
					if (TApplication.xmppConnection.isConnected()) {
						Log.i("loginBiz", "login thread 不再wait");

						TApplication.xmppConnection.login(
								userEntity.getUsername(),
								userEntity.getPassword());
						// true 登录成功 false 失败
						boolean isSuccess = TApplication.xmppConnection
								.isAuthenticated();
						Log.i("loginBiz", "登录结果 " + isSuccess);

						if (isSuccess)
						{
							SendNullPackage.newInstance();
						}
						status = Const.STATUS_OK;
					}else
					{
						status = Const.STATUS_CONNECT_FAILURE;
					}
				} catch (Exception e) {
					status = Const.STATUS_PASSWORD_ERROR;

					String info = e.toString();
					if ("SASL authentication failed using mechanism DIGEST-MD5: "
							.equals(info)) {
						status = Const.STATUS_PASSWORD_ERROR;
						// 在asmack中如果密码错误，必须断开连接，重新连接。
						TApplication.xmppConnection.disconnect();
						TApplication.instance.connectChatServer();
					}
					ExceptionUtil.handleException(e);
				} finally {
					Intent intent = new Intent(Const.ACTION_LOGIN);
					intent.putExtra(Const.KEY_DATA, status);
					TApplication.instance.sendBroadcast(intent);

				}
			};
		}.start();
		long time = System.currentTimeMillis();
		Log.i("loginBiz", "return time " + time);

	}
}
