package com.tarena.allrun.biz.implAsmack;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import com.tarena.allrun.TApplication;

/**
 * 心跳包，空包，维持连接
 * @author tarena
 *
 */
public class SendNullPackage extends Thread{
	private static SendNullPackage instance;
	public boolean isRunning=true;
	private SendNullPackage() {
     this.start();
	}
	public static synchronized SendNullPackage newInstance()
	{
		if (instance==null)
		{
			instance=new SendNullPackage();
		}
		return instance;
	}
	
	@Override
	public void run() {
		while (isRunning) {
			try {

				this.sleep(4*60*1000+50000);
				//服务器上如何判断客户端是否掉线
				long time=System.currentTimeMillis();
				Message message=new Message();
				message.setTo("system@tarena.com");
				message.setBody(String.valueOf(time));
				message.setFrom(TApplication.currentUser.getUser());
				message.setType(Type.chat);
				TApplication.xmppConnection.sendPacket(message);
				
			} catch (Exception e) {
			}
			
		}
	}

}
