package webserver.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.header.RequestHeader;
import webserver.response.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ResourceHandlerTest {
    private final URL HTML_BASE_URL = ResourceHandler.class.getClassLoader().getResource("./templates");
    private final URL OTHERS_BASE_URL = ResourceHandler.class.getClassLoader().getResource("./static");

    @Test
    @DisplayName("/index.html 파일을 요청할 때 테스트")
    void requestIndexHtml() throws IOException {
        RequestHeader requestHeader = RequestHeader.of("GET", "/index.html", "HTTP/1.1");

        Response response = ResourceHandler.run(requestHeader);

        File file = new File(HTML_BASE_URL.getPath() + "/index.html");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        assertEquals(true, isSame(fileBytes, response.getBody()));
    }

    @Test
    @DisplayName("/css/styles.css 파일을 요청할 때 테스트")
    void requestStyleCss() throws IOException {
        RequestHeader requestHeader = RequestHeader.of("GET", "/css/styles.css", "HTTP/1.1");

        Response response = ResourceHandler.run(requestHeader);

        File file = new File(OTHERS_BASE_URL.getPath() + "/css/styles.css");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        assertEquals(true, isSame(fileBytes, response.getBody()));
    }

    @Test
    @DisplayName("/index1.html 파일을 요청할 때 예외 테스트")
    void requestIndex1Html() throws IOException {
        RequestHeader requestHeader = RequestHeader.of("GET", "/index1.html", "HTTP/1.1");

        Response response = ResourceHandler.run(requestHeader);

        assertNull(response);
    }

    private boolean isSame(byte[] expected, byte[] actual){
        if(expected.length != actual.length){
            return false;
        }

        for(int i = 0; i < expected.length; i++){
            if(expected[i] != actual[i]){
                return false;
            }
        }

        return true;
    }
}