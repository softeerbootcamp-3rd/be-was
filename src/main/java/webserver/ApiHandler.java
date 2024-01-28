package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import constant.ErrorCode;
import exception.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler {
    /*
    handle 안에서
    DynamicHtmlController 를 하나 생성하고 여기다가 동적 html 관련 메소드....? 근데 생각해보면... 거의 모든 html은 동적이지만 css는 정적임 정적 그대로 두고..
    html 가져오고 - 컨트롤러 내부에다가...? 공통적으로 nav 를 세팅하는 메소드를 둬야 하나...
    그리고 여기 핸들러에
    일단 동적 컨텐츠 처리 - .html 인 경우에 확인
     */


    private static final Logger logger = LoggerFactory.getLogger(ApiHandler.class);
    private static final Map<String, Method> CONTROLLER_METHOD = new HashMap<>();

    static {
        registerMethod();
    }

    /**
     * <h3> 동적 요청을 처리 </h3>
     * <p> 요청에 맞는 컨트롤러 메소드가 있는지 확인 후 실행하여 HttpResponse 를 반환 </p>
     *
     * @param httpRequest
     * @return
     */
    public static HttpResponse handle(HttpRequest httpRequest) {
        Method method = CONTROLLER_METHOD.get(httpRequest.getMethod() + httpRequest.getPath());

        if (method != null) {
            HttpResponse httpResponse = new HttpResponse();
            Class<?> controller = method.getDeclaringClass();

            try {
                httpResponse = (HttpResponse) method.invoke(controller, httpRequest);
            } catch (InvocationTargetException | IllegalAccessException e) {
                if (e.getCause().getClass().equals(WebServerException.class)) {
                    WebServerException wasE = (WebServerException) e.getCause();
                    httpResponse.makeError(wasE.getErrorCode());
                } else {
                    logger.error(e.getCause().getMessage());
                    httpResponse.makeError(ErrorCode.SERVER_ERROR);
                }
            }
            return httpResponse;
        }
        return null;
    }

    /**
     * <h3> CONTROLLER_METHOD 초기화 </h3>
     * <p> 어노테이션을 이용해 컨트롤러 클래스에 작성한 메소드들을 그에 맞는 요청과 매핑 </p>
     */
    public static void registerMethod() {
        // 컨트롤러 클래스들을 작성하는 controller 폴더 경로
        File folder = new File("src/main/java/controller");
        File[] files = folder.listFiles();

        // controller 폴더에 있는 컨트롤러 클래스 파일들을 가져와 클래스명을 이용해 해당 클래스를 controllers 에 추가
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
                    path = (method.getAnnotation(GetMapping.class)).value();
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    httpMethod = "POST";
                    path = (method.getAnnotation(PostMapping.class)).value();
                }
                // TODO : DELETE 등 다른 요청에 대한 처리 코드 추가

                CONTROLLER_METHOD.put((httpMethod + path), method);
            }
        }
    }
}
