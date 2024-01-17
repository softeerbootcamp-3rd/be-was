package dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDtoTest {

    @Test()
    @DisplayName("UserDto.buildUserDtoFromParams() Test")
    public void buildUserDtoFromParamsTest() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "id");
        parameters.put("password", "123");
        parameters.put("name", "suji");
        parameters.put("email", "softeer@softeer.net");

        // when
        UserDto userDto = UserDto.buildUserDtoFromParams(parameters);

        // then
        Assertions.assertThat(userDto.getUserId()).isEqualTo("id");
        Assertions.assertThat(userDto.getPassword()).isEqualTo("123");
        Assertions.assertThat(userDto.getName()).isEqualTo("suji");
        Assertions.assertThat(userDto.getEmail()).isEqualTo("softeer@softeer.net");
    }

    @Test()
    @DisplayName("UserDto.buildUserDtoFromParams() Fail Test")
    public void buildUserDtoFromParamsFailTest() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "id");
        parameters.put("password", "123");

        // when & then
        Assertions.assertThatThrownBy(() -> UserDto.buildUserDtoFromParams(parameters))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Parameters");

    }
}
