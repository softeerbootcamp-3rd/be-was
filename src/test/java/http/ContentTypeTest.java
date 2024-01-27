package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static http.ContentType.getMimeType;
import static org.assertj.core.api.Assertions.*;

class ContentTypeTest {

    @Test
    @DisplayName("MimeType 도출 테스트")
    void getMimeTypeTest() {

        //given
        String extension = "ico";

        //when
        String mimeType = getMimeType(extension);

        //then
        assertThat(mimeType).isEqualTo("image/vnd.microsoft.icon");
    }
}
