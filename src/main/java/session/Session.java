package session;

import exception.BadRequestException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    public static ConcurrentHashMap<UUID, UserSession> loginUsers = new ConcurrentHashMap<>();

    public static synchronized UUID login(User user) {
        UserSession userSession = new UserSession(user.getUserId());
        if(loginUsers.containsValue(userSession)){
            throw new BadRequestException("이미 로그인 한 유저입니다.");
        }
        UUID sessionId = UUID.randomUUID();
        loginUsers.put(sessionId, userSession);
        logger.debug("유저 로그인 완료 + " + userSession.userId + sessionId);
        return sessionId;
    }

    public static boolean loginCheck(UUID sessionId) {
        return loginUsers.containsKey(sessionId);
    }

    public static class UserSession {
        private final String userId;

        public UserSession(String userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserSession that = (UserSession) o;
            return Objects.equals(this.userId, that.userId);
        }
    }
}
