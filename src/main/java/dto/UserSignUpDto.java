package dto;

import exception.BadRequestException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
            throw new IllegalArgumentException(fieldName + " 파일이 비어있을 수 없습니다.");
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

    public static UserSignUpDto fromUrlParameters(String pathUrl) {
        try {
            HashMap<String, String> map = new HashMap<>();
            String[] splitUrl = pathUrl.split("\\?");
            String[] parameters = splitUrl[1].split("&");
            for (String param : parameters) {
                String[] value = param.split("=");
                map.put(value[0], value[1]);
            }
            return new UserSignUpDto(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new BadRequestException("Please fill in all the necessary factors", e);
        }
    }
}
