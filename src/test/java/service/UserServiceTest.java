package service;

import db.Database;
import exception.WebServerException;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void signUp() {
        Map<String, String> user1Params = new HashMap<String, String>();
        user1Params.put("userId", "testId");
        user1Params.put("password", "testPwd");
        user1Params.put("name", "testName");
        user1Params.put("email", "test@email.com");

        Map<String, String> user2Params = new HashMap<String, String>(); // id 중복 확인
        user2Params.put("userId", "testId");

        Map<String, String> user3Params = new HashMap<String, String>(); // email 중복 확인
        user3Params.put("userId", "testId2");
        user3Params.put("email", "test@email.com");

        UserService.signUp(user1Params);
        Assertions.assertEquals(Database.findUserById("testId").getUserId(), "testId");
        Assertions.assertEquals(Database.findUserById("testId").getPassword(), "testPwd");
        Assertions.assertEquals(Database.findUserById("testId").getName(), "testName");
        Assertions.assertEquals(Database.findUserById("testId").getEmail(), "test@email.com");

        try {
            UserService.signUp(user2Params);
        } catch (WebServerException e) {
            Assertions.assertEquals(e.getErrorCode().getErrorMessage(), "중복되는 유저 id 입니다.");
        }

        try {
            UserService.signUp(user3Params);
        } catch (WebServerException e) {
            Assertions.assertEquals(e.getErrorCode().getErrorMessage(), "중복되는 유저 이메일 입니다.");
        }
    }
}