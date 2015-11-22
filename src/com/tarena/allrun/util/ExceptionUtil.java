package com.tarena.allrun.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.tarena.allrun.TApplication;
/**
 * 异常统一处理
 * @author tarena
 *
 */
public class ExceptionUtil {
	public static void handleException(Exception e)
	{
		if (TApplication.isRelease)
		{
		//把异常信息变成字符串
			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String string=stringWriter.toString();
			
		
		//发邮件，发到服务器
			LogUtil.i("", string);
		}else
		{
			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String string=stringWriter.toString();
			LogUtil.i("异常信息必须看", "  ");
			LogUtil.i("异常信息必须看", "  ");
			LogUtil.i("异常信息必须看", string);
			e.printStackTrace();
		}
	}

}
