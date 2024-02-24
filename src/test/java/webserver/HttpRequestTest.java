package webserver;

import constant.HttpHeader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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

    @Test
    public void testHttpRequestPost() throws Exception {

        InputStream in = new FileInputStream(testDirectory + "Http_POST.txt");
        HttpRequest request = new HttpRequest(in);

        assertEquals("POST", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader().get(HttpHeader.CONNECTION));
        assertEquals("userId=javajigi&password=password&name=JaeSung", new String(request.getBody()));
    }

    @Test
    public void testHttpRequestPost2() throws Exception {

        InputStream in = new FileInputStream(testDirectory + "Http_POST2.txt");
        HttpRequest request = new HttpRequest(in);

        assertEquals("POST", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader().get(HttpHeader.CONNECTION));
        assertEquals("1", request.getParamMap().get("id"));
        assertEquals("userId=javajigi&password=password&name=JaeSung", new String(request.getBody()));
    }

}
