package com.tarena.allrun.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.tarena.allrun.TApplication;
/**
 * �쳣ͳһ����
 * @author tarena
 *
 */
public class ExceptionUtil {
	public static void handleException(Exception e)
	{
		if (TApplication.isRelease)
		{
		//���쳣��Ϣ����ַ���
			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String string=stringWriter.toString();
			
		
		//���ʼ�������������
			LogUtil.i("", string);
		}else
		{
			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String string=stringWriter.toString();
			LogUtil.i("�쳣��Ϣ���뿴", "  ");
			LogUtil.i("�쳣��Ϣ���뿴", "  ");
			LogUtil.i("�쳣��Ϣ���뿴", string);
			e.printStackTrace();
		}
	}

}
