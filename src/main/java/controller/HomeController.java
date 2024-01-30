package controller;

import constants.ContentType;
import java.io.IOException;
import model.Request;
import model.Response;
import utils.HtmlBuilder;
import utils.PageReader;
import webserver.Session;

public class HomeController implements Controller {

    public void route(Request request, Response response) {
        String cookie = request.getHeader("Cookie");
        if (cookie != null && Session.getUserBySessionId(cookie) != null) {
            response.addHeader("Set-Cookie", cookie + "; Path=/; Max-Age=600");
        }

        if (request.getUrl().equals("/")) {
            redirectIndex(response);
            return;
        }
        if (request.getUrl().endsWith(".html")) {
            getPage(request, response);
            return;
        }
        getStatic(request.getUrl(), response);
    }

    /**
     * 리다이렉션 응답으로 설정합니다.
     *
     * @param response 응답 메시지
     */
    private void redirectIndex(Response response) {
        response.setCode(302);
        response.addHeader("location", "/index.html");
    }

    /**
     * 페이지 파일을 찾아 응답 메시지를 설정합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답으로, 찾을 수 없는 경우 404 응답으로 설정합니다.
     * 페이지를 동적으로 변경합니다.
     *
     * @param request 요청 정보
     * @param response 응답 메시지
     */
    private void getPage(Request request, Response response) {
        String filePath = "src/main/resources/templates" + request.getUrl();

        try{
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

    /**
     * 페이지 외의 파일을 찾아 응답 메시지를 설정합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답으로, 찾을 수 없는 경우 404 응답으로 설정합니다.
     *
     * @param url 요청 타겟
     * @param response 응답 메시지
     */
    private void getStatic(String url, Response response) {
        String filePath;
        if (url.startsWith("/images")) {
            filePath = "src/main/resources" + url;
        } else {
            filePath = "src/main/resources/static" + url;
        }

        try{
            byte[] body = PageReader.getPage(filePath);

            response.setCode(200);
            response.setBody(body);
            response.addHeader("Content-Type", ContentType.findContentType(filePath));
            response.addHeader("Content-Length: ", String.valueOf(body.length));
        } catch (IOException e) {
            response.setCode(404);
        }
    }
}
