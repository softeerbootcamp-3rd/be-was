package db;

import model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseTest {

    @DisplayName("userId, user를 맵으로 저장할 수 있다.")
    @Test
    void addUser(){
        //given
        User user = new User("user1", "1234", "test", "test@naver.com");

        //when
        Database.addUser(user);

        //then
        User findUser = Database.findUserById(user.getUserId());
        assertThat(findUser)
                .extracting("userId", "password", "name", "email")
                .contains("user1", "1234", "test", "test@naver.com");
    }
}
