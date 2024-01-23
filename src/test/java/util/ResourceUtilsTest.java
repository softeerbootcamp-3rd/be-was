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
    void notFoundExceptionHandler() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        ResourceUtils resourceUtils = new ResourceUtils();
        Class<ResourceUtils> clazz = ResourceUtils.class;
        Method method = clazz.getDeclaredMethod("notFoundExceptionHandler");
        method.setAccessible(true);

        //when
        ResponseEntity<?> responseEntity = (ResponseEntity<?>)method.invoke(resourceUtils);

        //then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getExtension() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        ResourceUtils resourceUtils = new ResourceUtils();
        Class<ResourceUtils> clazz = ResourceUtils.class;
        Method method = clazz.getDeclaredMethod("getExtension", String.class);
        method.setAccessible(true);

        //when
        String extension = (String)method.invoke(resourceUtils, "/index.html");

        //then
        Assertions.assertEquals("html", extension);
    }
}
