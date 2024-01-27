package domain.user.query.application;


public class UserNameResponse {
    private String userName;

    public UserNameResponse(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
