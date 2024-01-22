package webserver.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ContentTypeTest {
    @Test
    @DisplayName("확장자로 ContentType 찾기 테스트")
    void findContentTypeTest(){
        String fileExtension = "css";

        ContentType contentType = ContentType.findContentType(fileExtension);

        assertEquals(ContentType.CSS, contentType);
    }

    @Test
    @DisplayName("없는 확장자로 ContentType 찾을 때 null 인지 테스트")
    void findNonExistsContentTypeTest(){
        String fileExtension = "csss";

        ContentType contentType = ContentType.findContentType(fileExtension);

        assertNull(contentType);
    }
}