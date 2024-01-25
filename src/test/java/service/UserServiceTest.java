package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    UserService userService = new UserService();

    @Test
    @DisplayName("회원가입이 잘 되는지 확인")
    void createUserTest() {
        // given
        User user = new User("ossu1975", "ossu1975", "ossu", "ossu1975@gamil.com");

        // when
        try {
            userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // then
        User findUser = Database.findUserById(user.getUserId());
        assertEquals(user.getName(), findUser.getName());
        assertEquals(user.getEmail(), findUser.getEmail());
        assertEquals(user.getPassword(), findUser.getPassword());
        assertEquals(1, Database.findAll().size());
    }

    @Test
    @DisplayName("중복 아이디 검증")
    void validateDuplicatedTest() {
        // given
        User user1 = new User("ossu1975", "ossu1975", "ossu", "ossu1975@gamil.com");
        User user2 = new User("ossu1975", "ossu1975", "ossu", "ossu1975@gamil.com");

        // when
        try {
            userService.createUser(user1);
            userService.createUser(user2);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        // then
        Collection<User> users = Database.findAll();
        assertEquals(1, users.size());
    }
}