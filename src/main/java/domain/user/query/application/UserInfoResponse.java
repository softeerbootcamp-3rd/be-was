package domain.user.query.application;

public class UserInfoResponse {
    private String userId;
    private String userName;
    private String email;

    public UserInfoResponse(String userId, String userName, String email) {
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
