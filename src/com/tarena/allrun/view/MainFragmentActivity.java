package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainFragmentActivity extends FragmentActivity {
	SportFragment sportFragment;
	DiscoverFragment discoverFragment;
	MessageFragment messageFragment;
	MeFragment meFragment;
	Button[] btnArray = new Button[4];

	Fragment[] fragmentArray = null;
	/**
	 * 当前显示的fragment
	 */
	int currentIndex = 0;
	/**
	 * 选中的button,显示下一个fragment
	 */
	int selectedIndex;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		try {
			TApplication.listActivity.add(this);
			setContentView(R.layout.main_fragment);
			setupView();
			addListener();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	private void addListener() {
		MyButtonListener myButtonListener = new MyButtonListener();
		for (int i = 0; i < btnArray.length; i++) {
			btnArray[i].setOnClickListener(myButtonListener);
		}
	}

	private void setupView() {
		btnArray[0] = (Button) findViewById(R.id.btn_main_fragment_sport);
		btnArray[1] = (Button) findViewById(R.id.btn_main_fragment_discover);
		btnArray[2] = (Button) findViewById(R.id.btn_main_fragment_message);
		btnArray[3] = (Button) findViewById(R.id.btn_main_fragment_me);
		btnArray[0].setSelected(true);

		sportFragment = new SportFragment();
		discoverFragment = new DiscoverFragment();
		messageFragment = new MessageFragment();
		meFragment = new MeFragment();
		fragmentArray = new Fragment[] { sportFragment, discoverFragment,
				messageFragment, meFragment };

		// 一开始，显示第一个fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.fragment_container, sportFragment);
		transaction.show(sportFragment);
		transaction.commit();

	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.btn_main_fragment_sport:
					selectedIndex = 0;
					break;
				case R.id.btn_main_fragment_discover:
					selectedIndex = 1;
					break;
				case R.id.btn_main_fragment_message:
					selectedIndex = 2;
					break;
				case R.id.btn_main_fragment_me:
					selectedIndex = 3;
					break;
				}

				// 判断单击是不是当前的
				if (selectedIndex != currentIndex) {
					// 不是当前的
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					// 当前hide
					transaction.hide(fragmentArray[currentIndex]);
					// show你选中

					if (!fragmentArray[selectedIndex].isAdded()) {
						// 以前没添加过
						transaction.add(R.id.fragment_container,
								fragmentArray[selectedIndex]);
					}
					// 事务
					transaction.show(fragmentArray[selectedIndex]);
					transaction.commit();

					btnArray[currentIndex].setSelected(false);
					btnArray[selectedIndex].setSelected(true);
					currentIndex = selectedIndex;

				}
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}

		}

	}
}
