package controller;

import dto.UserCreateDto;
import model.User;
import org.junit.jupiter.api.Test;

import db.Database;

import static controller.UserController.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
