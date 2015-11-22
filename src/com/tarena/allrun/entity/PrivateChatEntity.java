package com.tarena.allrun.entity;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.Message;

public class PrivateChatEntity {

	//���浱ǰ�û������к��ѵ���������
	//ArrayList<message>
	//ConcurrentHashMap,Vector�ǰ�ȫ��
	/**
	 * string��friend user
	 */
	public static ConcurrentHashMap<String, CopyOnWriteArrayList<Message>> map=new ConcurrentHashMap<String, CopyOnWriteArrayList<Message>>();
	
	public static void addMessage(String friendUser,Message message)
	{
		CopyOnWriteArrayList<Message> vector=map.get(friendUser);
		if (vector==null)
		{
			vector=new CopyOnWriteArrayList<Message>();
		}
		vector.add(message);
		map.put(friendUser, vector);
	}
}
