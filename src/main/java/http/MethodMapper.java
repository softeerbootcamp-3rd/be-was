package http;

import controller.UserController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {

    private static final Map<String, Method> methodMapper = new HashMap<>();

    static {
        try {
            methodMapper.put("POST /user/create", UserController.class.getDeclaredMethod("create", String.class));
            methodMapper.put("POST /user/login", UserController.class.getDeclaredMethod("login", String.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(String path) {
        return methodMapper.get(path);
    }
}
