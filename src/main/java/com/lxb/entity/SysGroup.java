/** 广州哇宝信息技术有限公司 */
package com.lxb.entity;

import com.jfinal.plugin.activerecord.Model;
import com.lxb.annotation.TableBind;

/**
 * 系统用户组
 * 
 * @since 2018年4月17日 上午9:36:47
 * @author LXB
 */
@TableBind(tableName = "sys_group")
public class SysGroup extends Model<SysGroup> {
	private static final long serialVersionUID = 1L;
	public static final int GROUP_SUPER_ADMIN = 0;// 超级管理员组,一般系统初始化的时候新增,整个系统只会存在一个超管
	public static final int TYPE_NORMAL_ADMIN = 1;// 普通管理员组
	public static final int TYPE_NORMAL_USER = 2;// 普通用户组
	// 全类共享常量Dao
	public static final SysGroup dao = new SysGroup();

}
