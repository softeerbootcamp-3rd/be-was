package service;

import static org.assertj.core.api.Assertions.*;

import db.Database;
import dto.UserDto;
import model.User;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    public void saveUser() {
        // given
        UserDto case1 = new UserDto("userA", "pwd", "A", "A@email.com");

        // when
        userService.saveUser(case1);

        // then
        User result1 = Database.findUserById(case1.getUserId());
        assertThat(result1.getUserId()).isEqualTo(case1.getUserId());
        assertThat(result1.getPassword()).isEqualTo(case1.getPassword());
        assertThat(result1.getName()).isEqualTo(case1.getName());
        assertThat(result1.getEmail()).isEqualTo(case1.getEmail());
    }
}