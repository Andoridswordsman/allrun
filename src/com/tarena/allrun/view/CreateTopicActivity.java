package com.tarena.allrun.view;

import java.io.ByteArrayOutputStream;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.implAsmack.TopicBiz;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.ImageCompress;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.util.Tools;
import com.tarena.allrun.util.ImageCompress.CompressOptions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTopicActivity extends BaseActivity {
	TextView tvSubmit;
	ImageView ivOpenImageLibrary;
	Bitmap bitmap;
	public String address;
	MyReceiver myReceiver;

	MapView mapView;
	BaiduMap baiduMap;

	double longitude = 116.471512;
	double latitude = 39.882468;

	LocationClient locationClient;

	EditText etBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.create_topic);
			setupView();
			addListener();
			myReceiver = new MyReceiver();

			this.registerReceiver(myReceiver, new IntentFilter(
					Const.ACTION_GET_Address));
			this.registerReceiver(myReceiver, new IntentFilter(
					Const.ACTION_CREATE_TOPIC_OVER));
		} catch (Exception e) {
			// ExceptionUtil.handleException(e);
		}
	}

	@Override
	protected void onDestroy() {
		mapView.onDestroy();
		this.unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	// 在执行了startActivityForResult后，选中了数据后
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == this.RESULT_OK) {
			try {
				ImageCompress.CompressOptions options = new CompressOptions();
				options.maxHeight = 480;
				options.maxWidth = 480;
				options.uri = data.getData();

				ImageCompress imageCompress = new ImageCompress();
				bitmap = imageCompress.compressFromUri(this, options);
				ivOpenImageLibrary.setImageBitmap(bitmap);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addListener() {
		try {
			ivOpenImageLibrary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 打开系统图库
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					// 启动一个activity,我们通过onActivityResult得到数据
					startActivityForResult(intent, 0);
				}
			});
			tvSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						address = null;
						Tools.showProgressDialog(CreateTopicActivity.this,
								"正在创建话题");

						// 1:发坐标到百度云服务器查询地址

						TopicBiz.queryAddress(String.valueOf(latitude),
								String.valueOf(longitude));
						// 2,发content,address,imageUrl到allRunServer

						new Thread() {
							public void run() {
								try {
									// onrageUrl和address
									int count = 0;
									while (count < 1000 && address == null) {
										this.sleep(60);
										count++;
										LogUtil.i("上传话题", "count=" + count);
									}

									runOnUiThread(new Runnable() {

										@Override
										public void run() {

											String body = etBody.getText()
													.toString();
											ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
											bitmap.compress(CompressFormat.PNG,
													10, byteArrayOutputStream);
											String time = String.valueOf(System
													.currentTimeMillis());
											byte[] imageData = byteArrayOutputStream
													.toByteArray();
											LogUtil.i("上传话题", "state createTopic=" + System.currentTimeMillis());

											TopicBiz.createTopic(
													TApplication.currentUser,
													body, imageData, address,
													String.valueOf(latitude),
													String.valueOf(longitude),
													time);

										}
									});

								} catch (Exception e) {
									// TODO: handle exception
								}

							};
						}.start();

					} catch (Exception e) {
						// ExceptionUtil.handleException(e);
					}
				}
			});
		} catch (Exception e) {
			// ExceptionUtil.handleException(e);
		}
	}

	private void setupView() {
		try {
			etBody = (EditText) findViewById(R.id.et_create_topic_text);
			tvSubmit = (TextView) findViewById(R.id.tv_create_topic_submit);
			ivOpenImageLibrary = (ImageView) findViewById(R.id.iv_create_topic_openImageliberary);

			mapView = (MapView) findViewById(R.id.mapView);

			locationClient = new LocationClient(this);
			MyLocationListener myLocationListener = new MyLocationListener();
			locationClient.registerLocationListener(myLocationListener);

			// 设置参数
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);
			option.setCoorType("bd09ll");
			option.setScanSpan(1);

			// 重新定位
			// locationClient.requestLocation();

			locationClient.start();

			// 向地图添加一个图片
			baiduMap = mapView.getMap();
			baiduMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onMapClick(LatLng clickPosition) {

					baiduMap.clear();
					addMarkerOptions(clickPosition);
					latitude = clickPosition.latitude;
					longitude = clickPosition.longitude;

				}
			});
		} catch (Exception e) {
			// ExceptionUtil.handleException(e);
		}
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();

				if (Const.ACTION_GET_Address.equals(action)) {
					address = intent.getStringExtra(Const.KEY_DATA);
					LogUtil.i("上传话题", "address=" + address);

				}
				if (Const.ACTION_CREATE_TOPIC_OVER.equals(action)) {
					// dialog 先显示在发送图片，再显示发表话题
					try {
						LogUtil.i("上传话题", "toast=" + System.currentTimeMillis());

						Tools.closeProgressDialog();
						Toast.makeText(context, "创建话题成功", 1000).show();
						Intent data = new Intent();
						data.putExtra(Const.KEY_DATA, Const.STATUS_OK);
						CreateTopicActivity.this.setResult(RESULT_OK,
								data);
						CreateTopicActivity.this.finish();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}

	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			// 得不到坐标
			// 判断坐标是不是正确
			if (bdLocation.getLongitude() != 4.9E-324) {
				// 得到了正确的坐标
				longitude = bdLocation.getLongitude();
				latitude = bdLocation.getLatitude();
			}
			// 移动中心点
			LatLng currentPosition = new LatLng(latitude, longitude);
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLngZoom(currentPosition, 17);
			baiduMap.animateMapStatus(mapStatusUpdate);

			// 显示图片
			addMarkerOptions(currentPosition);
		}

	}

	private void addMarkerOptions(LatLng currentPosition) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(currentPosition);
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_overlay_blue));
		baiduMap.addOverlay(markerOptions);
	}
}
