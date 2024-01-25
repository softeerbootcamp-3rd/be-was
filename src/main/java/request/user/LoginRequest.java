package request.user;

import annotation.Column;

public class LoginRequest {

    @Column
    private String userId;
    @Column
    private String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
