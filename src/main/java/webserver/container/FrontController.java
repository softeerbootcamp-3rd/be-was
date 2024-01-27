package webserver.container;

import common.annotation.RequestBody;
import common.annotation.RequestMapping;
import common.annotation.RequestParam;
import common.http.request.HttpMethod;
import common.http.response.HttpStatusCode;
import domain.user.presentation.UserController;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FrontController {

    private static final Set<Class<?>> CONTROLLERS = new HashSet<>();
    private static final Map<String, Method> CONTROLLER_METHODS = new HashMap<>();

    private FrontController() {
    }

    private static class SingletonHelper {

        private static final FrontController SINGLETON = new FrontController();
    }

    public static FrontController getInstance() {
        return SingletonHelper.SINGLETON;
    }

    static {
        CONTROLLERS.add(UserController.class);
        initializeControllerMethods();
    }

    private static void initializeControllerMethods() {
        for (Class<?> controllerClass : CONTROLLERS) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String path = requestMapping.path();
                    HttpMethod httpMethod = requestMapping.method();

                    String key = httpMethod + ":" + path;
                    CONTROLLER_METHODS.put(key, method);
                }
            }
        }
    }

    public Method findControllerMethod(HttpMethod method, String path) {
        String key = method + ":" + path;
        if (CONTROLLER_METHODS.containsKey(key)) {
            return CONTROLLER_METHODS.get(key);
        }

        throw new IllegalArgumentException("Not found controller method");
    }

    public void invokeFunc(Method method, Map<String, String> params, Map<String, String> body) {
        try {
            Class<?> targetClass = method.getDeclaringClass();
            Object newInstance = targetClass.getDeclaredConstructor().newInstance();

            Parameter[] parameters = method.getParameters();
            Object[] parameterValues = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    parameterValues[i] = mapToRequestDto(method, body);
                } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                    Parameter param = parameters[i];
                    String paramName = param.getName();
                    parameterValues[i] = params.get(paramName);
                } else { // TODO: 핸들링 제대로 하기
                    String paramName = parameter.getName();
                    parameterValues[i] = params.get(paramName);
                }
            }

            method.invoke(newInstance, parameterValues);
        } catch (Exception e) {
            CustomThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, null, "Bad Request".getBytes());
        }

    }

    private Object mapToRequestDto(Method method, Map<String, String> body)
        throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[0];

        Class<?> dtoClass = parameter.getType();
        Constructor<?> dtoClassDeclaredConstructor = dtoClass.getDeclaredConstructors()[0];

        Parameter[] dtoConstructorParameters = dtoClassDeclaredConstructor.getParameters();
        Object[] parameterValues = new Object[dtoConstructorParameters.length];

        for (int i = 0 ; i<dtoConstructorParameters.length ; i++) {
            parameterValues[i] = body.get(dtoConstructorParameters[i].getName());
        }

        return dtoClassDeclaredConstructor.newInstance(parameterValues);
    }

}
