package dto;

public class UserDTO {
    public UserDTO(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private String userId;
    private String password;
    private String name;
    private String email;

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }
}
