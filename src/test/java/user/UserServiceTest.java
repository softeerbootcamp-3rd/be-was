package user;

import config.AppConfig;
import exception.AlreadyExistUserException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;


import static org.assertj.core.api.Assertions.*;

public class UserServiceTest {

    private final UserService userService = AppConfig.userService();

    @BeforeEach
    void clearDB(){
        userService.clear();
    }

    @Test
    void createUserTest() {
        User user = new User("test", "password", "lsh", "lsh@naver.com");
        userService.addUser(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        User findUser = userService.findUserById(user.getUserId());

        assertThat(user).isEqualTo(findUser);
    }

    @Test
    void duplicatedUserTest() {
        User user = new User("test", "password", "lsh", "lsh@naver.com");
        userService.addUser(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        assertThatExceptionOfType(AlreadyExistUserException.class)
                .isThrownBy(() -> userService.addUser(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()))
                .withMessage("이미 존재하는 userId입니다."); // 예외 메시지도 검증

    }
}
