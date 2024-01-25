package db;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

public class UserDatabaseTest {
    @Test()
    @DisplayName("DB Test: addUser()")
    public void addUserTest() {
        // given
        User testUser = new User("user", "password", "name", "softeer@example.com");

        // when
        UserDatabase.addUser(testUser);
        User addedUser = UserDatabase.findUserById("user");

        // then
        Assertions.assertThat(addedUser).isNotNull();
        Assertions.assertThat(addedUser.getUserId()).isEqualTo(testUser.getUserId());
        Assertions.assertThat(addedUser.getPassword()).isEqualTo(testUser.getPassword());
        Assertions.assertThat(addedUser.getName()).isEqualTo(testUser.getName());
        Assertions.assertThat(addedUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test()
    @DisplayName("DB Test: findByUserId()")
    public void findByUserIdTest() {
        // given
        User testUser = new User("user", "password", "name", "softeer@example.com");
        UserDatabase.addUser(testUser);

        // when
        User foundUser = UserDatabase.findUserById(testUser.getUserId());
        User nullUser = UserDatabase.findUserById("no_userId");

        // then
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUserId()).isEqualTo(testUser.getUserId());
        Assertions.assertThat(foundUser.getPassword()).isEqualTo(testUser.getPassword());
        Assertions.assertThat(foundUser.getName()).isEqualTo(testUser.getName());
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());

        Assertions.assertThat(nullUser).isNull();
    }
}
