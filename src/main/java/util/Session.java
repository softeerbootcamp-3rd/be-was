package util;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private String sessionId;
    private String userId;
    private LocalDateTime createTime;
    private LocalDateTime expires;

    public Session(String userId) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.createTime = LocalDateTime.now();
        this.expires = createTime.plusMinutes(10);
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public String getUserId() {
        return userId;
    }
}
