package test;

import db.Database;
import model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void verifyUser1() {
        User user = new User(
                "test1",
                "test1",
                "test1",
                "test1@gmail.com");
        String result = User.verifyUser(user);
        assertEquals(result, "성공");
        Database.addUser(user);
    }

    @Test
    public void verifyUser2() {
        String result = User.verifyUser(new User(
                "test2",
                "",
                "test2",
                "test2@gmail.com"));
        assertEquals(result, "입력란에 공백이 존재하면 안됩니다.");
    }

    @Test
    public void verifyUser3() {
        String result = User.verifyUser(new User(
                "test1",
                "test3",
                "test3",
                "test3@gmail.com"));
        assertEquals(result, "중복되는 아이디 입니다.");
    }
}