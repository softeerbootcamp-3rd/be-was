package dto;

public class Login {
    public Login(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    private String userId;
    private String password;

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }
}
