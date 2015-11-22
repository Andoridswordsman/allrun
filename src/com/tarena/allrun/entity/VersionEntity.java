package com.tarena.allrun.entity;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class VersionEntity implements Serializable {
	public String status;
	public String apkUrl;
	public String msg;
	public String changeLog;
	public String version;

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getApkUrl() {
		return this.apkUrl;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

	public String getChangeLog() {
		return this.changeLog;
	}

}
