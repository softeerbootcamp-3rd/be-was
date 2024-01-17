package dto;

public class UserCreateRequestDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserCreateRequestDto(String userId, String password, String name, String email) {
        validate(userId, password, name, email);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validate(String userId, String password, String name, String email){
        if (userId == null || password == null || name == null || email == null) {
            throw new IllegalArgumentException("모든 필드가 입력되야 합니다.");
        }
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
