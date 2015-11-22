package com.tarena.allrun.view;

import com.tarena.allrun.biz.implAsmack.GroupChatBiz;
import com.tarena.allrun.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ClubActivity extends BaseActivity {
	EditText etRoomName, etName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.input_club_name);
			setupView();
			addListener();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addListener() {
		
	}
	public void submit(View view)
	{

		try {
			String roomName = etRoomName.getText().toString();
			String name = etName.getText().toString();
			GroupChatBiz biz = new GroupChatBiz();
			biz.join(ClubActivity.this,roomName, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void setupView() {
		// TODO Auto-generated method stub
		etRoomName = (EditText) findViewById(R.id.et_input_room_roomName);
		etName = (EditText) findViewById(R.id.et_input_room_name);
	}

}
