package com.tarena.allrun;

import com.tarena.allrun.biz.implAsmack.LoginBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.view.LoginActivity;
import com.tarena.allrun.view.RegisterActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//testLog();
		
		
		setContentView(R.layout.activity_main);
		try {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					try {
						startActivity(new Intent(MainActivity.this,
								LoginActivity.class));
						MainActivity.this.finish();
					} catch (Exception e) {
						e.printStackTrace();

					}

				}
			}, 1000);
//			UserEntity userEntity=null;
//			LogUtil.i("", userEntity.getGroup());
		} catch (Exception e) {
			//每个方法加try  catch(发邮件)
			ExceptionUtil.handleException(e);
			
		}
	}

	private void testLog() {
		//1,使用log
		long startTime=0,endTime=0;
		startTime=System.currentTimeMillis();
		for (int i=0;i<10000;i++)
		{
			Log.i("testLog","i="+i);
		}
		endTime=System.currentTimeMillis();
		Log.i("testLog","time="+(endTime-startTime));
		//不用log
		
		startTime=System.currentTimeMillis();
		for (int i=0;i<10000;i++)
		{
			//Log.i("testLog","i="+i);
		}
		endTime=System.currentTimeMillis();
		Log.i("testLog","time="+(endTime-startTime));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
