package dto.user.request;

import java.util.Map;

public class UserCreateRequestDto {

    private String userId;
    private String password;
    private String name;
    private String email;

    private UserCreateRequestDto() {
    }

    private UserCreateRequestDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public static UserCreateRequestDto of(Map<String, String> params) {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        params.forEach((key, value) ->
            {
                switch (key) {
                    case "userId":
                        userCreateRequestDto.userId = value;
                        break;
                    case "password":
                        userCreateRequestDto.password = value;
                        break;
                    case "name":
                        userCreateRequestDto.name = value;
                        break;
                    case "email":
                        userCreateRequestDto.email = value;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
        );
        return userCreateRequestDto;
    }
}
