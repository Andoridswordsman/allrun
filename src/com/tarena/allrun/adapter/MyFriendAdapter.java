package com.tarena.allrun.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.tarena.allrun.R;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyFriendAdapter extends BaseExpandableListAdapter {
	// RosterGroup 好友分组
	ArrayList<RosterGroup> listGroup;
	Context context;

	public MyFriendAdapter(ArrayList<RosterGroup> list, Context context) {
		super();
		if (list != null) {
			this.listGroup = list;
		} else {
			this.listGroup = new ArrayList<RosterGroup>();
		}
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		RosterGroup rosterGroup = listGroup.get(groupPosition);
		return rosterGroup.getEntryCount();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return listGroup.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		RosterGroup rosterGroup = listGroup.get(groupPosition);
		ArrayList<RosterEntry> listFriend = new ArrayList<RosterEntry>(
				rosterGroup.getEntries());
		return listFriend.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	/**
	 * 有一个固定的id,return true
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		try {
			ViewHodlerGroup viewHodlerGroup = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.myfriend_group,
						null);
				viewHodlerGroup = new ViewHodlerGroup();
				viewHodlerGroup.tvGroupName = (TextView) convertView
						.findViewById(R.id.tv_my_friend_groupName);
				convertView.setTag(viewHodlerGroup);
			} else {
				viewHodlerGroup = (ViewHodlerGroup) convertView.getTag();
			}

			RosterGroup rosterGroup = (RosterGroup) this
					.getGroup(groupPosition);
			viewHodlerGroup.tvGroupName.setText(rosterGroup.getName());
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		try {
			ViewHodlerFriend viewHodlerFriend = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.myfriend_friend,
						null);
				viewHodlerFriend = new ViewHodlerFriend();
				viewHodlerFriend.tvFriendName = (TextView) convertView
						.findViewById(R.id.tv_my_friend_friendName);
				convertView.setTag(viewHodlerFriend);
			} else {
				viewHodlerFriend = (ViewHodlerFriend) convertView.getTag();
			}
			RosterEntry rosterEntry = (RosterEntry) this.getChild(
					groupPosition, childPosition);
			viewHodlerFriend.tvFriendName.setText(rosterEntry.getName());
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class ViewHodlerGroup {
		TextView tvGroupName;
	}

	class ViewHodlerFriend {
		TextView tvFriendName;
	}

}
