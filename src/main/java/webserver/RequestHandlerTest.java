package webserver;

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
}