package util;

import constant.ErrorCode;
import constant.MimeType;
import exception.WebServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileManagerTest {

    @Test
    @DisplayName("FileManager.getMimeType() 에 파일 경로를 매개변수로 넣었을 때, 올바른 MIME 타입이 반환된다.")
    void getFileBytes() {
        MimeType htmlMimeType = FileManager.getMimeType("a.b.c.html");
        MimeType cssMimeType = FileManager.getMimeType("a.b.c.css");
        MimeType jsMimeType = FileManager.getMimeType("a.b.c.js");
        MimeType noMimeType = FileManager.getMimeType("not.not");

        assertThat(htmlMimeType).isEqualTo(MimeType.HTML);
        assertThat(cssMimeType).isEqualTo(MimeType.CSS);
        assertThat(jsMimeType).isEqualTo(MimeType.JS);
        assertThat(noMimeType).isEqualTo(null);

        // 경로에 빈 문자열이 들어간 경우, PAGE_NOT_FOUND 예외 발생
        try {
            FileManager.getMimeType("");
        } catch (WebServerException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PAGE_NOT_FOUND);
        }
        // 경로에 확장자를 분리할 .이 없는 경우, PAGE_NOT_FOUND 예외 발생
        try {
            FileManager.getMimeType("noDot");
        } catch (WebServerException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PAGE_NOT_FOUND);
        }
        // 경로에 null 이 들어간 경우, PAGE_NOT_FOUND 예외 발생
        try {
            FileManager.getMimeType(null);
        } catch (WebServerException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PAGE_NOT_FOUND);
        }

    }

    @Test
    void getMimeType() {
    }
}
