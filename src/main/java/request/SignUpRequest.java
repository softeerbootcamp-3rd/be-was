package request;

public class SignUpRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

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

    public static SignUpRequest parseSignUpRequest(String request) {
        String[] split = request.split("&");
        String userId = split[0].split("=")[1];
        String password = split[1].split("=")[1];
        String name = split[2].split("=")[1];
        String email = split[3].split("=")[1];

        return new SignUpRequest(userId, password, name, email);
    }
}
