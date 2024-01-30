package db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DatabaseTest {

    @Test
    @DisplayName("사용자 추가")
    void addUser() {
        // given
        User user = new User("user", "pwd", "user", "user@email");

        // when
        Database.addUser(user);

        // then
        User findUser = Database.findUserById(user.getUserId());
        assertThat(findUser.getUserId()).as("사용자 아이디 일치").isEqualTo(user.getUserId());
        assertThat(findUser.getPassword()).as("패스워드 일치").isEqualTo(user.getPassword());
        assertThat(findUser.getName()).as("이름 일치").isEqualTo(user.getName());
        assertThat(findUser.getEmail()).as("이메일 일치").isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("중복 사용자 추가")
    void addUserDuplication() {
        // given
        User user1 = new User("user", "pwd", "user", "user@email");
        User user2 = new User("user", "pwd", "user", "user@email");
        Database.addUser(user1);

        // when
        Throwable result = catchThrowable(() -> Database.addUser(user2));

        // then
        assertThat(result).as("동일한 아이디의 사용자 추가시 오류 발생").isInstanceOf(IllegalArgumentException.class);
    }
}
