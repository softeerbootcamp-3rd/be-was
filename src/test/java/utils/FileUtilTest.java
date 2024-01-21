package utils;

import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.file.Files;

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

        File htmlContent = FileUtil.getFileFromUrl(htmlUrl);
        File javascriptContent = FileUtil.getFileFromUrl(javascriptUrl);
        File cssContent = FileUtil.getFileFromUrl(cssUrl);
        File ttfContent = FileUtil.getFileFromUrl(ttfUrl);
        File woffContent = FileUtil.getFileFromUrl(woffUrl);

        assertThat(FileUtil.getContentType(htmlContent.toPath()))
                .isEqualTo("text/html");
        assertThat(FileUtil.getContentType(javascriptContent.toPath()))
                .isEqualTo("text/javascript");
        assertThat(FileUtil.getContentType(cssContent.toPath()))
                .isEqualTo("text/css");
        assertThat(FileUtil.getContentType(ttfContent.toPath()))
                .isEqualTo("font/ttf");
        assertThat(FileUtil.getContentType(woffContent.toPath()))
                .isEqualTo("font/woff");

    }
}
