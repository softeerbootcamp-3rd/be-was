package controller;

import model.Request;
import model.Response;
import utils.PageReader;

public class HomeController implements Controller {

    public Response route(Request request) {
        if (request.getUrl().equals("/")) {
            return redirectIndex();
        }
        if (request.getUrl().equals("/index.html")) {
            return getIndex(request.getUrl());
        }
        return getStatic(request.getUrl());
    }

    /**
     * /index.html로 리다이렉트하는 응답을 반환합니다.
     *
     * @return /index.html 리다이렉션 응답
     */
    private Response redirectIndex() {
        return new Response(302, "/index.html");
    }

    /**
     * index.html 페이지 파일을 찾아 응답을 반환합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답을 반환하며, 요청한 파일을 찾을 수 없는 경우 404 응답을 반환합니다.
     *
     * @param url 요청 타겟
     * @return 요청을 수행한 결과를 담은 응답
     */
    private Response getIndex(String url) {
        String filePath = "src/main/resources/templates/index.html";
        return PageReader.getPage(url, filePath);
    }

    /**
     * 페이지 외의 파일을 찾아 응답을 반환합니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답을 반환하며, 요청한 파일을 찾을 수 없는 경우 404 응답을 반환합니다.
     *
     * @param url 요청 타겟
     * @return 요청을 수행한 결과를 담은 응답
     */
    private Response getStatic(String url) {
        String filePath = "src/main/resources/static" + url;
        return PageReader.getPage(url, filePath);
    }
}
