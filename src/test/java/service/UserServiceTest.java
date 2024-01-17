package service;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService userService = new UserService();

    @Test
    @DisplayName("유저가 정상적으로 생성되는지 테스트")
    void addUserTest(){
        userService.createUser("userId", "password", "name", "email");

        User user = userService.findUser("userId");

        assertEquals("userId", user.getUserId());
        assertEquals("password", user.getPassword());
        assertEquals("name", user.getName());
        assertEquals("email", user.getEmail());
    }
}