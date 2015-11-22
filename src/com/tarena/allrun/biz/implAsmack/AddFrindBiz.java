package com.tarena.allrun.biz.implAsmack;

import org.jivesoftware.smack.Roster;

import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;

public class AddFrindBiz extends IntentService{

	public AddFrindBiz() {
		super("AddFrindBiz");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int status=-1;
		try {
			UserEntity userEntity=(UserEntity) intent.getSerializableExtra(Const.KEY_DATA);
			
			String[] groups=new String[]{userEntity.getGroup()};
			
			//������,�ŵ��Ǻ��ѷ���ͺ�����Ϣ
			Roster roster=TApplication.xmppConnection.getRoster();
			roster.createEntry(userEntity.getUser(), userEntity.getName(), groups);
			status=Const.STATUS_OK;
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}finally
		{
			try {
				//�����⣺��service���������activity
				//1,�÷��㲥
				//2,��pendingintent
				PendingIntent pendingIntent=intent.getParcelableExtra("pendingIntent");
				Intent intentToActivity=new Intent();
				intentToActivity.putExtra(Const.KEY_DATA, status);
				pendingIntent.send(this, 200, intentToActivity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}
