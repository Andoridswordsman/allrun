package com.tarena.allrun.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.Item;

import com.baidu.location.LLSInterface;
import com.tarena.allrun.R;
import com.tarena.allrun.entity.MsgEntity;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.view.ChatActivity;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {
	ArrayList<MsgEntity> list;
	Context context;

	public MsgAdapter(ArrayList<MsgEntity> list, Context context) {
		super();
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MsgEntity>();
		}
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.msg_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ll_unAccept = (LinearLayout) convertView
					.findViewById(R.id.ll_unAccept);
			viewHolder.ll_accept = (LinearLayout) convertView
					.findViewById(R.id.ll_accept);
			viewHolder.tv_unAccept = (TextView) convertView
					.findViewById(R.id.tv_unAccept);
			viewHolder.tv_accept = (TextView) convertView
					.findViewById(R.id.tv_accept);
			viewHolder.btnChat = (Button) convertView
					.findViewById(R.id.btn_chat);
			convertView.setTag(viewHolder);
		} else {
			// 重用
			viewHolder = (ViewHolder) convertView.getTag();
			// 里面有多个控件
			viewHolder.ll_accept.setVisibility(View.GONE);
			viewHolder.ll_unAccept.setVisibility(View.GONE);
		}
		MsgEntity msgEntity = list.get(position);
		// 有多个数据类型
		if (msgEntity.getMsg_id() == 0) {
			// 不同意
			viewHolder.ll_unAccept.setVisibility(View.VISIBLE);
			Presence presence = (Presence) msgEntity.getData();
			String friend = presence.getFrom();
			viewHolder.tv_unAccept.setText(friend + "不同意");
		}
		if (msgEntity.getMsg_id() == 1) {
			// 同意
			viewHolder.ll_accept.setVisibility(View.VISIBLE);

			final Item item = (Item) msgEntity.getData();
			String friend = item.getName();
			viewHolder.tv_accept.setText(friend + "同意");

			viewHolder.btnChat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						String friendUser = item.getUser();
						Intent intent = new Intent(context, ChatActivity.class);
						intent.putExtra("friendUser", friendUser);
						context.startActivity(intent);
					} catch (Exception e) {
						ExceptionUtil.handleException(e);
					}
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		LinearLayout ll_unAccept, ll_accept;
		TextView tv_unAccept, tv_accept;
		Button btnChat;
	}

}
