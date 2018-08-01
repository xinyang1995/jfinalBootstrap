/** 广州哇宝信息技术有限公司 */
package com.lxb.entity;

import com.jfinal.plugin.activerecord.Model;
import com.lxb.annotation.TableBind;

/**
 * 后台系统操作痕迹记录
 * 
 * @since 2018年4月17日 上午9:36:57
 * @author LXB
 */
@TableBind(tableName = "operate_log")
public class OperateLog extends Model<OperateLog> {
	private static final long serialVersionUID = 1L;
	// 全类共享常量Dao
	public static final OperateLog dao = new OperateLog();
}
