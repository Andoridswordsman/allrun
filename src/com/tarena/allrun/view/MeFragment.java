package com.tarena.allrun.view;

import java.io.File;

//import net.youmi.android.AdManager;
//import net.youmi.android.offers.OffersManager;

import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.implAsmack.UpdateBiz;
import com.tarena.allrun.entity.VersionEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.util.Tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;

public class MeFragment extends Fragment {
	public static final int SHOW_VERSION_INFO = 1;
	public static final int INSTALL = 2;
	public static final int ERROR = 3;
	Button btnUpdate, btnExit,btnRecommend;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				Bundle bundle = msg.getData();
				switch (msg.what) {
				case INSTALL:
					String apkPath = bundle.getString(Const.KEY_DATA);
					File file = new File(apkPath);
					Uri uri = Uri.fromFile(file);

					Intent intent = new Intent(Intent.ACTION_VIEW);
					// type是mime
					String type = "application/vnd.android.package-archive";
					intent.setDataAndType(uri, type);
					getActivity().startActivity(intent);
					break;

				case SHOW_VERSION_INFO:
					VersionEntity versionEntity = (VersionEntity) bundle
							.getSerializable(Const.KEY_DATA);
					LogUtil.i("升级", versionEntity.getApkUrl());

					showDialog(versionEntity);
					break;
				case ERROR:
					Tools.showInfo(getActivity(), "升级失败");
					break;
				}
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}

		private void showDialog(final VersionEntity versionEntity)
				throws Exception {

			// 判断版本号是不是最新的
			String currentVersion = Tools.getCurrentVersion(getActivity());
			if (Double.parseDouble(currentVersion) < Double
					.parseDouble(versionEntity.getVersion())) {
				// showDialog
				AlertDialog.Builder dialog = new Builder(getActivity());
				dialog.setMessage(versionEntity.getVersion() + "\n"
						+ versionEntity.getChangeLog());
				dialog.setPositiveButton("升级",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									UpdateBiz.downloadApk(handler,
											versionEntity.getApkUrl());
									dialog.cancel();
								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});

				dialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						});
				dialog.show();
			} else {
				Tools.showInfo(getActivity(), "亲，你的版本已经是最新的");
			}
		};
	};
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = View.inflate(getActivity(), R.layout.fragment_me, null);
			setupView();
			addListener();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return view;
	}

	private void addListener() {
		btnRecommend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {

//					 显示积分墙，
//					显示在一个新的activity中，
//					activity在jar
//					 youmi sdk 有一个activity,.xml,联网，解析，显示
					AdManager.getInstance(getActivity()).init(
							"46bddc7cbaa5f53a", "360baaff5cf1121c", false);
					// 如果使用积分广告，请务必调用积分广告的初始化接口:
					OffersManager.getInstance(getActivity())
							.onAppLaunch();
					OffersManager.getInstance(getActivity())
							.showOffersWall();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					TApplication.instance.exit();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					UpdateBiz updateBiz = new UpdateBiz();
					updateBiz.getNewVersionInfo(handler, "zhangjiujun");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		btnUpdate = (Button) view.findViewById(R.id.btn_me_update);
		btnExit = (Button) view.findViewById(R.id.btn_me_exit);
		btnRecommend = (Button) view.findViewById(R.id.btn_me_recommend);
	}

}
