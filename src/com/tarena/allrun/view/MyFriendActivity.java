package com.tarena.allrun.view;

import java.util.ArrayList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.MyFriendAdapter;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MyFriendActivity extends BaseActivity {
	ExpandableListView expandableListView;
	MyFriendAdapter myFriendAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_friend);
		expandableListView = (ExpandableListView) this
				.findViewById(R.id.expandableListView1);
		Roster roster = TApplication.xmppConnection.getRoster();
		ArrayList<RosterGroup> listGroup = new ArrayList<RosterGroup>(
				roster.getGroups());
		myFriendAdapter = new MyFriendAdapter(listGroup, this);
		expandableListView.setAdapter(myFriendAdapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				try {
					RosterEntry rosterEntry=(RosterEntry) myFriendAdapter.getChild(groupPosition, childPosition);
					String friendUser=rosterEntry.getUser();
					
					Intent intent = new Intent(MyFriendActivity.this, ChatActivity.class);
					intent.putExtra("friendUser", friendUser);
					startActivity(intent);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
				return false;
			}
		});
	}

}
