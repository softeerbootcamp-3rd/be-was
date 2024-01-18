package service;

import db.Database;
import dto.UserDto;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService();

    @Test
    void 회원가입_서비스() {
        // given
        UserDto userDto = new UserDto("ossu1975", "1234", "ossu", "ossu@gmail.com");

        // when
        userService.createUser(userDto);

        // then
        User user = Database.findUserById(userDto.getUserId());
        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    void 중복_아이디_검증() {
        // given
        UserDto userDto1 = new UserDto("ossu1975", "1234", "ossu", "ossu@gmail.com");
        UserDto userDto2 = new UserDto("ossu1975", "1234", "ossu", "ossu@gmail.com");


        // when
        userService.createUser(userDto1);
        userService.createUser(userDto2);

        // then
        Collection<User> users = Database.findAll();
        assertEquals(1, users.size());

    }
}