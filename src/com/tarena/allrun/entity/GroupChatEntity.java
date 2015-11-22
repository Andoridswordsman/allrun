package com.tarena.allrun.entity;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.Message;

public class GroupChatEntity {

	//���浱ǰ�û�������room�е���������
	//ArrayList<message>
	//ConcurrentHashMap,Vector�ǰ�ȫ��
	public static ConcurrentHashMap<String, CopyOnWriteArrayList<Message>> map=new ConcurrentHashMap<String, CopyOnWriteArrayList<Message>>();
	
	public static void addMessage(String room,Message message)
	{
		CopyOnWriteArrayList<Message> vector=map.get(room);
		if (vector==null)
		{
			vector=new CopyOnWriteArrayList<Message>();
		}
		vector.add(message);
		map.put(room, vector);
	}
}
