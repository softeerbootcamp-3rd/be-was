package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

class UrlParserTest {
    @Test
    @DisplayName("정상적인 query인 경우 동작 확인")
    public void testParseQueryStringWithValidInput() {
        String queryString = "param1=value1&param2=value2";

        Map<String, String> queryParams = UrlParser.parseQueryString(queryString);

        assertEquals(2, queryParams.size());
        assertEquals("value1", queryParams.get("param1"));
        assertEquals("value2", queryParams.get("param2"));
    }

    @Test
    @DisplayName("value가 주어지지 않은 경우 key도 저장하지 않음")
    public void testParseQueryStringWithEmptyValue() {
        String queryString = "param1=value1&param2=";

        Map<String, String> queryParams = UrlParser.parseQueryString(queryString);

        assertEquals(1, queryParams.size());
        assertEquals("value1", queryParams.get("param1"));
        assertFalse(queryParams.containsKey("param2"));
    }
    @Test
    @DisplayName("value가 주어지지 않은 경우 key도 저장하지 않음")
    public void testParseQueryStringWithInvalidFormat() {
        String queryString = "param1value1&param2=value2";

        Map<String, String> queryParams = UrlParser.parseQueryString(queryString);

        assertEquals(1, queryParams.size());
        assertEquals("value2", queryParams.get("param2"));
        assertFalse(queryParams.containsKey("param1value1"));
    }

    @Test
    @DisplayName("query가 빈 경우 아무것도 저장되지 않음")
    public void testParseQueryStringWithEmptyInput() {
        String queryString = "";

        Map<String, String> queryParams = UrlParser.parseQueryString(queryString);

        assertTrue(queryParams.isEmpty());
    }


}