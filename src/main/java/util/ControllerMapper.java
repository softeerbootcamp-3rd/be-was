package util;

import controller.GetController;
import dto.RequestBuilder;
import dto.ResponseBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControllerMapper {

    public static final Map<String, Function<RequestBuilder, ResponseBuilder>> CONTROLLER_MAPPING = new HashMap<>();

    static {
        CONTROLLER_MAPPING.put("GET", GetController::getMethod);
    }

    public static Function<RequestBuilder, ResponseBuilder> getController(String requestMethod) {
        return CONTROLLER_MAPPING.get(requestMethod);
    }

}
