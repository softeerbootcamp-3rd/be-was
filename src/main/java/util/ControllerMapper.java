package util;

import controller.GetController;
import controller.PostController;
import dto.RequestDto;
import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ControllerMapper {

    public static final Map<String, Function<RequestDto, ResponseDto>> CONTROLLER_MAPPING = new HashMap<>();

    static {
        CONTROLLER_MAPPING.put("GET", GetController::getMethod);
        CONTROLLER_MAPPING.put("POST", PostController::postMethod);
    }

    public static Function<RequestDto, ResponseDto> getController(String requestMethod) {
        return CONTROLLER_MAPPING.get(requestMethod);
    }

}
