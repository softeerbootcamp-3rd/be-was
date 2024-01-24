package webserver.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exception.GeneralException;
import webserver.request.Request;
import webserver.response.Response;

import static org.junit.jupiter.api.Assertions.*;

class PostRequestAdapterTest {
    private final PostRequestAdapter postRequestAdapter = PostRequestAdapter.getInstance();

    @Test
    @DisplayName("/test를 요청할 시 정상적으로 TestController의 postTest()가 실행되는지 테스트")
    void requestTestMethodTest() throws Throwable {
        Request request = Request.of("POST", "/test", "HTTP/1.1");

        Response result = postRequestAdapter.run(request);

        assertEquals(true, isSame("post test".getBytes(), result.getBody()));
    }

    @Test
    @DisplayName("/test1를 요청할 시 예외 발생 테스트")
    void requestTes1tMethodTest() throws Throwable {
        Request request = Request.of("POST", "/test1", "HTTP/1.1");

        assertThrows(GeneralException.class, () -> {
            Response result = postRequestAdapter.run(request);
        });
    }

    @Test
    @DisplayName("POST /test를 요청할 때 canRun() 테스트")
    void requestTestCanRunTest() {
        Request request = Request.of("POST", "/test", "HTTP/1.1");

        assertEquals(true, postRequestAdapter.canRun(request));
    }

    @Test
    @DisplayName("GET /test를 요청할 때 canRun() 테스트")
    void requestTestCannotRunTest() {
        Request request = Request.of("GET", "/test", "HTTP/1.1");

        assertEquals(false, postRequestAdapter.canRun(request));
    }

    private boolean isSame(byte[] expected, byte[] actual){
        if(expected.length != actual.length){
            return false;
        }

        for(int i = 0; i < expected.length; i++){
            if(expected[i] != actual[i]){
                return false;
            }
        }

        return true;
    }
}