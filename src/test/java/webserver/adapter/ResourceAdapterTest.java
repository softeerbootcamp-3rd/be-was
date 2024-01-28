package webserver.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.request.Request;
import webserver.response.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResourceAdapterTest {
    private final URL HTML_BASE_URL = ResourceAdapter.class.getClassLoader().getResource("./templates");
    private final URL OTHERS_BASE_URL = ResourceAdapter.class.getClassLoader().getResource("./static");

    private final ResourceAdapter resourceAdapter = ResourceAdapter.getInstance();

    @Test
    @DisplayName("/index.html 파일을 요청할 때 테스트")
    void requestIndexHtml() throws IOException {
        Request requestHeader = Request.of("GET", "/index.html", "HTTP/1.1");

        Response response = resourceAdapter.run(requestHeader);

        File file = new File(HTML_BASE_URL.getPath() + "/index.html");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        assertEquals(true, isSame(fileBytes, response.getBody()));
    }

    @Test
    @DisplayName("/css/styles.css 파일을 요청할 때 테스트")
    void requestStyleCss() throws IOException {
        Request requestHeader = Request.of("GET", "/css/styles.css", "HTTP/1.1");

        Response response = resourceAdapter.run(requestHeader);

        File file = new File(OTHERS_BASE_URL.getPath() + "/css/styles.css");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        assertEquals(true, isSame(fileBytes, response.getBody()));
    }

    @Test
    @DisplayName("/index1.html 파일을 요청할 때 예외 테스트")
    void requestIndex1Html() throws IOException {
        Request requestHeader = Request.of("GET", "/index1.html", "HTTP/1.1");

        Response response = resourceAdapter.run(requestHeader);

        assertNull(response);
    }

    @Test
    @DisplayName("/index.html를 요청했을 때 canRun 테스트")
    void canRunIndexHtml(){
        Request requestHeader = Request.of("GET", "/index.html", "HTTP/1.1");

        assertEquals(true, resourceAdapter.canRun(requestHeader));
    }

    @Test
    @DisplayName("/css/styles.css 파일을 요청할 때 테스트")
    void canRunStyleCss() {
        Request requestHeader = Request.of("GET", "/css/styles.css", "HTTP/1.1");

        assertEquals(true, resourceAdapter.canRun(requestHeader));
    }

    @Test
    @DisplayName("/index1.html 파일을 요청할 때 canRun 테스트")
    void cannotRunIndex1Html() {
        Request requestHeader = Request.of("GET", "/index1.html", "HTTP/1.1");

        assertEquals(false, resourceAdapter.canRun(requestHeader));
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