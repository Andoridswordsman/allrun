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
					//70%�Ĵ����Ǵ����쳣���
					//���ϵ�¼�ɹ������˼��죬�������ȥ����Ϣ
					//����1:û������
					//����2:��ʱ��û�в������������ϰ���ĵ�¼״̬���
					//����1�ڶ������в�����
					//����2�ڶ���������sessiion����
					if (TApplication.networkType==
							TApplication.NETWORKTYPE_NONE)
					{
						//û��
						status=Const.STATUS_CONNECT_FAILURE;						
						
					}else
					{
						//�������п��� ���ӶϿ�
						
						if (TApplication.xmppConnection.isConnected()==false)
						{
							//����,����һ�������߳�
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
							//�����ɹ�,�ж��Ƿ��¼�ɹ�
							if (TApplication.xmppConnection
									.isAuthenticated()==false)
							{
								int count=0;
								//û�е�¼��
								//���µ�¼
								LoginBiz loginBiz=new LoginBiz();
								loginBiz.login(TApplication.currentUser);

								//�ȵ�¼
								count=0;
								while(count<6000 && TApplication.xmppConnection.isAuthenticated()==false)
								{
									count++;
									this.sleep(10);
								}
							}


						}
						//�ж��Ƿ��¼��
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
				// ����Ϣ���浽ʵ����
				GroupChatEntity.addMessage(room, message);
				// ���㲥
				Intent intent = new Intent(
						Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);

				TApplication.instance.sendBroadcast(intent);
				// Ҫ�Է������ϵ���Ϣ���м���
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
					// �ڱ�ĵط����õ�������ȫ�ֱ���
					TApplication.multiUserChat = multiUserChat;

					// ����ɹ�
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
							Tools.showInfo(activity, "����ʧ��");
						}
					});
				} finally {

				}

			};
		}.start();
	}

}
