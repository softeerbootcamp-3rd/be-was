package service;

import db.Database;
import dto.request.UserSignUpDto;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    @BeforeEach
    void setUp() {
        User existingUser = new User("userId", "1234", "name", "test@test.com");
        Database.addUser(existingUser);
    }

    @DisplayName("UserDto의 정보로 데이터베이스에 user를 저장한다.")
    @ValueSource(strings = {"userId=user1&password=1234&name=test&email=test@naver.com"})
    @ParameterizedTest
    void signUp(String query){
        //given
        UserSignUpDto userSignUpDto = UserSignUpDto.from(query);

        //when
        UserService.signUp(userSignUpDto);

        //then
        User user = Database.findUserById("user1");
        assertThat(user)
                .extracting("userId", "password", "name", "email")
                .contains("user1", "1234", "test", "test@naver.com");

    }

    @DisplayName("이미 존재하는 아이디인 경우 예외가 발생한다.")
    @ValueSource(strings = {"userId=userId&password=1234&name=test&email=test2@naver.com"})
    @ParameterizedTest
    void signUpWithSameUser(String query){
        //given
        UserSignUpDto userSignUpDto = UserSignUpDto.from(query);

        //when //then
        assertThatThrownBy(() -> UserService.signUp(userSignUpDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @DisplayName("아이디와 비밀번호로 로그인을 성공하면 userId를 반환한다.")
    @Test
    void loginSuccess(){
        //given
        User user = new User("1", "1234", "test", "test@naver.com");
        Database.addUser(user);

        //when
        String success = UserService.login(user.getUserId(), user.getPassword());

        //then
        assertThat(success).isEqualTo("1");
    }

    @DisplayName("아이디와 비밀번호가 일치하지 않는 경우 예외가 발생한다.")
    @Test
    void loginWrongUser(){
        //given
        User user = new User("1", "1234", "test", "test@naver.com");
        Database.addUser(user);

        //when //then
        String wrongUserId = "2";
        assertThatThrownBy(() -> UserService.login(wrongUserId, user.getPassword()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 잘못되었습니다.");
    }
}
