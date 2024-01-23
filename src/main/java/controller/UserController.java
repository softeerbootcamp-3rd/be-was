package controller;

import java.util.HashMap;
import model.Request;
import model.Response;
import java.util.Map;
import service.UserService;
import utils.PageReader;
import utils.ParamBuilder;

public class UserController implements Controller {

    private static final UserService userService = new UserService();

    public Response route(Request request) {
        if (request.getUrl().startsWith("/user/create")) {
            return createUser(request);
        }
        if (request.getUrl().startsWith("/user/login") && !request.getUrl().contains(".")) {
            return loginUser(request);
        }
        return getPage(request.getUrl());
    }

    /**
     * 사용자 로그인 결과에 따라 페이지를 이동시킵니다.
     *
     * <p> 로그인 성공시 메인 화면으로 이동하며, 실패시 실패 화면으로 이동합니다.
     * @param request 요청 정보
     * @return 요청을 수행한 결과를 담은 응답
     */
    private Response loginUser(Request request) {
        Map<String, String> params = ParamBuilder.getParamFromBody(request.getBody());
        if (userService.loginFail(params)) {
            String location = "/user/login_failed.html";
            return new Response(302, location);
        }
        String sid = "123456";
        String location = "/";
        return new Response(302, location, Map.of("sid", sid));
    }

    /**
     * 요청한 페이지 파일을 찾아 응답을 반환합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답을 반환하며, 요청한 파일을 찾을 수 없는 경우 404 응답을 반환합니다.
     *
     * @param url 요청 정보
     * @return 요청을 수행한 결과를 담은 응답
     */
    private Response getPage(String url) {
        String filePath = "src/main/resources/templates" + url;
        return PageReader.getPage(filePath, filePath);
    }

    /**
     * 회원가입 요청을 수행하고 결과 응답을 반환합니다.
     *
     * <p> 요청한 작업을 성공적으로 수행한다면 메인 페이지로 리다이렉션하는 응답을 반환합니다.
     * 파라미터가 불충분하거나 이미 회원가입된 아이디라면 400 응답을 반환합니다.
     *
     * @param request 요청 정보
     * @return 요청을 수행한 결과를 담은 응답
     */
    private Response createUser(Request request) {
        Map<String, String> params = ParamBuilder.getParamFromBody(request.getBody());

        try {
            userService.createUser(params);
        } catch (NullPointerException e) {
            byte[] body = "cannot find parameter.".getBytes();
            return new Response(400, body);
        } catch (IllegalArgumentException e) {
            byte[] body = "user id already exists.".getBytes();
            return new Response(400, body);
        }

        String location = "/index.html";
        return new Response(302, location);
    }
}
