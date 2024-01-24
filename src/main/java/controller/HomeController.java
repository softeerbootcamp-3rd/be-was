package controller;

import constants.ContentType;
import java.io.IOException;
import model.Request;
import model.Response;
import utils.HtmlBuilder;
import utils.PageReader;

public class HomeController implements Controller {

    public void route(Request request, Response response) {
        if (request.getUrl().equals("/")) {
            redirectIndex(response);
            return;
        }
        if (request.getUrl().equals("/index.html")) {
            getIndex(request, response);
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
     * index.html 페이지 파일을 찾아 응답 메시지를 설정합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답으로, 찾을 수 없는 경우 404 응답으로 설정합니다.
     * 페이지를 동적으로 변경합니다.
     *
     * @param request 요청 정보
     * @param response 응답 메시지
     */
    private void getIndex(Request request, Response response) {
        String filePath = "src/main/resources/templates/index.html";

        try{
            byte[] body = PageReader.getPage(filePath);
            body = HtmlBuilder.build(request, body).getBytes();

            response.setCode(200);
            response.setBody(body);
            response.addHeader("Content-Type", ContentType.findContentType(filePath));
        } catch (IOException e) {
            response.setCode(404);
            response.setBody(e.getMessage());
        }
    }

    /**
     * 페이지 외의 파일을 찾아 응답 메시지를 설정합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답으로, 찾을 수 없는 경우 404 응답으로 설정합니다..
     *
     * @param url 요청 타겟
     * @param response 응답 메시지
     */
    private void getStatic(String url, Response response) {
        String filePath = "src/main/resources/static" + url;

        try{
            byte[] body = PageReader.getPage(filePath);
            response.setCode(200);
            response.setBody(body);
            response.addHeader("Content-Type", ContentType.findContentType(filePath));
        } catch (IOException e) {
            response.setCode(404);
            response.setBody(e.getMessage());
        }
    }
}
