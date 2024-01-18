package dto;

import model.User;

public class UserSignUpDto {
    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public UserSignUpDto(String id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}