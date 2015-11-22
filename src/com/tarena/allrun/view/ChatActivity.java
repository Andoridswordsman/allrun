package com.tarena.allrun.view;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.Message;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.FaceAdapter;
import com.tarena.allrun.biz.implAsmack.GroupChatBiz;
import com.tarena.allrun.biz.implAsmack.PrivateChatBiz;
import com.tarena.allrun.entity.GroupChatEntity;
import com.tarena.allrun.entity.PrivateChatEntity;
import com.tarena.allrun.util.ChatUtil;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.ImageCompress;
import com.tarena.allrun.util.LogUtil;

public class ChatActivity extends BaseActivity {
	boolean isGroupChat = true;
	String friendUser;
	EditText etBody;
	TextView tvRoomName;
	Button btnSend;
	ShowGroupChatMessage showGroupChatMessage;

	ScrollView scrollView;
	LinearLayout llChatContent;
	Handler handler = new Handler();

	GridView gridView;
	LinearLayout llButton;
	Button btnFace;
	FaceAdapter faceAdapter;
	String[] faceFileName = new String[] { "f001.gif", "f002.gif", "f003.gif",
			"f004.gif", "f005.gif", "f006.gif", "f007.gif", "f008.gif",
			"f009.gif", "f010.gif", "f011.gif", "f012.gif", "f013.gif",
			"f014.gif", "f015.gif", "f016.gif", "f017.gif", "f018.gif",
			"f019.gif", "f020.gif", "f021.gif", "f022.gif", "f023.gif",
			"f024.gif", "f025.gif", "f026.gif", "f027.gif", "f028.gif",
			"f029.gif", "f030.gif", "f031.gif", "f032.gif", "f033.gif",
			"f034.gif", "f035.gif", "f036.gif", "f037.gif", "f038.gif",
			"f039.gif", "f040.gif", "f041.gif", "f042.gif", "f043.gif",
			"f044.gif", "f045.gif", "f046.gif", "f047.gif", "f048.gif",
			"f049.gif", "f050.gif", "f051.gif", "f052.gif", "f053.gif",
			"f054.gif", "f055.gif", "f056.gif", "f057.gif", "f058.gif",
			"f059.gif", "f060.gif", "f061.gif", "f062.gif", "f063.gif",
			"f064.gif", "f065.gif", "f066.gif", "f067.gif", "f068.gif",
			"f069.gif", "f070.gif", "f071.gif", "f072.gif", "f073.gif",
			"f074.gif", "f075.gif", "f076.gif", "f077.gif", "f078.gif",
			"f079.gif", "f080.gif", "f081.gif", "f082.gif", "f083.gif",
			"f084.gif", "f085.gif", "f086.gif", "f087.gif", "f088.gif",
			"f089.gif", "f090.gif", "f091.gif", "f092.gif", "f093.gif",
			"f094.gif", "f095.gif", "f096.gif", "f097.gif", "f098.gif",
			"f099.gif", "f100.gif", "f101.gif", "f102.gif", "f103.gif",
			"f104.gif", "f105.gif", "f106.gif", "f107.gif", "f108.gif",
			"f109.gif", "f110.gif", "f111.gif", "f112.gif", "f113.gif",
			"f114.gif", "f115.gif", "f116.gif", "f117.gif", "f118.gif",
			"f119.gif", "f120.gif", "f121.gif", "f122.gif", "f123.gif",
			"f124.gif", "f125.gif", "f126.gif", "f127.gif", "f128.gif",
			"f129.gif", "f130.gif", "f131.gif", "f132.gif", "f133.gif",
			"f134.gif", "f135.gif", "f136.gif", "f137.gif", "f138.gif",
			"f139.gif", "f140.gif", "f141.gif", "f142.gif" };
	Button btnImage, btnAudio;

	AlertDialog alertDialog;
	MediaRecorder mediaRecorder;
	Button btnMap;
	protected MapView mapView;

	// 定位
	BaiduMap baiduMap;
	LocationClient locationClient;
	TextView tvShowButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.chat);
			friendUser = this.getIntent().getStringExtra("friendUser");
			if (friendUser != null) {
				// 是私聊
				isGroupChat = false;
			}
			setupView();
			addListener();
			showGroupChatMessage = new ShowGroupChatMessage();
			this.registerReceiver(showGroupChatMessage, new IntentFilter(
					Const.ACTION_SHOW_GROUP_CHAT_MESSAGE));

		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(showGroupChatMessage);
	}

	private void addListener() {
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng clickPosition) {
				try {
					baiduMap.clear();
					addImage(clickPosition);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}

			}
		});
		btnAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// 显示dialog
					View view = View.inflate(ChatActivity.this,
							R.layout.recorder, null);
					alertDialog = new AlertDialog.Builder(ChatActivity.this)
							.create();
					alertDialog.setView(view);
					alertDialog.show();

					Button btnStart = (Button) view
							.findViewById(R.id.btn_recorder_start);
					final TextView tvState = (TextView) view
							.findViewById(R.id.tv_recorder_state);

					btnStart.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							try {
								// 判断动作是按下还是松开
								int action = event.getAction();
								switch (action) {
									case MotionEvent.ACTION_DOWN:
										// 按下
										if (mediaRecorder == null) {
											mediaRecorder = new MediaRecorder();
											mediaRecorder
													.setAudioSource(MediaRecorder.AudioSource.MIC);
											mediaRecorder
													.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
											mediaRecorder
													.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
											// mnt/sdcard/allrun/audio/1.mp3
											mediaRecorder.setOutputFile(ChatUtil
													.getAudioFile()
													.getAbsolutePath());
											mediaRecorder.prepare();
											mediaRecorder.start();
											tvState.setText("正在录音");
										}

										break;
									case MotionEvent.ACTION_UP:
										// 松开
										alertDialog.cancel();
										mediaRecorder.stop();
										mediaRecorder.release();
										mediaRecorder = null;
										System.gc();

										String body = ChatUtil.addAudioTag();
										GroupChatBiz.sendMessage(body);
										break;

								}
							} catch (Exception e) {
								ExceptionUtil.handleException(e);
							}
							return false;
						}
					});
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		btnImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 200);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String faceFileName = (String) faceAdapter.getItem(position);
				String body = ChatUtil.addFaceTag(faceFileName);
				GroupChatBiz.sendMessage(body);
				gridView.setVisibility(View.GONE);

			}
		});
		tvShowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (llButton.getVisibility()==View.GONE) {
						llButton.setVisibility(View.VISIBLE);
						mapView.setVisibility(View.GONE);
						gridView.setVisibility(View.GONE);
					}else {
						llButton.setVisibility(View.GONE);

					}

				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		btnFace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					llButton.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					llButton.setVisibility(View.GONE);
					mapView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String body = "";
					// 判断发的是地图还是文本
					if (mapView.getVisibility() == View.GONE) {
						body = etBody.getText().toString();
						etBody.getText().clear();
						if (isGroupChat) {
							GroupChatBiz.sendMessage(body);
						} else {
							PrivateChatBiz.sendMessage(friendUser, body);
						}

					} else {// 发地图
						baiduMap.snapshot(new SnapshotReadyCallback() {

							@Override
							public void onSnapshotReady(Bitmap bitmap) {
								try {
									ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
									bitmap.compress(CompressFormat.PNG, 10,
											outputStream);
									String body = ChatUtil
											.addImageTag(outputStream
													.toByteArray());
									if (isGroupChat) {
										GroupChatBiz.sendMessage(body);
									} else {
										PrivateChatBiz.sendMessage(friendUser, body);
									}
									mapView.setVisibility(View.GONE);
								} catch (Exception e) {
									ExceptionUtil.handleException(e);
								}

							}
						});

					}


				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
			}
		});

	}

	private void setupView() {
		tvRoomName = (TextView) findViewById(R.id.tv_chat_roomName);
		tvShowButton = (TextView) findViewById(R.id.tv_chat_showButton);

		etBody = (EditText) findViewById(R.id.et_chat_body);
		btnSend = (Button) findViewById(R.id.btn_chat_send);

		if (isGroupChat) {
			String roomName = TApplication.multiUserChat.getRoom();
			roomName = roomName.substring(0, roomName.indexOf("@"));
			tvRoomName.setText(roomName);
		} else {
			tvRoomName.setText(friendUser);
		}

		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		llChatContent = (LinearLayout) findViewById(R.id.linearLayoutChatContent);

		btnFace = (Button) findViewById(R.id.btn_face);
		gridView = (GridView) findViewById(R.id.gridView1);
		faceAdapter = new FaceAdapter(this, faceFileName);
		gridView.setAdapter(faceAdapter);

		llButton = (LinearLayout) findViewById(R.id.ll_chat_botton);

		btnImage = (Button) findViewById(R.id.btn_image);
		btnAudio = (Button) findViewById(R.id.btn_audio);
		btnMap = (Button) findViewById(R.id.btn_map);
		mapView = (MapView) findViewById(R.id.mapView);

		baiduMap = mapView.getMap();
		locationClient = new LocationClient(this);
		// 设置参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// 设置坐标类型bd09ll是百度自己的坐标类型
		option.setCoorType("bd09ll");
		// 设置每隔多长时间得一次坐标
		// 少于1000,只得一次
		option.setScanSpan(2);
		locationClient.setLocOption(option);
		// 一般情况下，只得一次
		// locationClient.requestLocation();
		MyLocationListener myLocationListener = new MyLocationListener();
		locationClient.registerLocationListener(myLocationListener);
		locationClient.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == 200) {
//				Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//						getContentResolver(), data.getData());
				ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
				options.maxHeight = 100;
				options.maxWidth = 100;
				options.uri = data.getData();

				ImageCompress imageCompress = new ImageCompress();
				Bitmap	bitmap = imageCompress.compressFromUri(this, options);
				// bitmap-->outputStream-->byte[]
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 10, outputStream);
				String body = ChatUtil.addImageTag(outputStream.toByteArray());
				GroupChatBiz.sendMessage(body);
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 在android说的话，spark上看不到，解决方法，logcat 看interceptor中有没有message 看 from
	 * to中服务器名称是否与openfire中的一致，如一致，说明android没问题
	 * 
	 * 有人在群里说的，andrid没收到，logcat中看listener有没有message
	 * 
	 * 你的代码所有的catch要exceptionUtil.看logcat,然后用debug跟
	 * 
	 * @author tarena
	 * 
	 */
	class ShowGroupChatMessage extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				CopyOnWriteArrayList<Message> vector = null;
				// 判断是私聊还是群聊
				if (isGroupChat) {
					String room = TApplication.multiUserChat.getRoom();
					vector = GroupChatEntity.map.get(room);
				} else {
					vector = PrivateChatEntity.map.get(friendUser);
				}

				// for (int i = 0; i < vector.size(); i++) {
				for (Message message : vector) {
					String from = message.getFrom();
					String body = message.getBody();
					LogUtil.i("ShowGroupChatMessage", from + ":" + body);
					// 判断是我说的，还是别人说的
					View view = null;
					if (from.equals(TApplication.currentUser.getUser())) {
						// 我说的
						view = View.inflate(context, R.layout.right, null);
					} else {
						// 好友说的
						view = View.inflate(context, R.layout.left, null);
					}

					if (!from.equals(TApplication.currentUser.getUser())) {
						// 好友说的
						TextView tvFriendName = (TextView) view
								.findViewById(R.id.tv_chat_friendName);
						// from:allrun@confrence.tarena.com/friendName
						String friendName = from.substring(from
								.lastIndexOf("/") + 1);
						tvFriendName.setText(friendName);
					}
					// 显示，判断数据的类型，有文本，有face
					// 不同类型显示不一样
					int type = ChatUtil.getType(body);
					if (type == ChatUtil.TYPE_AUDIO) {
						ImageView ivPlay = (ImageView) view
								.findViewById(R.id.iv_playAudio);
						ivPlay.setVisibility(View.VISIBLE);
						ivPlay.setTag(body);
						ivPlay.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									String body = (String) v.getTag();
									ChatUtil.getAudio(body);
									MediaPlayer mediaPlayer = new MediaPlayer();
									mediaPlayer.setDataSource(ChatUtil
											.getAudioFile().getAbsolutePath());
									mediaPlayer.prepare();
									mediaPlayer.start();
								} catch (Exception e) {
									ExceptionUtil.handleException(e);
								}
							}
						});

					}
					if (type == ChatUtil.TYPE_IMAGE) {
						ImageView iv = (ImageView) view
								.findViewById(R.id.iv_chat);
						iv.setVisibility(View.VISIBLE);

						iv.setImageBitmap(ChatUtil.getImage(body));
					}
					if (type == ChatUtil.TYPE_TEXT) {
						TextView tv = (TextView) view
								.findViewById(R.id.tv_chat_text);
						tv.setVisibility(View.VISIBLE);
						tv.setText(body);
					}
					if (type == ChatUtil.TYPE_FACE) {
						GifImageView gifImageView = (GifImageView) view
								.findViewById(R.id.gifImageView);
						gifImageView.setVisibility(View.VISIBLE);
						String fileName = ChatUtil.getFaceFileName(body);
						GifDrawable gifDrawable = new GifDrawable(getAssets(),
								fileName);
						gifImageView.setBackgroundDrawable(gifDrawable);
					}

					llChatContent.addView(view);

					vector.remove(message);
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							try {
								// 向上移
								// int linearLayoutHeight = llChatContent
								// .getHeight();
								// int scrollViewHeight =
								// scrollView.getHeight();
								// LogUtil.i("向上移", "linearLayoutHeight="
								// + linearLayoutHeight
								// + " scrollViewHeight="
								// + scrollViewHeight);
								//
								// if (linearLayoutHeight > scrollViewHeight) {
								// int moveY = linearLayoutHeight
								// - scrollViewHeight;
								// scrollView.scrollTo(0, moveY);
								// }
								scrollView.fullScroll(ScrollView.FOCUS_DOWN);

							} catch (Exception e) {
								ExceptionUtil.handleException(e);
							}
						}
					}, 1);// 有的手机samsung s4时间要长一点
				}
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	// 写一个程序，自启动，在service中定位，得到坐标，发送给自己
	// 在自己的手机上显示一个mapView,在坐标点上显示一个图
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			try {
				// 纬度
				double latitude = bdLocation.getLatitude();
				// 经度
				double longitude = bdLocation.getLongitude();
				LogUtil.i("定位结果", "纬度=" + latitude + ",经度=" + longitude);
				// 在模拟器上得到的是4.9E-324
				if (latitude == 4.9E-324) {
					latitude = 39.881891;
					longitude = 116.471553;
				}
				// 移动地图中心点到当前坐标
				LatLng currentLocation = new LatLng(latitude, longitude);
				moveMap(currentLocation);

				addImage(currentLocation);
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	private void moveMap(LatLng currentLocation) {
		// 更新地图中心点
		// 17 地图显示级别4-17
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(
				currentLocation, 17);
		// 以动画方式移动地图
		baiduMap.animateMapStatus(mapStatusUpdate);
	}

	private void addImage(LatLng currentLocation) {
		// 在坐标点上加图
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(currentLocation);
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_overlay_blue));
		baiduMap.addOverlay(markerOptions);
	}
}
