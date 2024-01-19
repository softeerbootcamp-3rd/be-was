package handler;


import http.request.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import service.Service;
import service.user.UserServiceFactory;

public class DynamicRequestHandler {

    private DynamicRequestHandler() {
    }

    private static class SingletonHelper {

        private static final DynamicRequestHandler SINGLETON = new DynamicRequestHandler();
    }

    public static DynamicRequestHandler getInstance() {
        return SingletonHelper.SINGLETON;
    }

    public byte[] process(HttpRequest httpRequest) {
        String path = parsingUrl(httpRequest);
        Map<String, String> params = parsingParams(httpRequest);

        if (path.startsWith("/user")) {
            UserServiceFactory userServiceFactory = UserServiceFactory.getInstance();
            Service userService = userServiceFactory.createService(path);
            return userService.execute(
                httpRequest.getHttpRequestStartLine().getHttpMethod(),
                params,
                httpRequest.getHttpRequestBody().getBody()
            );
        } // 또 다른 도메인 서비스가 추가되면 여기에 추가
        else {
            throw new IllegalArgumentException("Unsupported URL: " + path);
        }
    }

    private String parsingUrl(HttpRequest httpRequest) {
        String path = httpRequest.getHttpRequestStartLine().getRequestTarget();
        return path.split("\\?")[0];
    }

    private Map<String, String> parsingParams(HttpRequest httpRequest) {
        String path = httpRequest.getHttpRequestStartLine().getRequestTarget();
        if (!path.contains("?")) {
            return new HashMap<>();
        }
        String params = path.split("\\?")[1];

        Map<String, String> paramsMap = new HashMap<>();
        Arrays.stream(params.split("&"))
            .map(param -> param.split("="))
            .forEach(param -> paramsMap.put(param[0], param[1]));
        return paramsMap;
    }

}
