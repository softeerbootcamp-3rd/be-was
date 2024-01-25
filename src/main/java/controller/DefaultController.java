package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        // root 인덱스로 접근 시 /index.html 반환
        if (request.getUri().equals("/")) {
            return responseDtoBuilder.response302Header()
                    .setHeaders("Location", "/index.html").build();
        }
        return getPage(request, logger);
    }
}
