package webserver;

import controller.Controller;
import exception.NotSupportedContentTypeException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpRequest;
import http.HttpResponse;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static http.HttpStatus.*;
import static java.lang.invoke.MethodType.methodType;

public class DispatcherServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final DispatcherServlet servlet = new DispatcherServlet();

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return servlet;
    }

    //클라이언트의 요청 처리
    public void doService(HttpRequest request, OutputStream out) throws Throwable {
        logger.debug("Request 시작\n{}", request);
        doDispatch(request, out);
    }

    public void doDispatch(HttpRequest request, OutputStream out) throws Throwable {
        Method method = HandlerMapping.getMethodMapped(request);
        HttpResponse response = handle(request, method, request.getUserInSession());
        processDispatchResult(response, out);
        logger.debug("Response 끝\n{}", response);
    }

    //요청 처리 및 응답 생성
    private HttpResponse handle(HttpRequest request, Method method, User user) throws Throwable {
        String path = request.getPath();
        HttpResponse response = hasRequestPathMapped(method) ? executeRequest(request, method) : HttpResponse.init(path);
        processResources(response, user);
        return response;
    }

    private static boolean hasRequestPathMapped(Method method) {
        return method != null;
    }

    //컨트롤러 메소드 실행 및 응답 생성
    private HttpResponse executeRequest(HttpRequest request, Method method) throws Throwable {
        MethodHandle methodHandle = getMethodHandle(method);
        HttpResponse response;
        if (hasParameter(methodHandle.type())) {
            Map<String, String> map = (request.isGetMethod()) ? request.getQuery() : request.getBody();
            response = (HttpResponse) methodHandle.invoke(map);
        } else {
            response = (HttpResponse) methodHandle.invoke();
        }
        return response;
    }

    private void processResources(HttpResponse response, User user) throws IOException {
        try {
            ContentType type = ContentType.findBy(response.getFilePath());
            response.mapResourcePath(type);
            response.setResponse(user);
        } catch (NotSupportedContentTypeException e) {
            logger.debug("NotSupportedContentTypeException >> {}", response);
            logger.error(e.getMessage());
            response.setResponse(FOUND);
        } catch (IOException e) {
            logger.debug("IOException (readAllBytes ERROR) >> {}", response);
            logger.error(Arrays.toString(e.getStackTrace()));
            response.setResponse(NOT_FOUND);
        }
    }

    private MethodHandle getMethodHandle(Method method) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = (hasParameter(method)) ? methodType(HttpResponse.class, method.getParameterTypes()) : methodType(HttpResponse.class);
        return MethodHandles.lookup()
                .findVirtual(Controller.class, method.getName(), methodType)
                .bindTo(new Controller());
    }

    private boolean hasParameter(MethodType methodType) {
        return methodType.parameterCount() != 0;
    }

    private boolean hasParameter(Method method) {
        return method.getParameterCount() != 0;
    }

    private void processDispatchResult(HttpResponse response, OutputStream out) {
        response.writeResponseToOutputStream(out);
    }
}
