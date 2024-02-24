package dto;

import annotation.NotEmpty;

public class LoginDto {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;

    public LoginDto() {}

    public LoginDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
