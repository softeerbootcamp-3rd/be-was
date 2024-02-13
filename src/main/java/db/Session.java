package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    private static Map<String, String> sessionData = new HashMap<>();

    // 세션 만료 시간
    private static final long SESSION_EXPIRATION_TIME = 10 * 60 * 1000;

    static {
        // 주기적으로 세션 만료 확인 및 제거 스케줄링
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(Session::removeExpiredSessions, 0, 10, TimeUnit.MINUTES);
    }

    private Session() {}

    public static String createSession(String userId) {
        logger.debug("createSession() method");
        String sessionId = UUID.randomUUID().toString();
        sessionData.put(sessionId, userId);
        logger.debug("sessionId : {}", sessionId);
        logger.debug("userId : {}", userId);
        showAllSessions();

        return sessionId;
    }

    public static void removeSession(String sessionId) {
        logger.debug("removeSession()");
        sessionData.remove(sessionId);
        showAllSessions();
    }

    public static String getUserIdBySessionId(String sessionId) {
        logger.debug("getUserIdBySessionId()");
        return sessionData.get(sessionId);
    }

    public static Collection<String> getAllSessionId() {
        logger.debug("getAllSessions()");
        showAllSessions();
        return new ArrayList<>(sessionData.keySet());
    }

    public static void showAllSessions() {
        logger.debug("showAllSessions()");
        for (Map.Entry<String, String> entry : sessionData.entrySet()) {
            String sessionId = entry.getKey();
            String userId = entry.getValue();
            logger.debug("Session ID: {}, User ID: {}", sessionId, userId);
        }
    }

    private static void removeExpiredSessions() {
        logger.debug("Removing expired sessions");
        Iterator<Map.Entry<String, String>> iterator = sessionData.entrySet().iterator();
        long currentTime = System.currentTimeMillis();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String sessionId = entry.getKey();
            long sessionCreationTime = Long.parseLong(sessionId.split("-")[0], 16); // Extract creation time from session ID

            if (currentTime - sessionCreationTime > SESSION_EXPIRATION_TIME) {
                logger.debug("Expired session removed: {}", sessionId);
                iterator.remove();
            }
        }

        showAllSessions();
    }
}
