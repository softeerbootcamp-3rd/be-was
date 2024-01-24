package dto.request;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserLoginDto {
    private String userId;
    private String password;

    private static final int KEY = 0;
    private static final int VALUE = 1;

    public UserLoginDto(String userId, String password) {
        this.userId = Objects.requireNonNull(userId, "userId는 필수입니다.");
        this.password = Objects.requireNonNull(password, "password는 필수입니다.");
    }

    public static UserLoginDto from(String info){
        Map<String, String> parameters = Arrays.stream(info.split("&"))
                .map(param -> param.split("="))
                .filter(pair -> pair.length > 1)
                .collect(Collectors.toMap(pair -> pair[KEY], pair -> pair[VALUE]));

        return new UserLoginDto(
                parameters.get("userId"),
                parameters.get("password")
        );
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
