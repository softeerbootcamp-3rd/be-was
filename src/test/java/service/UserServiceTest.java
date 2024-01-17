package service;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private final UserService userService = UserService.getInstance();
    SignUpRequest signUpRequest;

    @BeforeEach
    void init() {
        signUpRequest = new SignUpRequest("userId=test1&password=1234&name=test1&email=test1@test.com");
        userService.signUp(signUpRequest);
    }

    @Test
    @DisplayName("회원가입이 정상적으로 처리되는지 확인 ")
    void signUp() {
        // given
        SignUpRequest request = new SignUpRequest("userId=test2&password=1234&name=test2&email=test2@test.com");

        // when
        userService.signUp(request);
        User findUser = userService.findUser("test2");

        // then
        assertThat(findUser).isNotNull();
    }

    @Test
    @DisplayName("회원정보 단건 조회")
    void findUser() {
        // when
        User findUser = userService.findUser("test1");

        // then
        assertThat(findUser.getUserId()).isEqualTo("test1");
        assertThat(findUser.getPassword()).isEqualTo("1234");
        assertThat(findUser.getName()).isEqualTo("test1");
        assertThat(findUser.getEmail()).isEqualTo("test1@test.com");
    }

    @Test
    @DisplayName("회원정보 전체 조회")
    void findUsers() {
        // given
        SignUpRequest request = new SignUpRequest("userId=test2&password=1234&name=test2&email=test2@test.com");

        // when
        userService.signUp(request);
        int userCount = userService.findUsers().size();

        // then
        assertThat(userCount).isEqualTo(2);
    }
}