package model;

import db.SessionStorage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class Session {
    private final String sessionId;
    private final String userId;
    private LocalDateTime expiredTime;

    public Session(String userId) {
        this.sessionId = String.valueOf(UUID.randomUUID());
        this.userId = userId;
        this.expiredTime = LocalDateTime.now();
    }
    public String getSessionId() {return this.sessionId;}

    public void renewExpiredTime() {
        this.expiredTime = this.expiredTime.plusSeconds(SessionStorage.SESSION_TIME);
    }
}
