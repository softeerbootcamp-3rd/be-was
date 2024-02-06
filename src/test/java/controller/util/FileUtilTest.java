package controller.util;

import model.User;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.SessionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilTest {

    @Test
    void getFileFromUrlSuccessTest() {
        File existingfile = FileUtil.getFileFromUrl("/index.html");

        assertThat(existingfile).exists();
    }

    @Test
    void getFileFromUrlFailTest() {
        File existingfile = FileUtil.getFileFromUrl("/inde.css");

        assertThat(existingfile).doesNotExist();
    }

    @Test
    void getContentTypeTest() throws IOException {
        String htmlUrl = "/index.html";
        String cssUrl = "/index.css";
        String javascriptUrl = "/index.js";
        String ttfUrl = "/index.ttf";
        String woffUrl = "/index.woff";

        assertThat(FileUtil.getContentType(htmlUrl))
                .isEqualTo("text/html");
        assertThat(FileUtil.getContentType(javascriptUrl))
                .isEqualTo("text/javascript");
        assertThat(FileUtil.getContentType(cssUrl))
                .isEqualTo("text/css");
        assertThat(FileUtil.getContentType(ttfUrl))
                .isEqualTo("font/ttf");
        assertThat(FileUtil.getContentType(woffUrl))
                .isEqualTo("font/woff");
    }

    @Test
    void generateDynamicMenuBarTest() throws IOException {
        String id = SessionManager.createSession(new User("mocking", "mocking", "mocking", "mock@mock.com"));

        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader(
                        "GET /user/login.html HTTP/1.1\n"
                                + "Cookie: sid=" + id + "\n"
                                + "\n"
                )));
        HttpResponse response = HttpResponse.of();
        response.setBody(FileUtil.readFile(FileUtil.getFileFromUrl("/index.html")));

        FileUtil.generateDynamicMenuBar(request, response);

        String htmlBodyString = new String(response.getBody());

        assertThat(htmlBodyString).contains("<li><a href=\"#\">mocking</a></li>");
    }
}
