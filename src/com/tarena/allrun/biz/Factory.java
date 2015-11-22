package com.tarena.allrun.biz;

import com.tarena.allrun.biz.implAsmack.GroupChatBiz;
import com.tarena.allrun.biz.implAsmack.LoginBiz;

public class Factory {
public static ILoginBiz getLoginBizInstance()
{
	return new LoginBiz();
}

public static IGroupChatBiz getGroupChatBizInstance()
{
	return new GroupChatBiz();
	}
}
