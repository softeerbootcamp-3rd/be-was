package parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.request.GetRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GetRequestParserTest {
    @Test
    @DisplayName("GET 요청에서 path와 parameter가 파싱되는지 테스트")
    void parsePathAndParametersTest(){
        String request = "/user/create?userId=user&password=password&name=name&email=email";

        GetRequest getRequest = GetRequestParser.parse(request);
        Map<String, String> map = getRequest.getParamsMap();

        assertEquals("/user/create", getRequest.getPath());
        assertEquals("user", map.get("userId"));
        assertEquals("password", map.get("password"));
        assertEquals("name", map.get("name"));
        assertEquals("email", map.get("email"));
    }

    @Test
    @DisplayName("GET 요청에서 parameter가 중복될 때 예외 테스트")
    void parseDuplicatedParameterTest(){
        String request = "/user/create?userId=user&userId=userId&name=name&email=email";

        assertThrows(IllegalArgumentException.class, () -> {
            GetRequest getRequest = GetRequestParser.parse(request);
        });
    }
}