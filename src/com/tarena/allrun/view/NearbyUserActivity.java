package com.tarena.allrun.view;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.NearbyUserAdapter;
import com.tarena.allrun.biz.implAsmack.NearbyUserBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

public class NearbyUserActivity extends BaseActivity {
	PullToRefreshListView pullToRefreshListView;
	NearbyUserAdapter nearbyUserAdatper;
	BitmapUtils bitmapUtils = null;
	ShowNearbyUserReceiver showNearbyUserReceiver;
	ArrayList<UserEntity> list;
@Override
protected void onActivityResult(int requestCode, int resultCode, 
		Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode==100 && resultCode==200)
	{
	//说明data来之addFriendBiz不是来之系统图库的图片
		int status=data.getIntExtra(Const.KEY_DATA, -1);
		if (status==0)
		{
			Tools.showInfo(this, "添加好友信息发送成功，等待好友处理");
		}
	}
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.nearby_user);
			setupView();
			addListener();

			showNearbyUserReceiver = new ShowNearbyUserReceiver();
			this.registerReceiver(showNearbyUserReceiver, new IntentFilter(
					Const.ACTION_GET_NEARBY_USER));
			//NearbyUserBiz nearbyUserBiz = new NearbyUserBiz();
			NearbyUserBiz.query(TApplication.currentUser);
			Tools.showInfo(this, "正在查询");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(showNearbyUserReceiver);
	}

	private void addListener() {
		//设置下拉下拉刷新
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new Thread() {
							public void run() {

								try {
									//一开始从服务器上取100个数据，拉了之后，要从服务器再取数据
									//联网
									this.sleep(1000);

									UserEntity userEntity = new UserEntity();
									userEntity.setName("new userName");
									list.add(0,userEntity);

									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {
												nearbyUserAdatper.update(list);
												pullToRefreshListView
														.onRefreshComplete();
											} catch (Exception e) {
												// TODO: handle exception
											}
										}
									});

								} catch (Exception e) {
									// TODO: handle exception
								}
							};
						}.start();

					}
				});
	}

	private void setupView() {
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_nearby_user);
		bitmapUtils = new BitmapUtils(this);
		nearbyUserAdatper = new NearbyUserAdapter(this, null, bitmapUtils);
		pullToRefreshListView.setAdapter(nearbyUserAdatper);
		//快速滑动不加载  Xutils
		pullToRefreshListView.setOnScrollListener(new PauseOnScrollListener(
				bitmapUtils, false, true));
	}

	class ShowNearbyUserReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Tools.closeProgressDialog();
				list = (ArrayList<UserEntity>) intent
						.getSerializableExtra(Const.KEY_DATA);
				nearbyUserAdatper.update(list);
			} catch (Exception e) {
			}

		}
	}
}
