package controller;

import constant.HttpHeader;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlBuilder;
import util.HttpResponseUtil;
import util.WebUtil;

public class QnaController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        if (request.getMethod().equalsIgnoreCase("GET") && request.getUri().startsWith("/qna/form")) {
            return printWriteForm(request);
        }
        if (request.getMethod().equalsIgnoreCase("POST") && request.getUri().startsWith("/qna/write")) {
            return writePost(request);
        }

        return HttpResponseUtil.loadResource(request, logger);
    }

    public HttpResponseDto printWriteForm(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        if (request.getUser() != null) {
            System.out.println("print write form >>>> ");
            // 로그인 한 경우
            byte[] body = HtmlBuilder.buildWriteFormPage(request);

            return responseDtoBuilder.response200Header()
                    .setHeaders(HttpHeader.CONTENT_TYPE, WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                    .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                    .setBody(body)
                    .build();
        }

        return responseDtoBuilder.response302Header()
                .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
    }

    public HttpResponseDto writePost(HttpRequestDto request) {
        // TODO: 글쓰기 기능 구현
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        if (request.getUser() != null) {

        }

        return responseDtoBuilder.response302Header()
                .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
    }
}
