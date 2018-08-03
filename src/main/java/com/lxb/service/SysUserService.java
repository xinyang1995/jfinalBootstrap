package com.lxb.service;

import com.lxb.entity.SysUser;
import com.lxb.util.MD5Util;

/**
 * 系统用户service层
 * 
 * @since 2018年8月1日 下午12:00:15
 * @author LXB
 */
public class SysUserService {

	private static volatile SysUserService service;

	private SysUserService() {
	}

	public static SysUserService getInstance() {
		if (null == service) {
			synchronized (SysUserService.class) {
				if (null == service) {
					service = new SysUserService();
				}
			}
		}
		return service;
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @since 2018年8月1日 下午12:00:08
	 * @author LXB
	 */
	public SysUser getSysUser(String userName, String password) {
		String sql = "select * from sys_user where userName='" + userName
				+ "' and password='" + MD5Util.MD5Encode(password) + "'";
		SysUser sysUser = SysUser.dao.findFirst(sql);
		return sysUser;
	}

}
