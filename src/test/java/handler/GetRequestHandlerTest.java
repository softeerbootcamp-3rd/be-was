package handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parser.GetRequestParser;
import webserver.request.GetRequest;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class GetRequestHandlerTest {
    @Test
    @DisplayName("/test를 요청할 시 정상적으로 TestController의 test()가 실행되는지 테스트")
    void requestTestMethodTest() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        String request = "/test";
        GetRequest getRequest = GetRequestParser.parse(request);

        Object object = GetRequestHandler.run(getRequest);
        String result = (String) object;

        assertEquals("test", result);
    }
}