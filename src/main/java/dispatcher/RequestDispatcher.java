package dispatcher;

import controller.StaticResourceController;
import http.HttpRequest;
import http.HttpResponse;
import http.header.ResponseHeader;
import http.status.HttpStatus;
import utils.MethodMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class RequestDispatcher {
    private final StaticResourceController staticResourceController = StaticResourceController.getInstance();

    public static RequestDispatcher getInstance() {
        return RequestDispatcher.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final RequestDispatcher INSTANCE = new RequestDispatcher();
    }


    public void dispatchHandler(HttpRequest request, HttpResponse response) throws IOException {
        String httpMethod = request.getMethod();
        String url = request.getUrl();

        Method method = MethodMapper.findMethodByRequest(request);
        if (method != null) { // 요청에 적합한 메소드가 있을 때
            MethodMapper.invokeMethod(method, request, response);
            return;
        }
        // 정적파일 및 리다이렉팅
        if (httpMethod.equals("GET")) {
            if (url.equals("/")) {
                response.setEmptyBody();
                response.setHeader(
                        ResponseHeader.of(HttpStatus.FOUND, Map.of("Location", "/index.html"))
                );
                return;
            }
            staticResourceController.handle(request, response);
            return;
        }

        response.setEmptyBody();
        response.setHeader(
                ResponseHeader.of(HttpStatus.METHOD_NOT_ALLOWED, Map.of("Allow", "GET, POST"))
        );
    }
}
