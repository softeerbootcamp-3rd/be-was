package controller;

import constants.ContentType;
import java.io.IOException;
import model.Request;
import model.Response;
import java.util.Map;
import model.User;
import service.UserService;
import utils.HtmlBuilder;
import utils.PageReader;
import utils.ParamBuilder;
import webserver.Session;

public class UserController implements Controller {

    private static final UserService userService = new UserService();

    public void route(Request request, Response response) {
        if (request.getUrl().startsWith("/user/create")) {
            createUser(request, response);
            return;
        }
        if (request.getUrl().startsWith("/user/login") && !request.getUrl().contains(".")) {
            loginUser(request, response);
            return;
        }
        if (request.getUrl().startsWith("/user/list")) {
            String cookie = request.getHeader("Cookie");
            if (cookie == null || Session.getUserBySessionId(cookie) == null) {
                response.setCode(302);
                response.addHeader("Location", "/user/login.html");
                return;
            }
        }
        getPage(request, response);
    }

    /**
     * 사용자 로그인 결과에 따라 응답 메시지를 설정합니다.
     *
     * <p> 로그인 성공시 메인 화면으로, 실패시 실패 화면으로 리다이렉션하도록 설정합니다.
     * 로그인 성공시 만료시간이 60초인 쿠키를 할당합니다.
     *
     * @param request  요청 정보
     * @param response 응답 메시지
     */
    private void loginUser(Request request, Response response) {
        Map<String, String> params = ParamBuilder.getParamFromBody(request.getBody());

        try {
            User user = userService.findUser(params);
            String sid = Session.generateSessionId();
            Session.addSession(sid, user);

            response.setCode(302);
            response.addHeader("Location", "/");
            response.addHeader("Set-Cookie", sid + "; Path=/; Max-Age=60");
        } catch (Exception e) {
            response.setCode(302);
            response.addHeader("location", "/user/login_failed.html");
        }
    }

    /**
     * 요청한 페이지 파일을 찾아 응답 메시지를 설정합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답으로, 찾을 수 없는 경우 404 응답으로 설정합니다.
     * 페이지를 동적으로 변경합니다.
     *
     * @param request 요청 정보
     * @param response 응답 메시지
     */
    private void getPage(Request request, Response response) {
        String filePath = "src/main/resources/templates" + request.getUrl();

        try {
            byte[] body = PageReader.getPage(filePath);
            body = HtmlBuilder.build(request, body).getBytes();

            response.setCode(200);
            response.setBody(body);
            response.addHeader("Content-Type", ContentType.findContentType(filePath));
            response.addHeader("Content-Length: ", String.valueOf(body.length));
        } catch (IOException e) {
            response.setCode(404);
            response.setBody(e.getMessage());
        }
    }

    /**
     * 회원가입 요청을 수행하고 결과 응답 메시지를 설정합니다.
     *
     * <p> 요청한 작업을 성공적으로 수행한다면 메인 페이지로 리다이렉션하도록 설정합니다.
     * 파라미터가 불충분하거나 이미 회원가입된 아이디라면 400 응답으로 설정합니다.
     *
     * @param request  요청 정보
     * @param response 응답 메시지
     */
    private void createUser(Request request, Response response) {
        Map<String, String> params = ParamBuilder.getParamFromBody(request.getBody());

        try {
            userService.createUser(params);
            response.setCode(302);
            response.addHeader("Location", "/index.html");
        } catch (NullPointerException | IllegalArgumentException e) {
            response.setCode(400);
            response.setBody(e.getMessage());
        }
    }
}
