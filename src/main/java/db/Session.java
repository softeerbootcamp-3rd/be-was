package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    private static Map<String, String> sessionData = new HashMap<>();

    public static String createSession(String userId) {
        logger.debug("createSession() method");
        String sessionId = UUID.randomUUID().toString();
        sessionData.put(sessionId, userId);
        logger.debug("sessionId : " + sessionId);
        logger.debug("userId : " + userId);

        return sessionId;
    }

    public static String getUserIdBySessionId(String sessionId) {
        logger.debug("getUserIdBySessionId()");
        return sessionData.get(sessionId);
    }
}
