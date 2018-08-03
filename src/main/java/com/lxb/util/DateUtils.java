/** 广州哇宝信息技术有限公司 */
package com.lxb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日起工具类
 * 
 * @since 2015年12月17日 上午10:10:03
 * @author hjielong
 */
public class DateUtils {
	private static Logger log = LoggerFactory.getLogger(DateUtils.class);
	public static final long NW = 7 * 1000 * 24 * 60 * 60;// 一周的毫秒数
	public static final long ND = 1000 * 24 * 60 * 60;// 一天的毫秒数
	public static final long NH = 1000 * 60 * 60;// 一小时的毫秒数
	public static final long NM = 1000 * 60;// 一分钟的毫秒数
	public static final long NS = 1000;// 一秒钟的毫秒数

	private static DateUtils dateUtils = null;

	/**
	 * 实例化单例模式(前台页面有时候需要调用)
	 * 
	 * @return
	 * @since 2017年2月15日 下午12:46:24
	 */
	public static DateUtils getInstance() {
		if (null == dateUtils) {
			dateUtils = new DateUtils();
		}
		return dateUtils;
	}

	/**
	 * 得到整形日期
	 * 
	 * @param date
	 * @return
	 * @since 2016年4月26日 下午3:36:33
	 */
	public static int getDateInt(Date date) {
		String intStr = format(date, "yyyyMMdd");
		return new Integer(intStr);
	}

	/**
	 * 根据默认格式把日期字符串转换为日期时间类型（"yyyy-MM-dd HH:mm:ss"）
	 * 
	 * @param source
	 *            字符型日期
	 * @return Date 日期
	 */
	public static Date stringToDateTime(String source) {
		return stringToDateTime(source, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据指定格式把日期字符串转换为日期类型
	 * 
	 * @param source
	 *            字符型日期
	 * @param format
	 *            格式（"yyyy-MM-dd HH:mm:ss"）
	 * @return Date 日期
	 */
	public static Date stringToDateTime(String source, String format) {
		Date date = null;
		try {
			final DateFormat df = new SimpleDateFormat(format);
			ParsePosition pp = new ParsePosition(0);
			date = df.parse(source, pp);
		} catch (final Exception e) {
			log.debug("转换失败.<source=" + source + ">", e);
		}
		return date;
	}

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @author PCY
	 * @date 2014年8月22日
	 */
	public static String format(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String format(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static Date parse(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parse(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把日期格式化为制定格式字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式（yyyy-MM-dd HH:mm:ss）
	 * @return String 字符型日期
	 */
	public static String dateToString(String date, String format) {
		String result = "";
		try {
			if (date != null) {
				DateFormat df = new SimpleDateFormat(format);
				result = df.format(stringToDateTime(date, format));
			}
		} catch (Exception e) {
			log.debug("转换失败.<date=" + date + ">");
			result = "";
		}
		return result;
	}

	/**
	 * 把日期格式化为制定格式字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式（yyyy-MM-dd HH:mm:ss）
	 * @return String 字符型日期
	 */
	public static String dateToString(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				DateFormat df = new SimpleDateFormat(format);
				result = df.format(date);
			}
		} catch (Exception e) {
			log.debug("转换失败.<date=" + date + ">");
			result = "";
		}
		return result;
	}

	/**
	 * 获取两个时间的小时差
	 * 
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @author PCY
	 * @date 2014年8月22日
	 */
	public static Long diffHour(Date minDate, Date maxDate) {
		long diff = maxDate.getTime() - minDate.getTime();
		long day = diff / ND;// 计算差多少天
		long hour = diff % ND / NH + day * 24;// 计算差多少小时
		return hour;
	}

	/**
	 * 获得两个日期间的差值(秒)
	 * 
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @author PCY
	 * @date 2014年8月25日
	 */
	public static long diffSecond(Date minDate, Date maxDate) {
		long min = minDate.getTime();
		long max = maxDate.getTime();
		return (max - min) / NS;
	}

	/**
	 * 获得两个日期间的差值(秒)
	 * 
	 * @param minTime
	 *            小的时间
	 * @param maxTime
	 *            大的时间
	 * @return
	 * @since 2015年3月11日 下午8:05:26
	 * @author jia li
	 */
	public static int diffSecond(long minTime, long maxTime) {
		return (int) ((maxTime - minTime) / NS);
	}

	/**
	 * 判断指定日期是不是前些天
	 * 
	 * @param d
	 * @return
	 * @author PCY
	 * @date 2013-9-13
	 */
	public static boolean isPreDay(Date d) {
		if (d == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		int date = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(d);
		int pyear = cal.get(Calendar.YEAR);
		int pdate = cal.get(Calendar.DAY_OF_YEAR);
		if (pyear < year || (pyear == year && pdate < date))
			return true;
		return false;
	}

	/**
	 * 两个日期是否为同一天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		boolean syear = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
		boolean sday = c1.get(Calendar.DAY_OF_YEAR) == c2
				.get(Calendar.DAY_OF_YEAR);
		return syear && sday;
	}

	/**
	 * 根据日期获得秒数
	 * 
	 * @param date
	 * @return
	 * @author PCY
	 * @date 2014年8月26日
	 */
	public static long getDateSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis() / NS;
	}

	/**
	 * 比较开始时间与结束时间的时间间隔是否在给定间隔天数内
	 * 
	 * @param startTime
	 * @param endTime
	 * @param dayLimit
	 *            间隔（天）
	 * @author jia li
	 */
	public static boolean isInTheTime(long startTime, long endTime,
			int dayLimit) {
		if ((startTime + dayLimit * 24 * 60 * 60 * 1000l) > endTime) {
			return true;
		}
		return false;
	}

	/**
	 * 获取两个Date对象之间的天数
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            截止日期
	 * @return
	 */
	public static int getDays(Date startDate, Date endDate) {
		if (startDate == null) {
			startDate = new Date();
		}
		long start = startDate.getTime();
		long end = endDate.getTime();
		int diffDays = (int) ((end - start) / ND);
		return diffDays;
	}

	/**
	 * 计算两个日期之间相隔的天数
	 * 
	 * @author guobangli
	 * @data 2015年1月8日 下午2:23:57
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int diffDays(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long start = calendar.getTime().getTime();

		calendar.setTime(endDate);
		long end = calendar.getTime().getTime();
		int diffDays = (int) ((end - start) / ND);

		return diffDays;
	}

	public static int diffDays(String startDate, String endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(startDate, "yyyyMMdd"));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long start = calendar.getTime().getTime();

		calendar.setTime(parse(endDate, "yyyyMMdd"));
		long end = calendar.getTime().getTime();
		int diffDays = (int) ((end - start) / ND);

		return diffDays;
	}

	public static int diffDays(long start, long end) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		int diffDays = (int) ((end - calendar.getTimeInMillis()) / ND);
		return diffDays;
	}

	/**
	 * 获取指定日期经过多少天后的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getAssignDate(Date date, int days) {
		if (date == null) {
			date = new Date();
		}
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		return now.getTime();
	}

	/**
	 * 获取时间的小时数
	 * 
	 * @param date
	 * @return
	 * @author PCY
	 * @date 2014年9月17日
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取时间的分钟数
	 * 
	 * @param date
	 * @return
	 * @author PCY
	 * @date 2014年9月17日
	 */
	public static int getMimute(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		String format = sdf.format(date);
		return Integer.parseInt(format);
	}

	/**
	 * 获取时间的秒数
	 * 
	 * @param date
	 * @return
	 * @author PCY
	 * @date 2014年9月17日
	 */
	public static int getSecond(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("ss");
		String format = sdf.format(date);
		return Integer.parseInt(format);
	}

	/**
	 * 增加天单位时间
	 * 
	 * @param date
	 * @param second
	 * @return
	 * @author PCY
	 * @date 2014年12月18日
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 增加小时单位时间
	 * 
	 * @param date
	 * @param hour
	 * @return
	 * @author PCY
	 * @date 2014年9月17日
	 */
	public static Date addHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}

	/**
	 * 增加秒单位时间
	 * 
	 * @param date
	 * @param second
	 * @return
	 * @author PCY
	 * @date 2014年12月18日
	 */
	public static Date addSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 获取今天星期几 一二三四五六日对应：1234567
	 * 
	 * @return
	 */
	public static int getTodayWeekOfDay() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return day == 1 ? 7 : (day - 1);
	}

	public static int getWeekOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return day == 1 ? 7 : (day - 1);
	}

	/**
	 * 获取某天对应周的周日
	 * 
	 * @param date
	 * @return
	 * @since 2016年4月20日 下午5:51:12
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day = day == 1 ? 7 : (day - 1);
		calendar.add(Calendar.DATE, 7 - day);
		return calendar.getTime();

	}

	/**
	 * 获取某天对应周的周一
	 * 
	 * @param date
	 * @return
	 * @since 2016年4月20日 下午5:51:12
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day = day == 1 ? 7 : (day - 1);
		calendar.add(Calendar.DATE, 1 - day);
		return calendar.getTime();

	}

	/**
	 * 得到每天指定整点毫秒
	 * 
	 * @author guobangli
	 * @data 2014年11月13日 上午11:03:38
	 * @return
	 */
	public static long getHourTime(int hour) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}

	/**
	 * 获取指定时间的零点
	 * 
	 * @param date
	 * @return
	 * @since 2016年2月20日 下午2:13:13
	 */
	public static Date getDateZero(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 获取当前时间的接下来的整点
	 * 
	 * @author guobangli
	 * @data 2014年11月26日 上午11:36:15
	 * @param hour
	 * @return
	 */
	public static Date getDateHour(int hour) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 判断是否在时间内
	 * 
	 * @param minDate
	 * @param maxDate
	 * @param judge
	 * @return
	 * @author PCY
	 * @date 2014年12月18日
	 */
	public static boolean isBetween(Date minDate, Date maxDate, Date judge) {
		if (minDate == null || maxDate == null || judge == null) {
			return false;
		}
		if (minDate.after(maxDate))
			return false;
		if (judge.equals(minDate))
			return true;
		return judge.after(minDate) && judge.before(maxDate);
	}

	/**
	 * 得到今天对应时间
	 * 
	 * @author guobangli
	 * @data 2015年1月12日 上午10:06:48
	 * @param hour
	 * @param mimute
	 * @param second
	 * @return
	 */
	public static Date getTodayTime(int hour, int mimute, int second) {
		if (hour > 23)
			hour = 0;
		if (mimute > 59)
			mimute = 0;
		if (second > 59)
			second = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, mimute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 判断是不是同一个月
	 * 
	 * @param millTime
	 *            毫秒值
	 * @return
	 * @since 2015年4月1日 下午5:29:22
	 * @author LSQ
	 */
	public static boolean isDiffMonth(Date date) {
		if (date == null) {
			return false;
		}
		SimpleDateFormat sdfs = new SimpleDateFormat("MM");
		String hisMonth = sdfs.format(date);
		String currMonth = sdfs.format(new Date());

		if (Integer.parseInt(hisMonth) != Integer.parseInt(currMonth)) {
			return true;
		}
		return false;
	}

	/**
	 * 设置今天的时间
	 * 
	 * @param hour
	 * @param mimute
	 * @return
	 * @since 2015年10月28日 下午2:34:53
	 * @author PCY
	 */
	public static Date setTodayTime(int hour, int mimute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, mimute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取月份
	 * 
	 * @return
	 * @since 2015年10月26日 下午3:54:20
	 * @author LSQ
	 */
	public static int getMonth() {
		SimpleDateFormat sdfs = new SimpleDateFormat("MM");
		String month = sdfs.format(new Date());
		return Integer.parseInt(month);
	}

	/**
	 * 获取今天是这个月的几号
	 * 
	 * @return
	 * @since 2015年10月26日 下午6:06:33
	 * @author LSQ
	 */
	public static int getTodayMonthOfDay() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param addMonth
	 * @param formatStr
	 * @return
	 * @since 2015年10月26日 下午6:06:49
	 * @author LSQ
	 */
	public static String getLastMonthOfDay(int addMonth, String formatStr) {
		Calendar calendar = Calendar.getInstance();
		int MM = Integer.parseInt(DateUtils.format(new Date(), "MM"));
		calendar.set(Calendar.MONTH, MM - 1 + addMonth);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		DateFormat format = new SimpleDateFormat(formatStr);
		return format.format(calendar.getTime());
	}

	/**
	 * 获取最大的日期
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 * @since 2015年11月4日 下午4:40:50
	 * @author PCY
	 */
	public static Date getMax(Date d1, Date d2) {
		if (d1 == null && d2 == null)
			return null;
		if (d1 == null)
			return d2;
		if (d2 == null)
			return d1;
		return d1.getTime() > d2.getTime() ? d1 : d2;
	}

	/**
	 * 获取今周的日期
	 * 
	 * @param d
	 *            周几
	 * @param h
	 *            小时
	 * @param m
	 *            分钟
	 * @param s
	 *            秒
	 * @return
	 * @since 2015年11月28日 下午6:00:05
	 * @author PCY
	 */
	public static Date getDayOfWeek(int d, int h, int m, int s) {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0)
			dayOfWeek = 7;
		calendar.add(Calendar.DATE, -dayOfWeek + d);
		calendar.set(Calendar.HOUR_OF_DAY, h);
		calendar.set(Calendar.MINUTE, m);
		calendar.set(Calendar.SECOND, s);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 得到指定天的指定时间
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 * @since 2015年12月8日 下午5:45:45
	 */
	public static Date getDayAssignTime(Date date, int hour, int minute,
			int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前日期所属的日志表(新项目)
	 * 
	 * @param date
	 * @return
	 * @since 2016年4月1日 下午4:02:51
	 */
	public static int getLogIdx(Date date) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return Integer.valueOf(dateFormat.format(date));
	}

	/**
	 * 获取当前日期的前n天的所属日志表
	 * 
	 * @return
	 */
	public static int getBeforeLogIdx(Date date, int n) {
		int tmp = getLogIdx(date);
		tmp = tmp - n - 1;

		if (tmp <= 0) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, 0);
			cal.add(Calendar.DAY_OF_MONTH, -1);

			tmp = cal.get(Calendar.DAY_OF_MONTH) + tmp;
		}
		return tmp;
	}

	/**
	 * 判断是否为月尾最后一天
	 * 
	 * @param date
	 * @return
	 * @since 2016年4月3日 上午12:28:04
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	public static Date getStartDayOfMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getCurrStartDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 秒转换为 day:hour:minute:second
	 * 
	 * @return String
	 * @param time
	 *            (s)
	 * @author wmingkai
	 * 
	 */
	public static String secToTime(int time) {
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int temp = 0;
		if (time == 0) {
			return day + ":" + hour + ":" + minute + ":" + second;
		} else {
			day = time / (24 * 60 * 60);
			temp = time % (24 * 60 * 60);
			if (temp > 0) {
				hour = temp / (60 * 60);
				temp = temp % (60 * 60);
				if (temp > 0) {
					minute = temp / (60);
					temp = temp % (60);
					if (temp > 0) {
						second = temp;
						return day + ":" + hour + ":" + minute + ":" + second;
					} else {
						return day + ":" + hour + ":" + minute + ":" + second;
					}
				} else {
					return day + ":" + hour + ":" + minute + ":" + second;
				}
			} else {
				return day + ":" + hour + ":" + minute + ":" + second;
			}
		}
	}

	/**
	 * 根据时间获取该时间所在的月份最大天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayMaxOfDate(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		int dayOfMonth = gCalendar
				.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		return dayOfMonth;
	}

	public static int diffMonths(Date date1, Date date2) {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);
		c2.setTime(date2);

		result = c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH);

		return result == 0 ? 0 : Math.abs(result);
	}

	public static Date addMonths(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * 一天的开始点
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 一天的结束点
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static String getStringDateFmt2Fmt(String dateStr, String oldFmt,
			String newFmt) {
		Date d = parse(dateStr, oldFmt);
		return format(d, newFmt);
	}

	/**
	 * 2个时间是不是同一分钟
	 * 
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameMinute(Date date1, Date date2) {
		if (Math.abs(date1.getTime() - date2.getTime()) > NM) {
			return false;
		} else {
			return true;
		}
	}

	public static int addDayInt(int dateInt, int days) {
		Date d = parse("" + dateInt, "yyyyMMdd");
		Date resultDay = addDay(d, days);
		String intStr = format(resultDay, "yyyyMMdd");
		return new Integer(intStr);
	}

	public static Date getMillSecBeginTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getMillSecEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 999);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getDateFromLong(long time) {
		return new Date(time);
	}

	public static String getRemainInfo(long time) {
		long d = time / ND;
		long h = (time - d * ND) / NH;
		long m = (time - d * ND - h * NH) / NM;
		long s = (time - d * ND - h * NH - m * NM) / NS;
		StringBuilder sb = new StringBuilder();
		sb.append("剩余" + d + "天");
		sb.append(h + "小时");
		sb.append(m + "分钟");
		sb.append(s + "秒");
		return sb.toString();
	}

	/**
	 * 计算两个日期相差多少周
	 * 
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @since 2018年7月5日 下午3:04:27
	 * @author LXB
	 */
	public static long diffWeek(Date minDate, Date maxDate) {
		return (maxDate.getTime() - minDate.getTime())
				/ (7 * 24 * 60 * 60 * 1000) + 1;
	}
}
