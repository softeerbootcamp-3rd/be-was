package util.session;

import model.User;

public class SessionInfo {
    private final User user;
    private long lastAccessTime;

    public SessionInfo(User user, long lastAccessTime) {
        this.user = user;
        this.lastAccessTime = lastAccessTime;
    }

    public User getUser() {
        return user;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
