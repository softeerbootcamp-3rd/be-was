package util;

import controller.GetController;
import controller.PostController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ControllerMapper {

    public static final Map<String, BiConsumer<OutputStream, String>> CONTROLLER_MAPPING = new HashMap<>();

    static {
        CONTROLLER_MAPPING.put("GET", GetController::getMethod);
        CONTROLLER_MAPPING.put("POST", PostController::postMethod);
    }

    public static BiConsumer<OutputStream, String> getController(String requestMethod) {
        return CONTROLLER_MAPPING.get(requestMethod);
    }

}
