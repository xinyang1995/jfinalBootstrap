package com.lxb.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsUtil {

	private static final Logger log = LoggerFactory.getLogger(LogsUtil.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static ThreadLocal<Map<String, Date>> threadMap = new ThreadLocal();

	public static <T> void info(Class<T> clazz, String message) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.info(message);
	}

	public static <T> void info(Class<T> clazz, String message, Throwable t) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.info(message, t);
	}

	public static <T> void debug(Class<T> clazz, String message) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.debug(message);
	}

	public static <T> void error(Class<T> clazz, String message) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.error(message);
	}

	public static <T> void error(Class<T> clazz, String message, Exception ex) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.error(message, ex);
	}

	public static <T> void logCurrentTime(String key, boolean isStart,
			Class<T> cls) {
		if (!log.isInfoEnabled())
			return;

		Map timeMap = (Map) threadMap.get();
		if (timeMap == null) {
			timeMap = new HashMap();
			threadMap.set(timeMap);
		}

		String mapKey = cls.getName() + ":" + key;
		Date curDate = new Date();
		if (isStart) {
			timeMap.put(mapKey, curDate);
		} else {
			Date lastDate = (Date) timeMap.get(mapKey);
			if (lastDate != null) {
				timeMap.remove(mapKey);
				StringBuilder logInfo = new StringBuilder("=========");

				logInfo.append("，（位置：").append(cls.getName());
				logInfo.append("，内容：").append(key).append(")");
				logInfo.append("，间隔：")
						.append(curDate.getTime() - lastDate.getTime());
				logInfo.append(",开始start：").append(sdf.format(lastDate));
				logInfo.append("，结束end：").append(sdf.format(curDate))
						.append("=========");

				log.info(logInfo.toString());
				threadMap.remove();
			}
		}
	}

}
