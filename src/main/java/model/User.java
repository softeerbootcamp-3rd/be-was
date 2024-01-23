package model;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String sessionId;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "User [userId=" + this.userId + ", password=" + this.password + ", name=" + this.name + ", email=" + this.email + "]";
    }
}
