package webserver;

import http.mapping.ControllerMethodMapper;
import http.mapping.RequestMappingInfo;
import controller.UserController;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpUtils;
import utils.StaticFileUtils;
import http.session.SessionHandler;
import model.User;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import http.response.handler.LoginResponseHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final LoginResponseHandler loginResponseHandler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.loginResponseHandler = new LoginResponseHandler();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            String httpRequestString = HttpUtils.readHttpRequest(in);
            HttpRequest request = new HttpRequest(httpRequestString);
            HttpResponse response = new HttpResponse();

            // 세션 확인 및 사용자 정보 조회
            SessionHandler sessionHandler = new SessionHandler();
            User loggedInuser = sessionHandler.checkSession(request);

            // 요청에 따른 로그인 상태 처리
            loginResponseHandler.handle(request, response);

            if (response.getBody() == null) {
                // 로그인 처리 후에도 응답 본문이 설정되지 않은 경우에만 정적 파일 처리 수행
                ControllerMethodMapper controllerMethodMapper = new ControllerMethodMapper();
                UserController userController = new UserController();
                controllerMethodMapper.scan(userController);

                RequestMappingInfo requestMappingInfo = new RequestMappingInfo(request.getPath(), request.getHttpMethod());
                Method method = controllerMethodMapper.getMappedMethod(requestMappingInfo);

                if (method != null) {
                    // 매핑된 컨트롤러 메서드 호출
                    invokeControllerMethod(method, userController, request, response, loggedInuser);
                } else {
                    // 정적 파일 처리 또는 404 Not Found 처리
                    StaticFileUtils.handleStaticFile(request.getPath(), response);
                }
            }

            // 응답 전송
            response.send(dos);
        } catch (IOException e) {
            logger.error("요청 처리 에러:", e);
        }
    }

    private void invokeControllerMethod(Method method, UserController userController, HttpRequest request, HttpResponse response, User loggedInUser) {
        try {
            // 메서드가 필요로 하는 매개변수 타입들을 가져옵니다.
            Class<?>[] parameterTypes = method.getParameterTypes();

            // 필요한 매개변수에 따라 적절한 인자들을 전달합니다.
            if (Arrays.asList(parameterTypes).contains(User.class)) {
                // User 객체가 필요한 경우
                method.invoke(userController, request, response, loggedInUser);
            } else {
                // User 객체가 필요하지 않은 경우
                method.invoke(userController, request, response);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("컨트롤러 메서드 호출 에러", e);
        }
    }

}
