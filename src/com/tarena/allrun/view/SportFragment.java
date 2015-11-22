package com.tarena.allrun.view;

import java.util.ArrayList;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.tarena.allrun.R;
import com.tarena.allrun.util.BaiduMapUtil;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SportFragment extends Fragment {
	public double longitude = 116.471533;
	public double latitude = 39.881906;
	LocationClient locationClient;
	MapView mapView;
	private LocationMode mCurrentMode;

	BaiduMap baiduMap;

	static BDLocation lastLocation = null;
	View view = null;
	RelativeLayout relativeLayoutStartSport, relativeLayoutTitle;
	PopupWindow popupWindow;
	AlertDialog dialogCounter;

	TextView tvCounter;
	int count = 3;
	Handler handler = new Handler();

	int sleepTime = 500;
	ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
	LinearLayout ll_sport_recorder;
	TextView tvOperation;
	Runnable runnable;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = inflater.inflate(R.layout.fragment_sport, container, false);
			setupView();
			addListener();
			return view;
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return null;
	}

	private void addListener() {
		tvOperation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ll_sport_recorder.getVisibility()==View.VISIBLE)
				{
					ll_sport_recorder.setVisibility(View.GONE);
					tvOperation.setText("开始");
					listPoints.clear();
					baiduMap.clear();
					handler.removeCallbacks(runnable);
				}else if(ll_sport_recorder.getVisibility()==View.GONE)
				{
					tvOperation.setText("停止");

					dialogCounter = new AlertDialog.Builder(getActivity())
							.create();
					View view = View.inflate(getActivity(),
							R.layout.activity_show_counter, null);
					dialogCounter.setView(view);

					tvCounter = (TextView) view
							.findViewById(R.id.tv_show_count);
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							try {
								count = count - 1;
								tvCounter.setText(String.valueOf(count));
								if (count > 1) {
									handler.postDelayed(this, sleepTime);
								} else {
									dialogCounter.dismiss();
									count = 3;
									showRecorder();
								}
							} catch (Exception e) {
								ExceptionUtil.handleException(e);
							}

						}
					}, sleepTime);
					dialogCounter.show();
				}
			}
		});
	}

	private void showRecorder() {

		ll_sport_recorder.setVisibility(View.VISIBLE);
		// 计算时间
		final Chronometer chronometer = (Chronometer) view
				.findViewById(R.id.chronometer1);
		chronometer.start();
		chronometer.setBase(SystemClock.elapsedRealtime());
		final TextView tvDistance = (TextView) view
				.findViewById(R.id.tv_distance);
		tvDistance.setText("0.00");
		final TextView tvSpeed = (TextView) view
				.findViewById(R.id.tv_recorder_speed);
		tvSpeed.setText("0.00");

		runnable=new Runnable() {

			@Override
			public void run() {
				try {
					LogUtil.i("distance", "run");
					//distance单位是米
					double distance = 0;
					if (listPoints.size() >= 2) {
						for (int i = 0; i < listPoints.size() - 1; i++) {
							LatLng point1 = listPoints.get(i);
							LatLng point2 = listPoints.get(i + 1);
							distance = distance
									+ BaiduMapUtil.GetShortDistance(
											point1.longitude, point1.latitude,
											point2.longitude, point2.latitude);
						}
						LogUtil.i("distance", "distance=" + distance);
						//把米变成公里
						String strDistance = String.valueOf(distance / 1000);
						if (strDistance.contains(".")) {
							strDistance = strDistance.substring(0,
									strDistance.indexOf(".") + 3);
						}
						tvDistance.setText(strDistance);
						String strTime = chronometer.getText().toString();
						int minute = Integer.parseInt(strTime.split("\\:")[0]);
						int second = Integer.parseInt(strTime.split("\\:")[1]);
						String speed = String.valueOf((distance / 1000.0)
								/ ((minute * 60.0 + second) / (60 * 60.0)));
						LogUtil.i("speed", "minute=" + minute + ",second="
								+ second + ",distance=" + distance + ",speed="
								+ speed);

						if (speed.contains(".")) {
							speed = speed.substring(0, speed.indexOf(".") + 3);
						}
						tvSpeed.setText(speed);
					}
					handler.postDelayed(this, 1000);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		handler.postDelayed(runnable, 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		locationClient.stop();
	}

	private void setupView() {
		tvOperation= (TextView) view.findViewById(R.id.tv_fragment_sport_operation);
		ll_sport_recorder = (LinearLayout) view
				.findViewById(R.id.ll_sport_recorder);
		mapView = (MapView) view.findViewById(R.id.mapView);
		baiduMap = mapView.getMap();

		locationClient = new LocationClient(this.getActivity());
		MyLocationListener myLocationListener = new MyLocationListener();
		locationClient.registerLocationListener(myLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(1);
		locationClient.setLocOption(option);

		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {

				return false;
			}

			@Override
			public void onMapClick(LatLng clickedPosition) {

				listPoints.add(clickedPosition);

				baiduMap.clear();

				if (listPoints.size() >= 2) {
					// 画线
					OverlayOptions lineOptions = new PolylineOptions()
							.points(listPoints);
					// 把线显示在地图上
					baiduMap.addOverlay(lineOptions);
				}
			}
		});

		relativeLayoutStartSport = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_startSport);
		relativeLayoutTitle = (RelativeLayout) view.findViewById(R.id.title);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			locationClient.start();
			if (relativeLayoutStartSport.getVisibility() == View.GONE) {
				relativeLayoutStartSport.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation.getLatitude() != 4.9E-324) {
				latitude = bdLocation.getLatitude();
				longitude = bdLocation.getLongitude();
			}
			Log.i("MyLocationListener", latitude + "," + longitude);

			LatLng currentPosition = new LatLng(latitude, longitude);

			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
					currentPosition, 17.0f);
			baiduMap.animateMapStatus(u);

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(currentPosition);
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.icon_marka));
			baiduMap.addOverlay(markerOptions);
		}

	}
}
