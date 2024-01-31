package domain.user.query.dto;

public class UserInfo {
    private final String userId;
    private final String userName;
    private final String email;

    public UserInfo(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
