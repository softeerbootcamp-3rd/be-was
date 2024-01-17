package util;

import controller.UserController;
import model.HttpRequest;
import model.HttpResponse;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class URLMapper {
    public static final Map<String, Function<HttpRequest, HttpResponse>> URL_MAPPING = new HashMap<>();

    static {
        URL_MAPPING.put("GET /user/create", UserController::createUser);
    }

    public static Function<HttpRequest, HttpResponse> getController(HttpRequest httpRequest) {
        return URL_MAPPING.get(httpRequest.getMethod() + " " + httpRequest.getUri().getPath());
    }

}
