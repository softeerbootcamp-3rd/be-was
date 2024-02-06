package session;

import model.User;
import request.HttpRequest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static Map<String, User> sessionRepository = new ConcurrentHashMap<>();

    // 로그인 성공한 유저에게 세션 id 발급
    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionRepository.put(sessionId, user);
        return sessionId;
    }

    // 세션 아이디로 유저 조회
    public User getUserBySessionId(HttpRequest request) {
        String cookie = request.getHeaders().get("Cookie");
        if (cookie != null) {
            String[] tokens = cookie.split("=");
            return sessionRepository.get(tokens[1]);
        }
        return null;
    }

    // 로그아웃 처리 용도
    public void deleteSession(HttpRequest request) {
        String cookie = request.getHeaders().get("Cookie");
        String[] tokens = cookie.split("=");
        sessionRepository.remove(tokens[1]);
    }
}
