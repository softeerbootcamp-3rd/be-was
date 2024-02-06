package util.session;

import constant.HttpHeader;
import model.User;
import util.web.RequestParser;
import webserver.HttpRequest;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private static final Map<String, SessionInfo> sessionMap = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        scheduler.scheduleAtFixedRate(SessionManager::cleanupExpiredSessions, 0, 1, TimeUnit.MINUTES);
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void addSession(String sessionId, User user) {
        sessionMap.put(sessionId, new SessionInfo(user, System.currentTimeMillis()));
    }

    public static User getLoggedInUser(HttpRequest request) {
        String sid = RequestParser.parseCookie(request.getHeader().get(HttpHeader.COOKIE)).get("SID");
        return getUserBySessionId(sid);
    }

    public static User getUserBySessionId(String sessionId) {
        if (sessionId == null) return null;

        SessionInfo sessionInfo = sessionMap.get(sessionId);
        if (sessionInfo == null) return null;

        sessionInfo.setLastAccessTime(System.currentTimeMillis());
        return sessionInfo.getUser();
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    private static void cleanupExpiredSessions() {
        long currentTimestamp = System.currentTimeMillis();
        sessionMap.entrySet().removeIf(entry -> {
            SessionInfo sessionInfo = entry.getValue();
            long lastAccessTime = sessionInfo.getLastAccessTime();
            // 세션 만료 시간 (10분)
            long sessionExpirationTime = 10 * 60 * 1000;
            return (currentTimestamp - lastAccessTime) > sessionExpirationTime;
        });
    }


}