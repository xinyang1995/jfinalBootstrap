package com.lxb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class StringUtils {

	private static StringUtils stringUtils = null;

	/**
	 * 实例化单例模式(前台页面有时候需要调用)
	 * 
	 * @return
	 * @since 2017年2月15日 下午12:46:24
	 */
	public static StringUtils getInstance() {
		if (null == stringUtils) {
			stringUtils = new StringUtils();
		}
		return stringUtils;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String getListString(List<Long> array) {
		if (null == array || array.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Long l : array) {
			sb.append(l.longValue()).append(",");
		}
		if (sb.length() > 1) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String getArrayString(String[] array) {
		if (null == array || 0 == array.length) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			sb.append(array[i]).append(",");
		}
		if (sb.length() > 1) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static List<Integer> getCidList(String[] array) {
		if (null == array || 0 == array.length) {
			return new ArrayList<Integer>();
		}
		List<Integer> resultList = new ArrayList<Integer>();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			resultList.add(new Integer(array[i]));
		}
		return resultList;
	}

	public static String getArrayString(List<String> list) {
		if (null == list || 0 == list.size()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (i < length - 1) {
				sb.append(list.get(i)).append(",");
			} else {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 把数组字符串组合成字符串，以split分割
	 * 
	 * @param stringArray
	 * @param split
	 * @return
	 */
	public static String splicing(String[] stringArray, String split) {
		StringBuilder sb = new StringBuilder();
		int length = stringArray.length;

		for (int i = 0; i < length; i++) {
			sb.append(stringArray[i]).append(split);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static boolean isMac(String mac) {
		if (isEmpty(mac))
			return false;
		mac = mac.replace(":", "-");
		return mac.matches(
				"[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}");
	}

	public static boolean isIp(String ipAddress) {
		String ip = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress.trim());
		return matcher.matches();
	}

	public static int long2Int(Long l) {
		if (null == l) {
			return 0;
		}
		int result = new Integer(String.valueOf(l));
		return result;
	}

	public static int double2Int(Double d) {
		if (null == d) {
			return 0;
		}
		int result = d.intValue();
		return result;
	}

	public static String getCollectionString(Collection<?> collection) {
		if (null == collection || 0 == collection.size()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object string : collection) {
			sb.append(String.valueOf(string)).append(",");
		}
		if (sb.length() > 1) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNumber(String num) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(num).matches();
	}

	public static void deleteFile(File file) {
		if (null == file || !file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (null == files[i] || !files[i].exists()) {
					continue;
				}
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	public static Set<String> getAuthSet(String pidStr) {
		Set<String> authSet = new HashSet<String>();
		if (isEmpty(pidStr)) {
			return authSet;
		}
		String[] pids = pidStr.split(",");
		if (null == pids || 0 == pids.length) {
			return authSet;
		}
		authSet.addAll(Arrays.asList(pids));
		return authSet;
	}

	public static boolean hasAuth(Set<String> authSet, int pid) {
		return authSet.contains("" + pid);
	}

	private static final String[] PROXY_REMOTE_IP_ADDRESS = { "X-Forwarded-For",
			"X-Real-IP" };

	/**
	 * <p>
	 * 获取请求的客户端的 IP 地址。若应用服务器前端配有反向代理的 Web 服务器， 需要在 Web 服务器中将客户端原始请求的 IP 地址加入到
	 * HTTP header 中。 详见 {@link #PROXY_REMOTE_IP_ADDRESS}
	 * </p>
	 */
	public static String getRemoteIp(HttpServletRequest request) {
		for (int i = 0; i < PROXY_REMOTE_IP_ADDRESS.length; i++) {
			String ip = request.getHeader(PROXY_REMOTE_IP_ADDRESS[i]);
			if (isNotEmpty(ip)) {
				return getRemoteIpFromForward(ip.trim());
			}
		}
		return request.getRemoteHost();
	}

	/**
	 * <p>
	 * 从 HTTP Header 中截取客户端连接 IP 地址。如果经过多次反向代理， 在请求头中获得的是以“,&lt;SP&gt;”分隔 IP
	 * 地址链，第一段为客户端 IP 地址。
	 * </p>
	 * 
	 * @param xforwardIp
	 *            从 HTTP 请求头中获取转发过来的 IP 地址链
	 * @return 客户端源 IP 地址
	 */
	private static String getRemoteIpFromForward(String xforwardIp) {
		int commaOffset = xforwardIp.indexOf(',');
		if (commaOffset < 0) {
			return xforwardIp;
		}
		return xforwardIp.substring(0, commaOffset);
	}
}
