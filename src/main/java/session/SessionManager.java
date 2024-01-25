package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

	private static Map<String, String> sessionIdMap = new ConcurrentHashMap<>();

	public static String generateSessionId(String userId) {
		// UUID 난수를 통해 sessionId 생성
		String sessionId = UUID.randomUUID().toString();
		sessionIdMap.put(sessionId, userId);

		return sessionId;
	}

	public static String getUserIdBySessionId(String sessionId) {
		return sessionIdMap.get(sessionId);
	}
}
