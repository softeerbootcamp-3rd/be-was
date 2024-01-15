package controller;

import model.User;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import db.Database;

import static controller.UserController.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    @Test
    public void testCreateUser() {
        String requestPath = "/path?userId=testUser&password=testPassword&name=TestName&email=test@example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        createUser(outputStream, requestPath);

        User expectedUser = new User("testUser", "testPassword", "TestName", "test@example.com");
        assertEquals(expectedUser, Database.findUserById("testUser"));
    }
}
