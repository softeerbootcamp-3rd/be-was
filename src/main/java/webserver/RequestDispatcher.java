package webserver;

import controller.StaticResourceController;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class RequestDispatcher {
    private final StaticResourceController staticResourceController = StaticResourceController.getInstance();

    public static RequestDispatcher getInstance() {
        return RequestDispatcher.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final RequestDispatcher INSTANCE = new RequestDispatcher();
    }


    public void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        String httpMethod = request.getMethod();
        String url = request.getUrl();

        if (!Set.of("GET", "POST").contains(httpMethod)) { // 적합하지 않은 HTTP method
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            response.addHeaderProperty("Allow", "GET, POST");
        }

        // MethodMapper가 GetMapping, PostMapping 어노테이션을 통해
        // Map 형태로 httpMethod, url에 맞는 메소드를 가지고 있다.
        Method method = MethodMapper.findMethodByRequest(request);
        if (method != null) { // 요청에 적합한 메소드가 있을 때
            MethodMapper.invokeMethod(method, request, response);
            return;
        }
        // 해당하는 메소드가 없다면 정적파일 및 리다이렉팅 시도
        if (httpMethod.equals("GET")) {
            if (url.equals("/")) {
                response.setStatusCode(HttpStatus.FOUND);
                response.addHeaderProperty("Location", "/index.html");
                return;
            }
            staticResourceController.handle(request, response);
            return;
        }

        // 요청을 처리할 수 없으므로 404 Not Found 응답
        response.setStatusCode(HttpStatus.NOT_FOUND);
    }
}
