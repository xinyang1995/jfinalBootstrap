/** 广州哇宝信息技术有限公司 */
package com.lxb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http请求工具类
 * 
 * @since 2018年4月17日 上午9:14:34
 * @author LXB
 */
public class HttpUtil {

	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	/** 设置请求和传输超时时间 */
	static int connect_timeout = 10 * 60 * 1000;
	/** 定义编码格式 UTF-8 */
	public static final String UTF8 = "UTF-8";

	private HttpUtil() {
	}

	public static String post(String urlStr, String body) {
		String response = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "wabao");
			conn.setReadTimeout(connect_timeout);
			conn.setConnectTimeout(connect_timeout);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.connect();

			conn.getOutputStream().write(body.getBytes("UTF-8"));
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			in.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return response;
	}

	public static String toGet(String url) throws IOException {
		// http 请求方法
		String method = "GET";

		// 发起http连接
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod(method);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.connect();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String temp = null;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		br.close();
		return sb.toString();
	}

	public static String getAllRequestParams(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder("{");
		Map<?, ?> allParams = request.getParameterMap();
		for (Entry<?, ?> entry : allParams.entrySet()) {
			String key = (String) entry.getKey();
			StringBuilder value = new StringBuilder("");
			Object[] values = (Object[]) entry.getValue();
			if (null == values || 0 == values.length) {
				continue;
			}
			for (Object object : values) {
				value.append(object.toString()).append(",");
			}
			value = value.deleteCharAt(value.length() - 1);
			sb.append("\"" + key + "\":").append("\"" + value.toString() + "\"")
					.append(",");
		}
		if (sb.length() > 2) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}

	public static Map<String, Object> getRequestMap(
			Map<String, String[]> reqMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (null == reqMap || 0 == reqMap.size()) {
			return resultMap;
		}
		for (Entry<String, String[]> entry : reqMap.entrySet()) {
			resultMap.put(entry.getKey(), entry.getValue()[0]);
		}
		return resultMap;
	}
}