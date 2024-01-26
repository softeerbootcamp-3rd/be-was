package controller;

import constants.ContentType;
import java.io.IOException;
import model.Request;
import model.Response;
import utils.HtmlBuilder;
import utils.PageReader;
import webserver.Session;

public class QnaController implements Controller {

    public void route(Request request, Response response) {
        String cookie = request.getHeader("Cookie");
        if (cookie != null && Session.getUserBySessionId(cookie) != null) {
            response.addHeader("Set-Cookie", cookie + "; Path=/; Max-Age=600");
        }

        getPage(request, response);
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
