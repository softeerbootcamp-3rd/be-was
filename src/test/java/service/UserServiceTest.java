package service;

import dto.UserRequest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exception.UserIdAlreadyExistsException;

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

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입 할 때 예외 발생 테스트")
    void addUserByExistsUserIdTest(){
        userService.createUser("userId", "password", "name", "email");

        assertThrows(UserIdAlreadyExistsException.class, () -> {
            userService.createUser("userId", "password", "name", "email");
        });
    }
}