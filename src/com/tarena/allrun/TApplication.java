package com.tarena.allrun;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.xmlpull.v1.XmlPullParserException;

import com.baidu.mapapi.SDKInitializer;
import com.tarena.allrun.biz.implAsmack.SendNullPackage;
import com.tarena.allrun.entity.GroupChatEntity;
import com.tarena.allrun.entity.MsgEntity;
import com.tarena.allrun.entity.PrivateChatEntity;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class TApplication extends Application {

	public static ArrayList<MsgEntity> listMsg = new ArrayList<MsgEntity>();
	public static String test = "1";
	public static ArrayList<UserEntity> listUserEntity = new ArrayList<UserEntity>();
	/**
	 * release=true ������� false:������
	 */
	public static boolean isRelease = false;
	public static MultiUserChat multiUserChat;
	public static ArrayList<Activity> listActivity = new ArrayList<Activity>();
	public static int networkType;
	public static int NETWORKTYPE_NONE = 1;
	public static int NETWORKTYPE_WIFI = 2;
	public static int NETWORKTYPE_MOBILE = 3;
	/**
	 * �ڳ�������ʱִ��һ�Ρ����̽���ʱ��tapplicaton�Ż���� ���ã�����������ÿ��activity�����õ�
	 */
	public static XMPPConnection xmppConnection;
	public static String host, serviceName;
	private int port;
	public static long appStartTime;

	public static TApplication instance;
	public static UserEntity currentUser;

	public void exit() {
		try {
			for (Activity activity : listActivity) {
				activity.finish();
				LogUtil.i("�˳�", activity.toString() + " finish��");
			}

			xmppConnection.disconnect();

			SendNullPackage.newInstance().isRunning = false;
			// ��������
			System.exit(0);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public void onCreate() {
		// application,activity����ɾ����
		super.onCreate();
		LogUtil.i("�˳�", "application onCreate");

		instance = this;

		appStartTime = System.currentTimeMillis();
		try {
			// ����������ϲ��Գ���
			// 1������ҵ�����-->������ѡ��-->usb����
			// 2, װ�ֻ���������������ֻ����ֻ����������������ıʼǱ���װ����
			// 3, debug as -->debug configuration-->target ѡ��һ��
			// 4,���ֻ���������з���http://ip:9090 �����������¼���棬˵������ͨ
			readConfig();
			connectChatServer();

			// ��ʼ����ͼ
			SDKInitializer.initialize(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void readConfig() throws Exception {

		XmlResourceParser xmlResourceParser = this.getResources().getXml(
				R.xml.config);
		int eventType = xmlResourceParser.getEventType();

		while (eventType != XmlResourceParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlResourceParser.START_TAG:
				String tagName = xmlResourceParser.getName();
				if ("host".equals(tagName)) {
					host = xmlResourceParser.nextText();
				}
				if ("serviceName".equals(tagName)) {
					serviceName = xmlResourceParser.nextText();
				}
				if ("port".equals(tagName)) {
					port = Integer.parseInt(xmlResourceParser.nextText());
				}
				break;

			}
			eventType = xmlResourceParser.next();
		}
	}

	public void connectChatServer() {
		ConnectionConfiguration config = new ConnectionConfiguration(host,
				port, serviceName);
		// asmack�õ�֤�����
		config.setSecurityMode(SecurityMode.disabled);
		xmppConnection = new XMPPConnection(config);
		registerInterceptorListener();
		new Thread("connect thread") {
			public void run() {
				try {
					// openfrie�������˿ں�
					// 1������� http://ip:9090 �����û�
					// 2,����ͻ��� spark,allrun socket(5222)

					int threadId = (int) Thread.currentThread().getId();
					for (int i = 0; i < 10; i++) {
						Log.i("connectChatServer", "i=" + i);
						xmppConnection.connect();
						Log.i("connectChatServer",
								"���ӷ��������1" + xmppConnection.isConnected());
						if (xmppConnection.isConnected()) {
							break;
						}
					}
					long connectOverTime = System.currentTimeMillis();
					Log.i("loginBiz", "connect thread " + threadId + "time="
							+ (connectOverTime - appStartTime));

					Log.i("connectChatServer",
							"���ӷ��������2" + xmppConnection.isConnected());
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// synchronized (instance) {
					// instance.notify();
					// }
				}
			};
		}.start();

	}

	void registerInterceptorListener() {
		// �ÿ�ܵĽӿ�ָ��ʵ����
		AllPackeInterceptor allPackeInterceptor = new AllPackeInterceptor();
		xmppConnection.addPacketInterceptor(allPackeInterceptor, null);

		AllPacketListener allPacketListener = new AllPacketListener();
		xmppConnection.addPacketListener(allPacketListener, null);
	}

	/**
	 * asmack����Ϣ��openfire
	 * 
	 * @author tarena
	 * 
	 */
	class AllPackeInterceptor implements PacketInterceptor {

		@Override
		public void interceptPacket(Packet packet) {
			try {
				// packet ���ݰ�����������������Ϣ
				String classInfo = packet.toString();
				String xmlString = packet.toXML();
				LogUtil.i("AllPackeInterceptor", "classInfo=" + classInfo
						+ "  xmlString=" + xmlString);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	class AllPacketListener implements PacketListener {

		// �������з��ص���Ϣ
		@Override
		public void processPacket(Packet packet) {
			try {
				// packet ���ݰ�����������������Ϣ
				String classInfo = packet.toString();
				String xmlString = packet.toXML();
				LogUtil.i("AllPacketListener", "classInfo=" + classInfo
						+ "  xmlString=" + xmlString);
				if (packet instanceof RosterPacket) {
					// RosterPacket:�ŵ��ǵ�ǰ�û��ĺ�����Ϣ
					RosterPacket rosterPacket = (RosterPacket) packet;
					ArrayList<Item> list = new ArrayList(
							rosterPacket.getRosterItems());
					for (Item item : list) {
						ItemType itemType = item.getItemType();
						if (itemType == ItemType.both) {
							MsgEntity msgEntity = new MsgEntity(1, item);
							listMsg.add(msgEntity);
							String friend = item.getName();
							LogUtil.i("��Ӻ��ѽ��", friend + "ͬ��");
							sendBroadcast(new Intent(
									Const.ACTION_UPDATE_MESSAGE));

						}
					}
				}

				if (packet instanceof Presence) {
					// �������������������п����Ǻ��Ѳ�ͬ��
					Presence presence = (Presence) packet;
					Presence.Type type = presence.getType();
					if (type == Presence.Type.unsubscribe) {
						String friend = presence.getFrom();
						MsgEntity msgEntity = new MsgEntity(0, presence);
						listMsg.add(msgEntity);
						sendBroadcast(new Intent(Const.ACTION_UPDATE_MESSAGE));
						LogUtil.i("��Ӻ��ѽ��", friend + "��ͬ��");
					}
				}
				// ͨ��logcat ����������Ⱥ��˵�� �������յ�����<message>
				// asmack��һ���ǩ����Ӧһ������
				if (packet instanceof Message) {
					Message message = (Message) packet;
					Type type = message.getType();
					if (type == Type.chat) {
						// ˽��
						String friendUser = message.getFrom();
						if (friendUser.contains("/")) {
							friendUser = friendUser.substring(0,
									friendUser.indexOf("/"));
							PrivateChatEntity.addMessage(friendUser, message);

							sendBroadcast(new Intent(
									Const.ACTION_SHOW_GROUP_CHAT_MESSAGE));
						}

					}
					if (type == Type.groupchat) {
						// allrun@confrences.tarena.com/pc1
						String from = message.getFrom();
						if (from.contains("/")) {
							String room = from.substring(0, from.indexOf("/"));
							String name = from.substring(from.indexOf("/") + 1);
							if (!name.equals(currentUser.getName())) {
								// ����˵��
								GroupChatEntity.addMessage(room, message);
								// ���㲥
								Intent intent = new Intent(
										Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);
								TApplication.instance.sendBroadcast(intent);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

}
