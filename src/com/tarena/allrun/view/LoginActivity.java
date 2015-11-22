package com.tarena.allrun.view;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.biz.Factory;
import com.tarena.allrun.biz.ILoginBiz;
import com.tarena.allrun.biz.implAsmack.LoginBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.NetworkUtil;
import com.tarena.allrun.util.Tools;
import com.tarena.allrun.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	EditText etUserName, etPassword;
	Button tvSubmit, tvToRegister;
	LoginReceiver loginReceiver;
	String username, password;
	UserEntity userEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			NetworkUtil.checkNetworkState(this);

			setContentView(R.layout.activity_login);
			setView();
			addListener();
			loginReceiver = new LoginReceiver();
			this.registerReceiver(loginReceiver, new IntentFilter(
					Const.ACTION_LOGIN));
			for (int i = 0; i < 10000; i++) {
				TApplication.listUserEntity.add(new UserEntity());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(loginReceiver);
	}

	private void addListener() {
		MyListener myListener = new MyListener();
		tvSubmit.setOnClickListener(myListener);
		tvToRegister.setOnClickListener(myListener);

	}

	private void setView() {
		etUserName = (EditText) findViewById(R.id.et_login_username);
		etPassword = (EditText) findViewById(R.id.et_login_password);

		tvSubmit = (Button) findViewById(R.id.btn_login_submit);
		tvToRegister = (Button) findViewById(R.id.btn_login_register);
	}

	class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.btn_login_register) {
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
			}
			if (v.getId() == R.id.btn_login_submit) {

				username = etUserName.getText().toString();
				password = etPassword.getText().toString();
				StringBuilder builder = new StringBuilder();
				if (TextUtils.isEmpty(username)) {
					builder.append("用户名为空\n");
				}
				if (TextUtils.isEmpty(password)) {
					builder.append("密码为空\n");
				}
				if (!TextUtils.isEmpty(builder.toString())) {
					Toast.makeText(LoginActivity.this, builder.toString(),
							Toast.LENGTH_LONG).show();
					return;
				}

				Tools.showProgressDialog(LoginActivity.this, "亲，正在努力为你登录");

				userEntity = new UserEntity();
				userEntity.setUsername(username);
				userEntity.setPassword(password);
				// 直接从下一层创建对象，耦合度高
				// LoginBiz loginBiz = new LoginBiz();
				// loginBiz.login(userEntity);

				// ioc
				ILoginBiz iLoginBiz = Factory.getLoginBizInstance();
				iLoginBiz.login(userEntity);

				// 单击不执行onClick()
				tvSubmit.setEnabled(false);

			}
		}
	}

	class LoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			tvSubmit.setEnabled(true);
			Tools.closeProgressDialog();
			// 给用户一个详细的提示
			// 成功，连接服务器失败，密码错误，
			int status = intent.getIntExtra(Const.KEY_DATA, -1);
			if (status == Const.STATUS_OK) {
				userEntity.setUser(username + "@" + TApplication.serviceName);
				TApplication.currentUser = userEntity;
				Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_LONG)
						.show();
				startActivity(new Intent(context, MainFragmentActivity.class));
			}

			if (status == Const.STATUS_PASSWORD_ERROR) {
				Tools.showInfo(context, "密码错误");
				// startActivity(new Intent(context,
				// MainFragmentActivity.class));
			}
			if (status == Const.STATUS_CONNECT_FAILURE) {
				Tools.showInfo(context, "连接失败");
				// startActivity(new Intent(context,
				// MainFragmentActivity.class));
			}

		}

	}
}
