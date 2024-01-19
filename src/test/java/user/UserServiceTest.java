package user;

import db.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @AfterEach
    public void tearDown() {
        Database.clearUsers();
    }

    @Test
    public void signUpSuccessTest() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "isExample");
        queryParams.put("password", "password123");
        queryParams.put("name", "isExample");
        queryParams.put("email", "isExample@naver.com");

        assertTrue(userService.signUp(queryParams));
    }

    @Test
    public void signUpFailureDueToDuplicateUserIdTest() {
        // isExample 사용자를 하나 추가
        userService.signUp(new HashMap<>() {{
            put("userId", "isExample");
            put("password", "password123");
            put("name", "isExample");
            put("email", "isExample@naver.com");
        }});

        // 동일한 userId로 다시 시도
        assertFalse(userService.signUp(new HashMap<>() {{
            put("userId", "isExample");
            put("password", "newPassword");
            put("name", "user2");
            put("email", "user2@gmail.com");
        }}));
    }

    @Test
    public void signUpFailureDueToMissingParamsTest() {
        // name과 email이 누락됨
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "user");
        queryParams.put("password", "password123");

        assertFalse(userService.signUp(queryParams));
    }

}
