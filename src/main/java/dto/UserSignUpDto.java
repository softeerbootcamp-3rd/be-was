package dto;

import exception.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Objects;

public class UserSignUpDto {
    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public UserSignUpDto(String id, String password, String name, String email) throws UnsupportedEncodingException {
        validateNotNullOrEmpty(id, "User ID");
        validateNotNullOrEmpty(password, "Password");
        validateNotNullOrEmpty(name, "Name");
        validateNotNullOrEmpty(email, "Email");
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = URLDecoder.decode(email, "UTF-8");
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

    public static UserSignUpDto fromRequestBody(String requestBody) {
        try {
            HashMap<String, String> map = new HashMap<>();
            if (!requestBody.isEmpty()) {
                String[] pairs = requestBody.split("&");

                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    String key = keyValue[0];
                    String value = keyValue[1];
                    map.put(key, value);
                }
            }
            return new UserSignUpDto(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        } catch (IndexOutOfBoundsException | IllegalArgumentException | UnsupportedEncodingException e) {
            throw new BadRequestException("Please fill in all the necessary factors", e);
        }
    }
}
