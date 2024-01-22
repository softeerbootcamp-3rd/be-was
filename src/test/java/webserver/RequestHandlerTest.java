package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestHandlerTest {
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
