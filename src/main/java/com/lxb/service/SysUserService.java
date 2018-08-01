package com.lxb.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SysUserService {

	public String getUser(Integer id) {
		Record record = Db.findById("sys_user", id);
		return record.get("userName");
	}

}
