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

public class DefaultController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        // root 인덱스로 접근 시 /index.html 반환
        if (request.getUri().equals("/")) {
            return responseDtoBuilder.response302Header()
                    .setHeaders(HttpHeader.LOCATION, "/index.html").build();
        }
        if (request.getUri().equalsIgnoreCase("/index.html")) {
            return getIndexPage(request);
        }

        return HttpResponseUtil.loadResource(request, logger);
    }

    public HttpResponseDto getIndexPage(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        byte[] body = HtmlBuilder.buildIndexPage(request);

        return responseDtoBuilder.response200Header()
                .setHeaders(HttpHeader.CONTENT_TYPE, WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                .setBody(body)
                .build();
    }
}
