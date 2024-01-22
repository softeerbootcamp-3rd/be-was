package controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

public class ResourcePathMappingTest {
    @ParameterizedTest
    @ValueSource(strings = {"html", "css", "js", "ico", "png", "jpg", "woff", "ttf"})
    void testGetDirectory(String extensions) {
        // Given
        String expect = getExpectDirectory(extensions);

        // When
        String actual = ResourcePathMapping.getDirectory(extensions);

        // Then
        assertThat(actual).isEqualTo(expect)
                .withFailMessage("확장자 %s의 경로는 %s여야합니다.", extensions, expect);
    }

    @ParameterizedTest
    @ValueSource(strings = {"html", "css", "js", "ico", "png", "jpg", "woff", "ttf"})
    void testGetContentType(String extensions) {
        // Given
        String expect = getExpectContentType(extensions);

        // When
        String actual = ResourcePathMapping.getContentType(extensions);

        // Then
        assertThat(actual).isEqualTo(expect)
                .withFailMessage("확장자 %s의 Content-Type은 %s여야합니다.", extensions, expect);

    }

    private String getExpectDirectory(String extension) {
        if(extension.equals("html")) {
            return "/templates";
        }
        return "/static";
    }

    private String getExpectContentType(String extension) {
        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "ico":
                return "image/x-icon";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpg";
            case "woff":
                return "font/woff";
            case "ttf":
                return "font/ttf";
            default:
                throw new IllegalArgumentException("지원하지 않는 확장자 입니다 : " + extension);
        }
    }
}
