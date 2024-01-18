package service;

import dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService();

    @Test
    void 회원가입() {
        // given
        UserDto userDto = new UserDto("ossu1975", "1234", "ossu", "ossu@gmail.com");

        // when
        userService.createUser(userDto);

        // then

    }
}