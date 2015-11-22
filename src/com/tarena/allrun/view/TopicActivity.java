package com.tarena.allrun.view;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.TopicAdapter;
import com.tarena.allrun.biz.implAsmack.TopicBiz;
import com.tarena.allrun.entity.TopicEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TopicActivity extends BaseActivity {
	TextView tvCreateTopic;
	double longitude = 116.471499;
	double latitude = 39.882367;

	ListView listView;
	TopicAdapter topicAdapter;
	TopicReceiver topicReceiver;
	boolean isRefresh = false;
	BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.topic);
			setupView();
			addListener();
			bitmapUtils = new BitmapUtils(this);

			// 先注册
			topicReceiver = new TopicReceiver();
			this.registerReceiver(topicReceiver, new IntentFilter(
					Const.ACTION_SHOW_TOPIC));

			// 联网从allRunServer服务器上查询数据
			TopicBiz.getAllData(TApplication.currentUser);
			Tools.showProgressDialog(this, "正在查询数据");

		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == RESULT_OK) {
			int status = data.getIntExtra(Const.KEY_DATA, -1);
			if (status == Const.STATUS_OK) {
				isRefresh = true;
				Tools.showProgressDialog(this, "正在查询最新数据");
				TopicBiz.getAllData(TApplication.currentUser);
			}

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(topicReceiver);
	}
	private void addListener() {
		tvCreateTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivityForResult(new Intent(TopicActivity.this,
							CreateTopicActivity.class),100);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});

	}

	private void setupView() {
		tvCreateTopic = (TextView) findViewById(R.id.tv_topic_create);
		listView = (ListView) findViewById(R.id.lv_topic);
	}

	class TopicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Tools.closeProgressDialog();
				if (isRefresh) {
					ArrayList<TopicEntity> list = (ArrayList<TopicEntity>) intent
							.getSerializableExtra(Const.KEY_DATA);
					topicAdapter.updateData(list);
					isRefresh=false;
				} else {
					ArrayList<TopicEntity> list = (ArrayList<TopicEntity>) intent
							.getSerializableExtra(Const.KEY_DATA);
					topicAdapter = new TopicAdapter(context, list, bitmapUtils);
					listView.setAdapter(topicAdapter);
				}

			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}
	}
}
