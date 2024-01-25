package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestHandlerTest {

    // 메소드 별로 테스트를 수행해야 한다.
    // 추후 리팩토링 과정에서 run method가 분리되면 그에 맞는 새로운 테스트 코드가 작성되어야 한다
    @Test
    public void userCreationTest() {
        User user = new User("testId", "testPwd", "testName", "testEmail");
        Database.addUser(user);
        User dbUser = Database.findUserById("testId");
        Assertions.assertEquals(dbUser.getUserId(), "testId");
        Assertions.assertEquals(dbUser.getPassword(), "testPwd");
        Assertions.assertEquals(dbUser.getName(), "testName");
        Assertions.assertEquals(dbUser.getEmail(), "testEmail");
    }
}

