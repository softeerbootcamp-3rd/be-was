package util;


import controller.HttpMethod;
import controller.ResourceMapping;
import data.RequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static util.RequestParserUtil.getFileExtension;

public class ResourceLoaderTest {

    static Stream<Object[]> provideUrl() {
        return Stream.of(
                new Object[]{"/index.html", "FILE"},
                new Object[]{"/user/form.html", "FILE"},
                new Object[]{"/user/list.html", "FILE"},
                new Object[]{"/user/login.html", "FILE"},
                new Object[]{"/user/profile.html", "FILE"}
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrl")
    @DisplayName("리소스 로딩 테스트")
    public void loadResource_ValidResourcePath(String path, String expect) throws IOException {
        // Given
        String extension = getFileExtension(path);
        String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();
        File file = new File(ResourceLoader.RESOURCE_URL + directory + path);
        byte[] expectedResult;
        try (InputStream inputStream = new FileInputStream(file)) {
            // 파일 내용을 바이트 배열로 읽어오기
            expectedResult = new byte[(int) file.length()];
            int bytesRead = inputStream.read(expectedResult);

            // bytesRead가 -1이 아니면 계속 읽기
            while (bytesRead != -1) {
                bytesRead = inputStream.read(expectedResult);
            }
        }

        // When
        byte[] actualResult = ResourceLoader.loadResource(path, createDummyRequestData());

        // Then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private RequestData createDummyRequestData() {
        // 테스트를 위한 더미 RequestData 객체 생성
        return new RequestData(HttpMethod.GET, "/", "HTTP/1.1", null, false);
    }
}
