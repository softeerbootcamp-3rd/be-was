package webserver.http;

import model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionManager {

    private static class Session {
        private LocalDateTime createdAt;
        private User user;

        public Session(LocalDateTime createdAt, User user) {
            this.createdAt = createdAt;
            this.user = user;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public User getUser() {
            return user;
        }
    }

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static String createSession(User user) {
        String randomKey = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        sessionMap.put(randomKey, new Session(createdAt, user));

        // 현재 시간이랑 비교하여 5분을 초과한 만료된 세션 제거
        List<String> expired = sessionMap.entrySet().stream()
                .filter(entry -> entry.getValue().getCreatedAt().plusMinutes(5).isBefore(createdAt))
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        expired.forEach(
                key -> sessionMap.remove(key)
        );

        return randomKey;
    }

    private static User findUserBySessionKey(String key) {
        Session session = sessionMap.getOrDefault(key, null);
        if (session == null)
            return null;

        LocalDateTime now = LocalDateTime.now();

        if (session.getCreatedAt().plusMinutes(5).isAfter(now))
            return session.getUser();

        return null;
    }

    public static User findUserByRequest(HttpRequest request) {
        try {
            String sid = request.getCookie().getOrDefault("sid", "");
            return findUserBySessionKey(sid);
        } catch (IOException e) {
            return null;
        }
    }
}
