package util;

import annotation.GetMapping;
import annotation.PostMapping;
import constant.ErrorCode;
import dto.RequestDto;
import dto.ResponseDto;
import exception.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {
    private static final Logger logger = LoggerFactory.getLogger(MethodMapper.class);
    private static final Map<String, Method> CONTROLLER_METHOD = new HashMap<>();

    static {
        registerMethod();
    }


    /**
     * <h3> 요청의 HTTP 메소드와 경로에 매핑되는 컨트롤러_메소드가 있는지 확인 </h3>
     * @param methodAndPath
     * @return 요청에 매핑되는 컨트롤러 메소드가 있는지 boolean
     */
    public static boolean hasMethod(String methodAndPath) {
        return CONTROLLER_METHOD.containsKey(methodAndPath);
    }


    /**
     * <h3> 요청에 매핑되는 컨트롤러의 메소드를 실행 </h3>
     * @param requestDto
     * @return
     */
    public static ResponseDto execute(RequestDto requestDto) {
        Method method = CONTROLLER_METHOD.get(requestDto.getMethodAndPath());
        Class<?> controller = method.getDeclaringClass();
        ResponseDto response = new ResponseDto();

        try {
            response = (ResponseDto) method.invoke(controller, requestDto);
        } catch (InvocationTargetException | IllegalAccessException e) {
            if (e.getCause().getClass().equals(WebServerException.class)) {
                WebServerException wasE = (WebServerException) e.getCause();
                response.makeError(wasE.getErrorCode());
            } else {
                // TODO
                e.printStackTrace();
                response.makeError(ErrorCode.PAGE_NOT_FOUND);
            }
        }

        return response;
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
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    httpMethod = "POST";
                    path = (method.getAnnotation(PostMapping.class)).path();
                }
                // TODO : DELETE 등 다른 요청에 대한 처리 코드 추가

                CONTROLLER_METHOD.put((httpMethod + " " + path), method);
            }
        }
    }
}
