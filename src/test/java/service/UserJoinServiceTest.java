package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserJoinServiceTest {

    UserJoinService userJoinService = new UserJoinService();

    @AfterEach
    public void afterEach() {
        Database.clear();
    }

    @Test
    @DisplayName("유저 생성 및 DB 저장 확인")
    public void createUserAndSaveTest() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "id");
        params.put("password", "pass");
        params.put("name", "kim");
        params.put("email", "123@naver.com");

        userJoinService.createUser(params);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 가입 시 폼에 입력되지 않은 데이터가 하나라도 있는 경우 회원 가입 실패")
    public void formParamsRequiredTest() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "id");
        params.put("password", "word");
        params.put("name", "");
        params.put("email", "456@google.com");

        boolean result = userJoinService.createUser(params);
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("회원 가입 시 다른 유저의 id와 똑같은 경우 회원 가입 실패")
    public void sameUserIdTest() {
        User user = new User("id", "pass", "kim", "123@naver.com");
        Database.addUser(user);

        Map<String, String> params = new HashMap<>();
        params.put("userId", "id");
        params.put("password", "word");
        params.put("name", "lee");
        params.put("email", "456@google.com");

        boolean result = userJoinService.createUser(params);
        assertThat(result).isEqualTo(false);
    }

}
