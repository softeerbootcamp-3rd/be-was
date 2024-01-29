package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.http.HttpStatus;
import util.http.HttpStatusCode;
import util.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
