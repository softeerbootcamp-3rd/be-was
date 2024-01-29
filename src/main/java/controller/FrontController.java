package controller;

import annotation.RequestMapping;
import controller.dto.InputData;
import controller.dto.OutputData;
import http.HttpRequest;
import http.HttpResponse;
import view.View;
import view.ViewMaker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FrontController {
    private final ControllerContainer container;
    private ViewMaker viewMaker;
    private final String RESOURCES_TEMPLATES_URL = "src/main/resources/templates";
    private final String RESOURCES_STATIC_URL = "src/main/resources/static";

    public FrontController() {
        container = ControllerContainer.getInstance();
    }

    public void process(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        String url = request.getUrl();
        String path = null;

        if (url.contains(".")) {
            if (url.endsWith(".html")) {
                path = RESOURCES_TEMPLATES_URL + url;
            } else { //.js .css .g..
                path = RESOURCES_STATIC_URL + url;
                response.setPath(path);
                return;
            }
        } else {
            String[] urlToken = url.split("/");
            Controller controller = container.get("/"+urlToken[1]); //적합한 controller 찾기
            Method method = findMethod(controller, url,request.getMethod()); //찾은 컨트롤러에서 메서드 찾기

            InputData inputData;
            if (request.getMethod().equals("POST")) {
                inputData = new InputData(request.getFormData());
            } else if (request.getMethod().equals("GET")) {
                inputData = new InputData(request.getRequestParam());
            } else {
                inputData = null;
            }

            OutputData outputData = new OutputData();
            path = (String)method.invoke(controller, inputData, outputData); //메서드 실행
            path += ".html";

            if (outputData.headerExists()) {
                Map<String, String> headerMap = outputData.getHeaderMap();
                for (String key : headerMap.keySet()) {
                    response.setHeader(key, headerMap.get(key));
                }
            }
        }
        response.setPath(path);

        if(!path.startsWith("redirect:")) {
            View view = new View();
            viewMaker = new ViewMaker(path, view);
            String bodyString = viewMaker.readFile(request);
            response.readString(bodyString);
        }
    }

    public Method findMethod(Controller controller, String url, String method) {

        Class<?> clazz = controller.getClass();
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMappingAnnotation = m.getAnnotation(RequestMapping.class);
                if (url.equals(requestMappingAnnotation.value()) && method.equals(requestMappingAnnotation.method())) {
                    return m;
                }
            }
        }
        return null;
    }
}