package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserService userService = new UserService();

    @Test
    void create() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";

        //When
        String id = userService.create(userId, password, name, email);

        //Then
        User user = Database.findUserById(id);

        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }
}
