package webserver;

import constant.HttpHeader;
import database.UserRepository;
import dto.UserCreateDto;
import model.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static controller.UserController.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestTest {
    private final String testDirectory = "./src/test/resources/";

    @Test
    public void testHttpRequestGet() throws Exception {

        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        assertEquals("GET", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader().get(HttpHeader.CONNECTION));
        assertEquals("javajigi", request.getParamMap().get("userId"));
    }

}
