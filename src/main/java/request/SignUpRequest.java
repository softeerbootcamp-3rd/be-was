package request;

public class SignUpRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

    // 회원가입 요청 객체 생성
    public SignUpRequest(String request) {

        String[] userInfo = request.split("&");

        this.userId = userInfo[0].split("=")[1];
        this.password = userInfo[1].split("=")[1];
        this.name = userInfo[2].split("=")[1];
        this.email = userInfo[3].split("=")[1];
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
