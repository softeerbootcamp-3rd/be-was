package dto;

import annotation.NotEmpty;

public class LoginDto {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }
}
