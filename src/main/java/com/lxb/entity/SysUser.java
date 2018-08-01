package com.lxb.entity;

import com.jfinal.plugin.activerecord.Model;
import com.lxb.annotation.TableBind;

/**
 * 系统用户
 * 
 * @since 2018年4月17日 上午9:27:23
 * @author LXB
 */
@TableBind(tableName = "sys_user")
public class SysUser extends Model<SysUser> {

	private static final long serialVersionUID = 1L;
	public static final int STATUS_NORMAL = 0;// 正常状态
	public static final int STATUS_LOCKED = 1;// 锁定状态状态
	// 全类共享常量Dao
	public static final SysUser dao = new SysUser();

}
