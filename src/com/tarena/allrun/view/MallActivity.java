package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.util.ExceptionUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MallActivity extends BaseActivity {
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.mall);
			webView = (WebView) findViewById(R.id.webView1);
			webView.loadUrl("http://192.168.188.98:8080/mall/test.html");
			webView.setWebViewClient(new WebViewClient() {
				// 单击超链接
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url.contains("tarena/phone:")) {
						// 打电话
						// url=http://ip:808/tarena/phone:88886666
						int startIndex = url.lastIndexOf("tarena/phone:");
						int phoneIndex = startIndex + "tarena/phone:".length();
						String phone = url.substring(phoneIndex);

						Uri uri = Uri.parse("tel:" + phone);
						Intent intent = new Intent(Intent.ACTION_CALL, uri);
						startActivity(intent);
						// 写到sqlite
						//true 这个超链接被处理了，
						return true;
					}
					return super.shouldOverrideUrlLoading(view, url);
				}
			});
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
