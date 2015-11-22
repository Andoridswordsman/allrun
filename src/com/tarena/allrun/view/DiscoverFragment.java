package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DiscoverFragment extends Fragment {
	View view;
	LinearLayout llSportGroup,llNearby,llClub,llMall,llFriend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = View
					.inflate(getActivity(), R.layout.fragment_discover, null);
			setupView();
			addListener();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return view;
	}

	public void club(View view) {
		
	}

	private void addListener() {
		llClub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getActivity().startActivity(
							new Intent(getActivity(), ClubActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		llNearby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					getActivity()
							.startActivity(
									new Intent(getActivity(),
											NearbyUserActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		llMall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getActivity()
					.startActivity(
							new Intent(getActivity(),
									MallActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		llFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getActivity()
					.startActivity(
							new Intent(getActivity(),
									MyFriendActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		llSportGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getActivity()
					.startActivity(
							new Intent(getActivity(),
									TopicActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		llNearby = (LinearLayout) view.findViewById(R.id.ll_discover_nearby);
		llClub = (LinearLayout) view.findViewById(R.id.ll_discover_club);
		llMall = (LinearLayout) view.findViewById(R.id.ll_discover_mall);
		llFriend = (LinearLayout) view.findViewById(R.id.ll_discover_friend);
		llSportGroup = (LinearLayout) view.findViewById(R.id.ll_discover_sportGroup);
	}

}
