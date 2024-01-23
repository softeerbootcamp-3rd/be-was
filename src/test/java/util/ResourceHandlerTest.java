package util;

import dto.ResourceDto;
import exception.SourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class ResourceHandlerTest {

    @Nested
    @DisplayName("ResourceHandler 리소스 요청 메소드")
    class ResourceRequest {

        @Nested
        @DisplayName("존재하는 자원을 요청하면")
        class ExistResource {
            @Test
            @DisplayName("css static 자원을 반환한다.")
            public void cssSourceResolveTest() throws IOException {
                // given
                ResourceDto resourceDto = ResourceDto.of("/css/styles.css");

                // when
                byte[] resource = ResourceHandler.resolveResource(resourceDto);

                // then
                Assertions.assertNotNull(resource);
            }

            @Test
            @DisplayName("js static 자원을 반환한다")
            public void jsSourceResolveTest() throws IOException {
                // given
                ResourceDto resourceDto = ResourceDto.of("/js/scripts.js");

                // when
                byte[] resource = ResourceHandler.resolveResource(resourceDto);

                // then
                Assertions.assertNotNull(resource);
            }

            @Test
            @DisplayName("html templates 자원을 반환한다")
            public void htmlSourceResolveTest() throws IOException {
                // given
                ResourceDto resourceDto = ResourceDto.of("/index.html");

                // when
                byte[] resource = ResourceHandler.resolveResource(resourceDto);

                // then
                Assertions.assertNotNull(resource);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 자원을 요청하면")
        class NotExistResource {
            @Test
            @DisplayName("예외가 발생한다.")
            public void cssSourceResolveTest() {
                // given
                ResourceDto resourceDto = ResourceDto.of("/css/bad.css");

                // when & then
                Assertions.assertThrows(SourceException.class, () -> ResourceHandler.resolveResource(resourceDto));
            }
        }

    }

}