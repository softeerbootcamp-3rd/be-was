package controller;

import constant.HttpStatus;
import db.Database;
import dto.LoginDto;
import dto.UserCreateDto;
import model.User;
import org.junit.jupiter.api.Test;
import util.ObjectMapper;
import util.RequestParser;
import util.SessionManager;
import webserver.HttpResponse;

import static controller.UserController.createUser;
import static controller.UserController.login;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    public void testCreateUser() {
        String userId = "testUser";
        String password = "testPassword";
        String name = "TestName";
        String email = "test@example.com";

        createUser(new UserCreateDto(userId, password, name, email));

        User expectedUser = new User("testUser", "testPassword", "TestName", "test@example.com");
        assertEquals(expectedUser, Database.findUserById("testUser"));
    }

    @Test
    public void testLogin() {
        User user = new User("validUserId", "validPassword", "TestName", "test@example.com");
        Database.addUser(user);

        LoginDto validLoginInfo = new LoginDto("validUserId", "validPassword");
        HttpResponse httpResponse = login(validLoginInfo);

        assertEquals(HttpStatus.FOUND, httpResponse.getStatus());
        assertTrue(httpResponse.getHeader().containsKey("Set-Cookie"));
        assertTrue(httpResponse.getHeader().containsKey("Location"));

        String sid = RequestParser.parseCookie(httpResponse.getHeader().get("Set-Cookie")).get("SID");
        assertNotNull(SessionManager.getUserBySessionId(sid));
    }
}
