package com.tarena.allrun.biz.implAsmack;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.IGroupChatBiz;
import com.tarena.allrun.entity.GroupChatEntity;
import com.tarena.allrun.entity.PrivateChatEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.Tools;
import com.tarena.allrun.view.ChatActivity;

import android.app.Activity;
import android.content.Intent;

public class PrivateChatBiz {
	/**
	 * 
	 * @param friendUser
	 * @param body
	 */
	public static void sendMessage(final String friendUser,final String body) {
		new Thread() {
			public void run() {
				int status=0;
				try {
					Message message = new Message();
					message.setTo(friendUser);
					String from = TApplication.currentUser.getUser();
					message.setFrom(from);

					message.setBody(body);
					message.setType(Type.chat);
					// 把信息保存到实体类
					PrivateChatEntity.addMessage(friendUser, message);
					// 发广播
					Intent intent = new Intent(
							Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);

					TApplication.instance.sendBroadcast(intent);
					
					//发到服务器上
					TApplication.xmppConnection.sendPacket(message);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			
		}.start();
	}

	

}
