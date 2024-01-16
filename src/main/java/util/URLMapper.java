package util;

import controller.UserController;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class URLMapper {

    public static final Map<String, BiConsumer<OutputStream, HttpRequest>> URL_MAPPING = new HashMap<>();

    static {
        URL_MAPPING.put("GET /user/create", UserController::createUser);
    }

    public static BiConsumer<OutputStream, HttpRequest> getMethod(HttpRequest request) {
        return URL_MAPPING.get(request.getMethod() + " " + URIParser.extractPath(request.getURI()));
    }
}
