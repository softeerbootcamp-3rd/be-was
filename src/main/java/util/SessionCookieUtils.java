package util;

import session.SessionManager;

public class SessionCookieUtils {

	public static String getSessionId(String[] cookies) {
		for (String cookie : cookies) {
			if (isCookieValid(cookie)) { // SID 키 값이고 서버에 그 값이 있는 경우
				return getSessionIdFromCookie(cookie);
			}
		}
		return null; // 서버에 저장된 세션이 없는 경우
	}

	private static boolean isCookieValid(String cookie) {
		String targetKey = "SID";
		String[] keyValue = cookie.trim().split("=");
		String key = keyValue[0];
		String value = keyValue[1];
		return targetKey.equals(key) && SessionManager.containsSession(value);
	}

	private static String getSessionIdFromCookie(String cookie) {
		String[] keyValue = cookie.trim().split("=");
		return keyValue[1];
	}
}
