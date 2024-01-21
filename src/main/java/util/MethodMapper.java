package util;

import annotation.GetMapping;
import dto.RequestDto;
import exception.WebServerException;
import webserver.ResponseHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {
    private static final Map<String, Method> CONTROLLER_METHOD = new HashMap<>();

    static {
        registerMethod();
    }

    // 요청이 들어온 HTTP 메소드와 경로에 매팽되어 있는 컨트롤러_메소드가 있는지 확인
    public static boolean hasMethod(String methodAndPath) {
        return CONTROLLER_METHOD.containsKey(methodAndPath);
    }

    // 요청에 맞게 매핑되는 컨트롤러_메소드 를 실행
    public static void execute(DataOutputStream dos, RequestDto requestDto) {
        Method method = CONTROLLER_METHOD.get(requestDto.getMethodAndPath());
        Class<?> controller = method.getDeclaringClass();

        try {
            method.invoke(controller, dos, requestDto);
        } catch (InvocationTargetException | IllegalAccessException e) {
            WebServerException wasE = (WebServerException) e.getCause();
            ResponseHandler.sendError(dos, wasE.getErrorCode());
        }
    }

    public static void registerMethod() {
        // 컨트롤러 클래스들을 작성하는 controller 폴더 경로
        File folder = new File("src/main/java/controller");
        File[] files = folder.listFiles();

        // controller 폴더에 있는 컨트롤러 클래스들을 가져와 controllers 에 추가
        ArrayList<Class<?>> controllers = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String className = file.getName().replace(".java", "");
                try {
                    Class<?> controllerClass = Class.forName("controller." + className);
                    controllers.add(controllerClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        // 위에서 불러온 컨트롤러 클래스들을 for 문을 돌며
        // 각 클래스의 메소드들을 모두 불러오고
        // GetMapping 어노테이션이 있는지 확인하고 ("GET " + 어노테이션에 명시한 경로)를 키 값으로 하고
        // 메소드를 벨류로 하여 CONTROLLER_METHOD 에 put
        for (Class<?> controller : controllers) {
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                String httpMethod = "", path = "";

                if (method.isAnnotationPresent(GetMapping.class)) {
                    httpMethod = "GET";
                    path = (method.getAnnotation(GetMapping.class)).path();
                }
//             TODO : POST, PUT 등 다른 요청에 대한 처리 코드 추가
//                else if (method.isAnnotationPresent(PostMapping.class)) {
//                }

                CONTROLLER_METHOD.put((httpMethod + " " + path), method);
            }
        }

    }
}
