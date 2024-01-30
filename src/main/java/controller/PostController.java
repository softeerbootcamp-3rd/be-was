package controller;

import constants.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import model.Request;
import model.Response;
import model.User;
import service.PostService;
import utils.HtmlBuilder;
import utils.PageReader;
import utils.ParamBuilder;
import webserver.Session;

public class PostController implements Controller {

    PostService postService = new PostService();

    public void route(Request request, Response response) {
        String cookie = request.getHeader("Cookie");
        if (cookie != null && Session.getUserBySessionId(cookie) != null) {
            response.addHeader("Set-Cookie", cookie + "; Path=/; Max-Age=600");
        } else {
            response.setCode(302);
            response.addHeader("Location", "/user/login.html");
            return;
        }

        if (request.getUrl().startsWith("/post/write")) {
            if (request.getMethod().equals("POST")) {
                post(request, response);
                return;
            }
        }
        if (request.getUrl().endsWith("show.html")) {
            showPost(request, response);
            return;
        }
        getPage(request, response);
    }

    /**
     * 글의 세부 내용을 불러오는 요청을 수행하고 응답 메시지를 설정합니다.
     *
     * <p> 정상적으로 불러온다면 200 응답으로, 불러오지 못한 경우 404 응답으로 설정합니다.
     *
     * @param request 요청 정보
     * @param response 응답 메시지
     */
    private void showPost(Request request, Response response) {
        String filePath = "src/main/resources/templates/post/show.html";

        try {
            byte[] body = PageReader.getPage(filePath);
            body = HtmlBuilder.build(request, body).getBytes();

            response.setCode(200);
            response.setBody(body);
            response.addHeader("Content-Type", ContentType.findContentType(filePath));
            response.addHeader("Content-Length: ", String.valueOf(body.length));
        } catch (IOException e) {
            response.setCode(404);
        }
    }

    /**
     * 작성한 글을 저장하는 요청을 수행하고 응답 메시지를 설정합니다.
     *
     * <p> 정상적으로 저장이 되었다면 200 응답으로, 필수적인 제목이 비어있다면 400 응답으로 설정합니다.
     *
     * @param request 요청 정보
     * @param response 응답 메시지
     */
    private void post(Request request, Response response) {
        Map<String, String> params;
        params = ParamBuilder.getParamFromMultipart(request.getBody(), request);

        User user = Session.getUserBySessionId(request.getHeader("Cookie"));
        LocalDateTime time = LocalDateTime.now();

        try {
            postService.createPost(user.getName(), params, time);

            response.setCode(302);
            response.addHeader("Location", "/");
        } catch (IllegalArgumentException | NullPointerException e) {
            response.setCode(302);
            response.addHeader("Location", "/post/write_failed.html");
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
            response.setCode(302);
            response.addHeader("Location", "/404.html");
        }
    }
}
