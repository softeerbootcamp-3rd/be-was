package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService();

    @Test
    void 회원가입_서비스() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("userId", "ossu1975");
        params.put("password", "ossu1975");
        params.put("name", "ossu");
        params.put("email", "ossu1975@gmail.com");

        // when
        userService.createUser(params);

        // then
        User findUser = Database.findUserById(params.get("userId"));
        assertEquals(params.get("name"), findUser.getName());
        assertEquals(params.get("email"), findUser.getEmail());
        assertEquals(params.get("password"), findUser.getPassword());
        assertEquals(1, Database.findAll().size());
    }

    @Test
    void 중복_아이디_검증() {
        // given
        Map<String, String> params1 = new HashMap<>();
        params1.put("userId", "ossu1975");
        params1.put("password", "ossu1975");
        params1.put("name", "ossu");
        params1.put("email", "ossu1975@gmail.com");

        Map<String, String> params2 = new HashMap<>();
        params2.put("userId", "ossu1975");
        params2.put("password", "ossu1975");
        params2.put("name", "ossu");
        params2.put("email", "ossu1975@gmail.com");

        // when
        userService.createUser(params1);
        userService.createUser(params2);

        // then
        Collection<User> users = Database.findAll();
        assertEquals(1, users.size());
    }
}