package service;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static util.SingletonUtil.getUserService;

class UserServiceTest {

    SignUpRequest signUpRequest;

    @BeforeEach
    void init() {
        signUpRequest = new SignUpRequest("userId=test1&password=1234&name=test1&email=test1@test.com");
        getUserService().signUp(signUpRequest);
    }

    @Test
    @DisplayName("회원가입이 정상적으로 처리되는지 확인 ")
    void signUp() {
        // given
        SignUpRequest request = new SignUpRequest("userId=test2&password=1234&name=test2&email=test2@test.com");

        // when
        getUserService().signUp(request);
        User findUser = getUserService().findUserById("test2");

        // then
        assertThat(findUser).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 정상적으로 조회되는지 확인")
    void findByUserId() {
        // when
        User findUser = getUserService().findUserById("test1");

        // then
        assertThat(findUser.getUserId()).isEqualTo("test1");
        assertThat(findUser.getPassword()).isEqualTo("1234");
        assertThat(findUser.getName()).isEqualTo("test1");
        assertThat(findUser.getEmail()).isEqualTo("test1@test.com");
    }

    @Test
    @DisplayName("모든 회원정보가 정상적으로 조회되는지 확인")
    void findAll() {
        // given
        SignUpRequest request = new SignUpRequest("userId=test2&password=1234&name=test2&email=test2@test.com");

        // when
        getUserService().signUp(request);
        int userCount = getUserService().findAll().size();

        // then
        assertThat(userCount).isEqualTo(2);
    }
}