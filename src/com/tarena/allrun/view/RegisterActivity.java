package com.tarena.allrun.view;

import java.io.ByteArrayOutputStream;

import com.tarena.allrun.R;
import com.tarena.allrun.biz.implAsmack.RegisterBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ImageCompress;
import com.tarena.allrun.util.ImageCompress.CompressOptions;
import com.tarena.allrun.util.Tools;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText etUsername, etPassword, etConfirmPassword, etName;
	Button btnSubmit;

	MyReceiver myReceiver;
	ImageView ivSelectIcon;
	Bitmap bitmap;

	public void selectIcon(View view) {
		// 打开系统图库，选择图片
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 200);
	}

	/**
	 * 执行startActivityForResult，打开系统图库，选择完图片后，执行onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == 200) {
				// bitmap = MediaStore.Images.Media.getBitmap(
				// getContentResolver(), data.getData());
				// ivSelectIcon.setImageBitmap(bitmap);

				ImageCompress.CompressOptions options = new CompressOptions();
				options.maxHeight = 40;
				options.maxWidth = 40;
				options.uri = data.getData();

				ImageCompress imageCompress = new ImageCompress();
				bitmap = imageCompress.compressFromUri(this, options);
				ivSelectIcon.setImageBitmap(bitmap);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_register);
			setupView();
			addListener();

			myReceiver = new MyReceiver();
			this.registerReceiver(myReceiver, new IntentFilter(
					Const.ACTION_REGISTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void addListener() {
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					btnSubmit.setEnabled(false);

					Tools.showProgressDialog(RegisterActivity.this, "亲，正在为你注册");
					String username = etUsername.getText().toString();
					String password = etPassword.getText().toString();
					String name = etName.getText().toString();
					// 检查学员完成

					// 调IntentService
					Intent intent = new Intent(RegisterActivity.this,
							RegisterBiz.class);
					// 带数据
					UserEntity userEntity = new UserEntity();
					userEntity.setUsername(username);
					userEntity.setPassword(password);
					userEntity.setName(name);

					intent.putExtra(Const.KEY_DATA, userEntity);

					// 用intent不能直接带图片，带byte[]
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.PNG, 100,
							byteArrayOutputStream);
					byte[] imageData = byteArrayOutputStream.toByteArray();
					intent.putExtra("imageData", imageData);

					RegisterActivity.this.startService(intent);

				} catch (Exception e) {
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		etUsername = (EditText) findViewById(R.id.et_register_username);
		etPassword = (EditText) findViewById(R.id.et_register_password);
		etConfirmPassword = (EditText) findViewById(R.id.et_register_confirm_password);
		etName = (EditText) findViewById(R.id.et_register_name);

		btnSubmit = (Button) findViewById(R.id.btn_register_submit);
		ivSelectIcon = (ImageView) findViewById(R.id.iv_register_selectIcon);
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Tools.closeProgressDialog();
			btnSubmit.setEnabled(true);

			int status = intent.getIntExtra(Const.KEY_DATA, -1);
			if (status == Const.STATUS_OK) {
				Toast.makeText(context, "注册成功", 2000).show();
			} else {

				Toast.makeText(context, "注册失败", 2000).show();

			}
		}

	}
}
