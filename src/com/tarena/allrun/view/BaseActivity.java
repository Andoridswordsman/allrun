package com.tarena.allrun.view;

import com.tarena.allrun.TApplication;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TApplication.listActivity.add(this);
	}

}
