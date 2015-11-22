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
	 * ������1:ui�ؼ��ǲ����̰߳�ȫ�� ����̰߳�ȫ���̲߳���ȫ StringBuffer�ǰ�ȫ�� StringBuilder�ǲ���ȫ��
	 * vector�ǰ�ȫ��,arrayList �ǲ���ȫ�� �̰߳�ȫ����ͬ��ʵ�ֵģ��ٶ��� �𰸣�ui�ؼ������̰߳�ȫ��
	 * 
	 * ������� 1��handler.sendMessage() 2,���㲥 3��runOnUiThread(runnable)
	 * 4,handler.post/postDelayed
	 * 
	 * ������ ����Ϣ�ͷ��㲥��ʲô���� ���㲥�����Ӧ�����յ���
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
					// ʹ��xmppConnection�����ж��Ƿ�������
					// 1,wait
					// if (TApplication.xmppConnection.isConnected()==false)
					// {
					// synchronized (TApplication.instance) {
					// TApplication.instance.wait();
					// Log.i("loginBiz", "login thread ��ʼwait" );
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
					// �ٴ��ж��Ƿ�������
					if (TApplication.xmppConnection.isConnected()) {
						Log.i("loginBiz", "login thread ����wait");

						TApplication.xmppConnection.login(
								userEntity.getUsername(),
								userEntity.getPassword());
						// true ��¼�ɹ� false ʧ��
						boolean isSuccess = TApplication.xmppConnection
								.isAuthenticated();
						Log.i("loginBiz", "��¼��� " + isSuccess);

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
						// ��asmack�����������󣬱���Ͽ����ӣ��������ӡ�
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
