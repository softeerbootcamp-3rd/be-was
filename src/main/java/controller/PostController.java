package controller;

import constants.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import model.Request;
import model.Response;
import utils.HtmlBuilder;
import utils.PageReader;
import utils.ParamBuilder;
import webserver.Session;

public class PostController implements Controller {

    public void route(Request request, Response response) {
        String cookie = request.getHeader("Cookie");
        if (cookie != null && Session.getUserBySessionId(cookie) != null) {
            response.addHeader("Set-Cookie", cookie + "; Path=/; Max-Age=600");
        }

        if (request.getUrl().startsWith("/post/form")) {
            if (cookie == null || Session.getUserBySessionId(cookie) == null) {
                response.setCode(302);
                response.addHeader("Location", "/user/login.html");
                return;
            }
            if (request.getMethod().equals("POST")) {
                post(request, response);
                return;
            }
        }
        getPage(request, response);
    }

    private void post(Request request, Response response) {
        Map<String, String> params = ParamBuilder.getParamFromBody(request.getBody());

        for (String key : params.keySet()) {
            System.out.println(key + " : " + params.get(key));
        }
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);
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
}
