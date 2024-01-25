package utils;

import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilTest {
    @Test
    @DisplayName("Url을 받아 알맞은 Content-Type으로 반환")
    void testGetContentType() throws IOException {
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
}
