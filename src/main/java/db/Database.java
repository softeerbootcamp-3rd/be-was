package db;

import com.google.common.collect.Maps;

import dto.session.Session;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {

    // 유저 저장소 (key: user id, value: User 객체)
    private static Map<String, User> users = Maps.newHashMap();
    // 세션 저장소 (key: session id, value: Session 객체)
    private static Map<String, Session> sessions = Maps.newHashMap();

    // 유저 관련 메소드
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    public static User findUserById(String userId) {
        return users.get(userId);
    }
    public static Collection<User> findAllUser() {
        return users.values();
    }
    public static void clearUsers() {
        users.clear();
    }

    // 세션 관련 메소드
    public static void addSession(Session session) {        // 세션 추가
        sessions.put(session.getId(), session);
    }
    public static Session findSessionById(String sessionId) {       // 세션 id로 세션 탐색 후 반환
        return sessions.get(sessionId);
    }
    public static void deleteSession(String sessionId) {            // 저장소에서 세션 삭제
        sessions.remove(sessionId);
    }
    public static Collection<Session> findAllSession() {
        return sessions.values();
    }
    public static void clearSessions() {
        sessions.clear();
    }

    // 세션 가비지 컬렉터
    public static void sessionGC() {
        for(Map.Entry<String, Session> entry : sessions.entrySet()) {
            if(entry.getValue().isValid())
                continue;
            // 만료된 세션일 경우 -> 저장소에서 삭제
            deleteSession(entry.getKey());
        }
    }
}
