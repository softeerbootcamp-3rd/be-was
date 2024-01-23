package model;

import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private String userId;
    private LocalDateTime createDate;
    private LocalDateTime expireDate;
    private LocalDateTime lastAccessDate;

    public Session(String sessionId, String userId, LocalDateTime createDate){
        this.sessionId=sessionId;
        this.userId=userId;
        this.createDate=createDate;
        this.expireDate=createDate.plusDays(3);
    }

    public String getSessionId(){
        return this.sessionId;
    }

    public LocalDateTime getExpireDate(){
        return this.expireDate;
    }

}
