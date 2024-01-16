package webserver;

import model.User;
import org.junit.Test;

import java.net.Socket;
import java.util.HashMap;

import static org.junit.Assert.*;

public class RequestHandlerTest {

    @Test
    public void queryStringParsingTest() {
        HashMap<String, String> result = RequestHandler.queryStringParsing(
                "userId=javajigi" +
                "&" +
                "password=password" +
                "&" +
                "name=%EB%B0%95%EC%9E%AC%EC%84%B1" +
                "&" +
                "email=javajigi%40slipp.net");
        assertNotNull(result);
    }

    @Test
    public void verifyUserTest() {
        String result = RequestHandler.verifyUser(new User("userId",
                "",
                "name",
                "email@gmail.com"));
        assertEquals(result, "입력란에 공백이 존재하면 안됩니다.");
    }
}