package webserver.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.parser.GetRequestParser;
import webserver.handler.GetRequestHandler;
import webserver.request.GetRequest;

import static org.junit.jupiter.api.Assertions.*;

class GetRequestHandlerTest {
    @Test
    @DisplayName("/test를 요청할 시 정상적으로 TestController의 test()가 실행되는지 테스트")
    void requestTestMethodTest() throws Throwable {
        String request = "/test";
        GetRequest getRequest = GetRequestParser.parse(request);

        Object object = GetRequestHandler.run(getRequest);
        String result = (String) object;

        assertEquals("test", result);
    }
}