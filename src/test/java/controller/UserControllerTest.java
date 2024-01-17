package controller;

import model.User;
import org.junit.jupiter.api.Test;

import db.Database;
import webserver.HttpRequest;

import static controller.UserController.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    @Test
    public void testCreateUser() {
        String request = "GET /path?userId=testUser&password=testPassword&name=TestName&email=test@example.com";

        createUser(new HttpRequest(request));

        User expectedUser = new User("testUser", "testPassword", "TestName", "test@example.com");
        assertEquals(expectedUser, Database.findUserById("testUser"));
    }
}
