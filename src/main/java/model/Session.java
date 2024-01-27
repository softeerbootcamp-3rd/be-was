package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private String sessionId;
    private String userId;
    private LocalDateTime expireDate;
    private LocalDateTime createDate;
    private static final int SESSION_EXPIRE_TIME_IN_HOURS = 24;

    public Session(String userId) {
        this.sessionId = generateSessionId();
        this.userId = userId;

        LocalDateTime now = LocalDateTime.now();
        this.expireDate = now.plusHours(SESSION_EXPIRE_TIME_IN_HOURS);
        this.createDate = now;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getUserId() {
        return this.userId;
    }

    public LocalDateTime getExpireDate() {
        return this.expireDate;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Session [sessionId = " + this.sessionId + ", userId = " + this.userId
                + ", expireDate = " + this.expireDate + ", createDate = " + this.createDate + "]";
    }
}
