package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService();

    @Test
    void 회원가입_서비스() {
        // given
        User user = new User("ossu1975", "1234", "ossu", "ossu@gmail.com");

        // when
        userService.createUser(user);

        // then
        User findUser = Database.findUserById(user.getUserId());
        assertEquals(user.getName(), findUser.getName());
        assertEquals(user.getEmail(), findUser.getEmail());
        assertEquals(user.getPassword(), findUser.getPassword());
        assertEquals(1, Database.findAll().size());
    }

    @Test
    void 중복_아이디_검증() {
        // given
        User user1 = new User("ossu1975", "1234", "ossu", "ossu@gmail.com");
        User user2 = new User("ossu1975", "1234", "ossu", "ossu@gmail.com");

        // when
        userService.createUser(user1);
        userService.createUser(user2);

        // then
        Collection<User> users = Database.findAll();
        assertEquals(1, users.size());
    }
}