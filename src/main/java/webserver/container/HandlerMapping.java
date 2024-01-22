package webserver.container;


import common.http.request.HttpMethod;
import common.http.request.HttpRequest;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerMapping {

    private HandlerMapping() {
    }

    private static class SingletonHelper {

        private static final HandlerMapping SINGLETON = new HandlerMapping();
    }

    public static HandlerMapping getInstance() {
        return SingletonHelper.SINGLETON;
    }

    public void process(HttpRequest httpRequest) {
        HttpMethod httpMethod = httpRequest.getHttpRequestStartLine().getHttpMethod();
        String path = httpRequest.parsingUrl();

        Map<String, String> params = httpRequest.parsingParams();
        Map<String, String> body = httpRequest.getHttpRequestBody().getBody();

        Method controllerMethod = FrontController.getInstance().findControllerMethod(httpMethod, path);
        FrontController.getInstance().invokeFunc(controllerMethod, params, body);
    }

}
