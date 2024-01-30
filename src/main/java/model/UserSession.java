package model;

public class UserSession {
    private User user;
    private long expiredTime;

    public UserSession(User user, long expiredTime) {
        this.user = user;
        this.expiredTime = expiredTime;
    }

    public User getUser() {
        return user;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
