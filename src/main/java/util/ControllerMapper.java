package util;

import controller.GetController;
import dto.RequestDto;
import dto.ResponseDto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControllerMapper {

    public static final Map<String, Function<RequestDto, ResponseDto>> CONTROLLER_MAPPING = new HashMap<>();

    static {
        CONTROLLER_MAPPING.put("GET", GetController::getMethod);
    }

    public static Function<RequestDto, ResponseDto> getController(String requestMethod) {
        return CONTROLLER_MAPPING.get(requestMethod);
    }

}
