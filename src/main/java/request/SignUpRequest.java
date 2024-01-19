package request;

public class SignUpRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

    // 회원가입 요청 객체 생성
    public SignUpRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
