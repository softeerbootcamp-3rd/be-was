package webserver.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.adapter.GetRequestAdapter;

import static org.junit.jupiter.api.Assertions.*;

class GetRequestAdapterTest {
    @Test
    @DisplayName("/test를 요청할 시 정상적으로 TestController의 test()가 실행되는지 테스트")
    void requestTestMethodTest() throws Throwable {
        String request = "/test";
        GetRequest getRequest = RequestParameterParser.parse(request);

        Object object = GetRequestAdapter.run(getRequest);
        String result = (String) object;

        assertEquals("test", result);
    }
}