package webserver;

import dto.HttpResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ResponseTest {

    @Test
    @DisplayName("Path에서 MimeType을 추출한다")
    void getMimeType() throws IOException {

        //given
        String path = "/css/bootstrap.min.css";

        //when
        String mimeType = HttpResponse.getContentType(path);

        //then
        Assertions.assertThat(mimeType).isEqualTo("text/css");
    }
}