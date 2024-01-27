package common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static common.util.FileManager.getFile;
import static org.assertj.core.api.Assertions.*;

class FileManagerTest {

    @Test
    @DisplayName("정적 컨텐츠의 로딩 성공 테스트 - 리소스 파일이 존재하는 경우")
    void getFileSuccessTest() throws IOException {

        //given
        String filePath = "/index.html";
        String contentType = "text/html";

        //when
        byte[] file = getFile(filePath, contentType);

        //then
        assertThat(file.getClass()).isEqualTo(byte[].class);
        assertThat(file.length).isEqualTo(6902);
    }

    @Test
    @DisplayName("정적 컨텐츠의 로딩 실패 테스트 - 리소스 파일이 존재하지 않는 경우")
    void getFileFailTest() {

        //given
        String filePath = "/indexxxxx.html";
        String contentType = "text/html";

        //when, then
        assertThatThrownBy(() -> getFile(filePath, contentType))
                .isInstanceOf(FileNotFoundException.class);
    }
}