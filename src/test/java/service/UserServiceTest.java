package service;

import db.Database;
import dto.UserDto;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    public static final UserService userService = new UserService();

    @Test()
    @DisplayName("UserService.createUser() test")
    public void createUserTest() {
        // given
        UserDto userDto = new UserDto("userId", "password", "name", "softeer@example.com");

        // when
        userService.createUser(userDto);

        // then
        User user = Database.findUserById("userId");
        Assertions.assertThat(user.getUserId()).isEqualTo(userDto.getUserId());
        Assertions.assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        Assertions.assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        Assertions.assertThat(user.getName()).isEqualTo(userDto.getName());

        Database.deleteUserById(userDto.getUserId()); // 테스트 종료 후 데이터 정리
    }
}
