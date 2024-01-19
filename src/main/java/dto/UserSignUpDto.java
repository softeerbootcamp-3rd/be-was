package dto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserSignUpDto {
    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public UserSignUpDto(String id, String password, String name, String email) {
        validateNotNullOrEmpty(id, "User ID");
        validateNotNullOrEmpty(password, "Password");
        validateNotNullOrEmpty(name, "Name");
        validateNotNullOrEmpty(email, "Email");
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = URLDecoder.decode(email, StandardCharsets.UTF_8);
    }

    private void validateNotNullOrEmpty(String value, String fieldName) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
