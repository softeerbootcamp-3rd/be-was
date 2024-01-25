package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class UrlParserTest {
    @Test
    @DisplayName("정상적인 query인 경우 동작 확인")
    public void testValidQuery() {
        String queryString = "param1=value1&param2=value2";

        Map<String, String> queryParams = Parser.parseQueryString(queryString);

        assertEquals(2, queryParams.size());
        assertEquals("value1", queryParams.get("param1"));
        assertEquals("value2", queryParams.get("param2"));
    }

    @Test
    @DisplayName("정상적인 query인 경우 동작 확인")
    public void testExampleEncodeQuery() {
        String queryString = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        Map<String, String> queryParams = Parser.parseQueryString(queryString);

        assertEquals(4, queryParams.size());
        assertEquals("javajigi", queryParams.get("userId"));
        assertEquals("password", queryParams.get("password"));
        assertEquals("박재성", queryParams.get("name"));
        assertEquals("javajigi@slipp.net", queryParams.get("email"));
    }

    @Test
    @DisplayName("value가 주어지지 않은 경우 key도 저장하지 않음")
    public void testWithoutValue() {
        String queryString = "param1=value1&param2=";

        Map<String, String> queryParams = Parser.parseQueryString(queryString);

        assertEquals(1, queryParams.size());
        assertEquals("value1", queryParams.get("param1"));
        assertFalse(queryParams.containsKey("param2"));
    }
    @Test
    @DisplayName("value가 주어지지 않은 경우 key도 저장하지 않음")
    public void testWithoutValue2() {
        String queryString = "param1value1&param2=value2";

        Map<String, String> queryParams = Parser.parseQueryString(queryString);

        assertEquals(1, queryParams.size());
        assertEquals("value2", queryParams.get("param2"));
        assertFalse(queryParams.containsKey("param1value1"));
    }

    @Test
    @DisplayName("query가 빈 경우 아무것도 저장되지 않음")
    public void testWithoutQuery() {
        String queryString = "";

        Map<String, String> queryParams = Parser.parseQueryString(queryString);

        assertTrue(queryParams.isEmpty());
    }


}