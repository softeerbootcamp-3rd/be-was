package dto.request;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserSignUpDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    private static final int KEY = 0;
    private static final int VALUE = 1;

    public UserSignUpDto(String userId, String password, String name, String email) {
        this.userId = Objects.requireNonNull(userId, "userId는 필수입니다.");
        this.password = Objects.requireNonNull(password, "password는 필수입니다.");
        this.name = Objects.requireNonNull(name, "name은 필수입니다.");
        this.email = Objects.requireNonNull(email, "email은 필수입니다.");
    }

    public static UserSignUpDto from(String info){
        Map<String, String> parameters = Arrays.stream(info.split("&"))
                .map(param -> param.split("="))
                .filter(pair -> pair.length > 1)
                .collect(Collectors.toMap(pair -> pair[KEY], pair -> pair[VALUE]));

        return new UserSignUpDto(
                parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email")
        );
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
