package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserServiceTest 클래스")
class UserServiceTest {
    private UserService userService;
    private Database database;
    private User user;

    @BeforeEach
    void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.userService = UserService.getInstance();
        this.database = new Database();

        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        this.user = constructor.newInstance();

        Field[] fields = User.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(user, "test");
        }
    }

    @Test
    @DisplayName("회원가입이 정상적으로 처리되는지 확인")
    void sign_up_success() {

        // when
        userService.join(user);
        User findUser = database.findUserById("test");

        // then
        assertThat(findUser).isNotNull();
    }
}
