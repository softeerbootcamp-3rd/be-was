package service;

import db.Database;
import exception.WebServerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class UserServiceTest {

    @Test
    void signUp() {
        Map<String, String> user1 = new HashMap<String, String>();
        user1.put("userId", "testId");
        user1.put("password", "testPwd");
        user1.put("name", "testName");
        user1.put("email", "test@email.com");

        Map<String, String> user2 = new HashMap<String, String>(); // id 중복 확인
        user2.put("userId", "testId");

        Map<String, String> user3 = new HashMap<String, String>(); // email 중복 확인
        user3.put("userId", "testId2");
        user3.put("email", "test@email.com");

        UserService.signUp(user1);
        Assertions.assertEquals(Database.findUserById("testId").getUserId(), "testId");
        Assertions.assertEquals(Database.findUserById("testId").getPassword(), "testPwd");
        Assertions.assertEquals(Database.findUserById("testId").getName(), "testName");
        Assertions.assertEquals(Database.findUserById("testId").getEmail(), "test@email.com");

        try {
            UserService.signUp(user2);
        } catch (WebServerException e) {
            Assertions.assertEquals(e.getErrorCode().getErrorMessage(), "중복되는 유저 id 입니다.");
        }

        try {
            UserService.signUp(user3);
        } catch (WebServerException e) {
            Assertions.assertEquals(e.getErrorCode().getErrorMessage(), "중복되는 유저 이메일 입니다.");
        }
    }
}
