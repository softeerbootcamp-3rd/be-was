package db;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

public class DatabaseTest {
    @Test()
    @DisplayName("DB Test")
    public void databaseTest() {
        // given
        int initialSize = Database.findAll().size();
        User user = new User("user", "password", "name", "softeer@example.com");

        // when
        Database.addUser(user);

        // then
        Assertions.assertThat(initialSize + 1).isEqualTo(Database.findAll().size());
        Assertions.assertThat(Database.findUserById("user")).isEqualTo(user);
    }
}
