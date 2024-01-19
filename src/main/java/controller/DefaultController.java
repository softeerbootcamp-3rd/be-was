package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtil;

public class DefaultController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        // root 인덱스로 접근 시 /index.html 반환
        if (request.getUri().equals("/")) {
            return HttpResponseUtil.response302("/index.html");
        }
        return getPage(request, logger);
    }
}
