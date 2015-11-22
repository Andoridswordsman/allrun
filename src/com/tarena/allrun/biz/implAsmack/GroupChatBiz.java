package com.tarena.allrun.biz.implAsmack;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.IGroupChatBiz;
import com.tarena.allrun.entity.GroupChatEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.Tools;
import com.tarena.allrun.view.ChatActivity;

import android.app.Activity;
import android.content.Intent;

public class GroupChatBiz implements IGroupChatBiz {
	public static void sendMessage(final String body) {
		new Thread() {
			public void run() {
				int status=0;
				try {
					//70%的代码是处理异常情况
					//早上登录成功，过了几天，打开软件，去发消息
					//问题1:没有连接
					//问题2:长时间没有操作，服务器上把你的登录状态清除
					//问题1在短连接中不存在
					//问题2在短连接中有sessiion存在
					if (TApplication.networkType==
							TApplication.NETWORKTYPE_NONE)
					{
						//没网
						status=Const.STATUS_CONNECT_FAILURE;						
						
					}else
					{
						//有网，有可能 连接断开
						
						if (TApplication.xmppConnection.isConnected()==false)
						{
							//重连,启了一个工作线程
							TApplication.instance.connectChatServer();
							
							int count=0;
							while(count<6000 && 
									TApplication.xmppConnection
									.isConnected()==false)
							{
								count++;
								this.sleep(10);
							}
						}

						if (TApplication.xmppConnection.isConnected())
						{
							//重连成功,判断是否登录成功
							if (TApplication.xmppConnection
									.isAuthenticated()==false)
							{
								int count=0;
								//没有登录上
								//重新登录
								LoginBiz loginBiz=new LoginBiz();
								loginBiz.login(TApplication.currentUser);

								//等登录
								count=0;
								while(count<6000 && TApplication.xmppConnection.isAuthenticated()==false)
								{
									count++;
									this.sleep(10);
								}
							}


						}
						//判断是否登录上
						if (TApplication.xmppConnection.isAuthenticated())
						{
							sendMessage(body);

						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			private void sendMessage(final String body) throws XMPPException {
				Message message = new Message();
				String room = TApplication.multiUserChat.getRoom();
				message.setTo(room);
				String from = TApplication.currentUser.getUser();
				message.setFrom(from);

				message.setBody(body);
				message.setType(Type.groupchat);
				// 把信息保存到实体类
				GroupChatEntity.addMessage(room, message);
				// 发广播
				Intent intent = new Intent(
						Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);

				TApplication.instance.sendBroadcast(intent);
				// 要对发到网上的信息进行加密
				Message decryptMessage = new Message();
				decryptMessage.setTo(room);
				decryptMessage.setFrom(from);
				byte[] data = body.getBytes();
				for (int i = 0; i < data.length; i++) {
					data[i] = Tools.decrypt(data[i]);
				}
				decryptMessage.setBody(new String(data));
				decryptMessage.setType(Type.groupchat);
				TApplication.multiUserChat.sendMessage(decryptMessage);
			};
		}.start();
	}

	/**
	 * 
	 * @param activity
	 * @param roonName
	 *            allRun
	 * @param name
	 */
	public void join(final Activity activity, final String roonName,
			final String name) {
		new Thread() {
			public void run() {
				try {
					String room = roonName + "@conference."
							+ TApplication.serviceName;
					MultiUserChat multiUserChat = new MultiUserChat(
							TApplication.xmppConnection, room);
					multiUserChat.join(name);
					TApplication.currentUser.setName(name);
					// 在别的地方会用到，作成全局变量
					TApplication.multiUserChat = multiUserChat;

					// 加入成功
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {
								// activity.handleTextView()
								activity.startActivity(new Intent(activity,
										ChatActivity.class));
							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Tools.showInfo(activity, "加入失败");
						}
					});
				} finally {

				}

			};
		}.start();
	}

}
