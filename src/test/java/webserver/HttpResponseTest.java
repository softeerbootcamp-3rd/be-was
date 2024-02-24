package webserver;

import constant.HttpHeader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import model.SharedData;

import java.io.*;

public class HttpResponseTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseTest.class);
    private static final String testDirectory = "./src/test/resources/";

    static {
        try {
            InputStream in = new FileInputStream(testDirectory + "Http_GET.txt");
            HttpRequest request = new HttpRequest(in);
            SharedData.request.set(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void responseForward() throws Exception {
        // Http_Forward.txt 결과는 응답 body에 index.html이 포함되어 있어야 한다.
        HttpResponse response = HttpResponse.forward("/index.html");
        response.send(createOutputStream("Http_Forward.txt"), logger);
    }

    @Test
    public void responseRedirect() throws Exception {
        // Http_Redirect.txt 결과는 응답 headere에 Location 정보가 /index.html로 포함되어 있어야 한다.
        HttpResponse response = HttpResponse.redirect("/index.html");
        response.send(createOutputStream("Http_Redirect.txt"), logger);
    }

    @Test
    public void responseCookies() throws Exception {
        // Http_Cookie.txt 결과는 응답 header에 Set-Cookie 값으로 logined=true 값이 포함되어 있어야 한다.
        HttpResponse response = HttpResponse.redirect("/index.html");
        response.getHeader().put(HttpHeader.SET_COOKIE, "logined=true");
        response.send(createOutputStream("Http_Cookie.txt"), logger);
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
