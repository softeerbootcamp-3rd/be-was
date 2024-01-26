package config;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final ConcurrentHashMap<UUID, String> sessions = new ConcurrentHashMap<>();


    //새로운 세션 생성 후 저장
    public static UUID createSession(String userId){
        if (exitsByValue(userId))
            return null;
        //UUID는 복잡하고 길고 중복 가능성이 매우 작아 발급할 session id로 적합해 보임
        // 단순할 때 생기는 문제점: 아무 세션이나 입력했는데 이미 로그인 해놓은 유저와 겹친다면 그 유저로 인식된다.
        UUID uuid =  UUID.randomUUID();
        sessions.put(uuid, userId);
        return uuid;
    }

    public static boolean exitsByValue(String userId){
        return sessions.containsValue(userId);
    }

    public static String getUserId(UUID uuid){
        return sessions.get(uuid);
    }

}
