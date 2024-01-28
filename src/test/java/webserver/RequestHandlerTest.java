package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    @Test
    @DisplayName("Body 내용을 stream으로 성공적으로 읽는지 테스트")
    public void bodyParseTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        // given
        String bodyData = "userId=javajigi&password=password&name=jaesung&email=javajigi@slipp.net";
        InputStream is = new ByteArrayInputStream(bodyData.getBytes());

        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Class<RequestHandler> requestHandlerClass = RequestHandler.class;
        RequestHandler requestHandler = new RequestHandler(new Socket());

        Method parseBodyMethod = requestHandlerClass.getDeclaredMethod("parseBody", BufferedReader.class, int.class);
        parseBodyMethod.setAccessible(true);

        // when
        String result = (String) parseBodyMethod.invoke(requestHandler, br, bodyData.length());

        // then
        Assertions.assertEquals(bodyData, result);
    }
}