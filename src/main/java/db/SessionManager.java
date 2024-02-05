package db;

import com.google.common.collect.Maps;
import http.Session;
import model.User;

import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, Session> sessions = Maps.newConcurrentMap();

    // 사용자 세션 생성
    public static String createUserSession(User user) {
        // 세션 ID 생성
        String sid = generateSessionId();
        // 세션 맵에 사용자 세션 추가
        sessions.put(sid, Session.of("user", user));
        // 생성된 세션 ID 반환
        return sid;
    }

    // 세션 ID 생성
    private static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    // 세션 만료 처리
    private static void processExpiration(String sid, Session session){
        if(session != null){
            // 세션이 유효한지 확인하고, 유효하면 마지막 액세스 시간 업데이트
            if(session.validateExpiration()){
                session.updateLastAccessTime();
            }else{
                // 세션이 만료된 경우 맵에서 제거
                sessions.remove(sid);
            }
        }
    }

    // 세션에서 사용자 정보 가져오기
    public static User fetchUser(String sid) {
        Session session = sessions.get(sid);
        // 세션 만료 처리
        processExpiration(sid, session);
        // 만료된 세션이면 null 반환, 그렇지 않으면 사용자 정보 반환
        if(sessions.get(sid) == null){
            return null;
        }
        return (User) session.getValue("user");
    }

    // 세션 제거
    public static void remove(String sid) {
        if(sid != null){
            sessions.remove(sid);
        }
    }
}
