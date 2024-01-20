package service;

import db.Database;
import dto.request.UserDto;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        UserDto userDto = UserDto.from(query);

        //when
        UserService.signUp(userDto);

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
        UserDto userDto = UserDto.from(query);

        //when //then
        assertThatThrownBy(() -> UserService.signUp(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

}
