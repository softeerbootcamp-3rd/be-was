package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class RequestUrlTest {
    @Test
    void 쿼리파라미터없을때() {
        //Given
        String firstLine = "GET /index.html HTTP/1.1";

        //When
        RequestUrl url = RequestUrl.of(firstLine);

        //Then
        assertEquals("GET", url.getMethod());
        assertEquals("/index.html", url.getPath());
        assertNull(url.getQuery());
    }

    @Test
    void 쿼리파라미터있을때() {
        //Given
        String firstLine = "GET /user/create?userId=gyeongsu&password=1234&name=gyeongsu&email=gyeongsu@hyundai.com HTTP/1.1";

        //When
        RequestUrl url = RequestUrl.of(firstLine);

        //Then
        assertEquals("GET", url.getMethod());
        assertEquals("/user/create", url.getPath());

        HashMap<String, String> query = url.getQuery();
        assertEquals("gyeongsu", query.get("userId"));
        assertEquals("1234", query.get("password"));
        assertEquals("gyeongsu", query.get("name"));
        assertEquals("gyeongsu@hyundai.com", query.get("email"));
    }
}