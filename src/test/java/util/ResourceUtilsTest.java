package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {
    @Test
    void getExtension() {
        //given
        String path = "/index.html";

        //when
        String extension = ResourceUtils.getExtension(path);

        //then
        Assertions.assertEquals("html", extension);
    }

    @Test
    void getExtension확장자없을때() {
        //given
        String path = "/";

        //when
        String extension = ResourceUtils.getExtension(path);

        //then
        Assertions.assertEquals("/", extension);
    }
}
