package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.MsgAdapter;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MessageFragment extends Fragment {
	View view;
	ListView listView;
	MsgAdapter msgAdapter;
	UpdateMessageReceiver updateMessageReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = View.inflate(getActivity(), R.layout.fragment_message, null);
			setupView();
			addListener();
			updateMessageReceiver = new UpdateMessageReceiver();
			getActivity().registerReceiver(updateMessageReceiver,
					new IntentFilter(Const.ACTION_UPDATE_MESSAGE));
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(updateMessageReceiver);
	}

	private void addListener() {
		// TODO Auto-generated method stub

	}

	private void setupView() {
		// TODO Auto-generated method stub
		listView = (ListView) view.findViewById(R.id.listView_msg);
		msgAdapter = new MsgAdapter(TApplication.listMsg, getActivity());
		listView.setAdapter(msgAdapter);
	}

	class UpdateMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				msgAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}

	}

}
