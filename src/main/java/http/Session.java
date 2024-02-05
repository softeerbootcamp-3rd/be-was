package http;

import com.google.common.collect.Maps;

import java.util.Map;

public class Session {
    private final Map<String, Object> attributes = Maps.newHashMap();
    private long lastAccessTime;
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30분


    //세션 객체 생성 및 초기화
    private Session(String key, Object value) {
        attributes.put(key, value);
        this.lastAccessTime = System.currentTimeMillis();
    }

    public static Session of(String key, Object value) {
        return new Session(key, value);
    }

    //지정된 키에 해당되는 세션 속성 값 반환
    public Object getValue(String key) {
        return attributes.get(key);
    }

    //세션의 마지막 액세스 시간 현재 시간으로 업데이트
    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    //세션 만료 확인
    public boolean validateExpiration() {
        return this.lastAccessTime + SESSION_TIMEOUT > System.currentTimeMillis();

    }

    @Override
    public String toString() {
        return "Session{" +
                "attributes=" + attributes +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }
}
