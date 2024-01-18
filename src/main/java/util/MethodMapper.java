package webserver;

import controller.UserController;
import dto.RequestDto;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MethodMapper {
    private static final Map<String, BiConsumer<DataOutputStream, RequestDto>> CONTROLLER_METHOD = new HashMap<>();

    // "[http 메소드] [경로]" 와 그에 맞는 컨트롤러 메소드를 초기화
    static {
        CONTROLLER_METHOD.put("GET /user/create", UserController::create);
    }

    public static BiConsumer<DataOutputStream, RequestDto> getMethod(String httpMethodAndUri) {
        return CONTROLLER_METHOD.get(httpMethodAndUri);
    }
}
