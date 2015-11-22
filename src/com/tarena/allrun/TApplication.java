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
	 * release=true 软件发布 false:开发中
	 */
	public static boolean isRelease = false;
	public static MultiUserChat multiUserChat;
	public static ArrayList<Activity> listActivity = new ArrayList<Activity>();
	public static int networkType;
	public static int NETWORKTYPE_NONE = 1;
	public static int NETWORKTYPE_WIFI = 2;
	public static int NETWORKTYPE_MOBILE = 3;
	/**
	 * 在程序启动时执行一次。进程结束时，tapplicaton才会结束 作用：如果这个对象每个activity都会用到
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
				LogUtil.i("退出", activity.toString() + " finish了");
			}

			xmppConnection.disconnect();

			SendNullPackage.newInstance().isRunning = false;
			// 结束进程
			System.exit(0);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public void onCreate() {
		// application,activity不能删除，
		super.onCreate();
		LogUtil.i("退出", "application onCreate");

		instance = this;

		appStartTime = System.currentTimeMillis();
		try {
			// 天天在真机上测试程序
			// 1，真机找到设置-->开发者选项-->usb调试
			// 2, 装手机助手软件，连上手机，手机助手软件联网在你的笔记本上装驱动
			// 3, debug as -->debug configuration-->target 选第一项
			// 4,在手机的浏览器中访问http://ip:9090 如果看不到登录界面，说明网不通
			readConfig();
			connectChatServer();

			// 初始化地图
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
		// asmack用的证书加密
		config.setSecurityMode(SecurityMode.disabled);
		xmppConnection = new XMPPConnection(config);
		registerInterceptorListener();
		new Thread("connect thread") {
			public void run() {
				try {
					// openfrie有两个端口号
					// 1，浏览器 http://ip:9090 创建用户
					// 2,聊天客户端 spark,allrun socket(5222)

					int threadId = (int) Thread.currentThread().getId();
					for (int i = 0; i < 10; i++) {
						Log.i("connectChatServer", "i=" + i);
						xmppConnection.connect();
						Log.i("connectChatServer",
								"连接服务器结果1" + xmppConnection.isConnected());
						if (xmppConnection.isConnected()) {
							break;
						}
					}
					long connectOverTime = System.currentTimeMillis();
					Log.i("loginBiz", "connect thread " + threadId + "time="
							+ (connectOverTime - appStartTime));

					Log.i("connectChatServer",
							"连接服务器结果2" + xmppConnection.isConnected());
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
		// 让框架的接口指向实现类
		AllPackeInterceptor allPackeInterceptor = new AllPackeInterceptor();
		xmppConnection.addPacketInterceptor(allPackeInterceptor, null);

		AllPacketListener allPacketListener = new AllPacketListener();
		xmppConnection.addPacketListener(allPacketListener, null);
	}

	/**
	 * asmack发信息给openfire
	 * 
	 * @author tarena
	 * 
	 */
	class AllPackeInterceptor implements PacketInterceptor {

		@Override
		public void interceptPacket(Packet packet) {
			try {
				// packet 数据包：发给服务器的信息
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

		// 服务器有返回的信息
		@Override
		public void processPacket(Packet packet) {
			try {
				// packet 数据包：发给服务器的信息
				String classInfo = packet.toString();
				String xmlString = packet.toXML();
				LogUtil.i("AllPacketListener", "classInfo=" + classInfo
						+ "  xmlString=" + xmlString);
				if (packet instanceof RosterPacket) {
					// RosterPacket:放的是当前用户的好友信息
					RosterPacket rosterPacket = (RosterPacket) packet;
					ArrayList<Item> list = new ArrayList(
							rosterPacket.getRosterItems());
					for (Item item : list) {
						ItemType itemType = item.getItemType();
						if (itemType == ItemType.both) {
							MsgEntity msgEntity = new MsgEntity(1, item);
							listMsg.add(msgEntity);
							String friend = item.getName();
							LogUtil.i("添加好友结果", friend + "同意");
							sendBroadcast(new Intent(
									Const.ACTION_UPDATE_MESSAGE));

						}
					}
				}

				if (packet instanceof Presence) {
					// 服务器发过来的内容有可能是好友不同意
					Presence presence = (Presence) packet;
					Presence.Type type = presence.getType();
					if (type == Presence.Type.unsubscribe) {
						String friend = presence.getFrom();
						MsgEntity msgEntity = new MsgEntity(0, presence);
						listMsg.add(msgEntity);
						sendBroadcast(new Intent(Const.ACTION_UPDATE_MESSAGE));
						LogUtil.i("添加好友结果", friend + "不同意");
					}
				}
				// 通过logcat 发现有人在群里说话 ，我们收到的是<message>
				// asmack中一般标签名对应一个类名
				if (packet instanceof Message) {
					Message message = (Message) packet;
					Type type = message.getType();
					if (type == Type.chat) {
						// 私聊
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
								// 别人说的
								GroupChatEntity.addMessage(room, message);
								// 发广播
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
