package util;

import controller.UserController;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class URLMapper {

    public static final Map<String, Function<HttpRequest, HttpResponse>> URL_MAPPING = new HashMap<>();

    static {
        URL_MAPPING.put("GET /user/create", UserController::createUser);
    }

    public static Function<HttpRequest, HttpResponse> getMethod(HttpRequest request) {
        return URL_MAPPING.get(request.getMethod() + " " + URIParser.extractPath(request.getURI()));
    }
}
