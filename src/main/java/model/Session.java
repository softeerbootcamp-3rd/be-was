package model;

import db.SessionStorage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class Session implements Comparable<Session> {
    private final String sessionId;
    private final String userId;
    private LocalDateTime expiredTime;

    public Session(String userId) {
        this.sessionId = String.valueOf(UUID.randomUUID());
        this.userId = userId;
        this.expiredTime = LocalDateTime.now().plusSeconds(SessionStorage.SESSION_TIME);
    }
    public String getSessionId() {return this.sessionId;}
    public String getUserId() {return this.userId;}
    public LocalDateTime getExpiredTime() {return this.expiredTime;}

    public void renewExpiredTime() {
        this.expiredTime = LocalDateTime.now().plusSeconds(SessionStorage.SESSION_TIME);
    }

    @Override
    public int compareTo(Session o) {
        return this.expiredTime.compareTo(o.getExpiredTime());
    }
}
