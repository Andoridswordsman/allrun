package com.tarena.allrun.entity;
/**
 * 消息模块
 * @author tarena
 *
 */
public class MsgEntity {
/**
 * 0:不同意 1:同意   2：聊天  3：红包
 */
	int msg_id;
	Object data;
	
	
	public MsgEntity(int msg_id, Object data) {
		super();
		this.msg_id = msg_id;
		this.data = data;
	}
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
