package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;
import util.WebUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public interface Controller {
    public abstract HttpResponseDto handleRequest(HttpRequestDto request);

    // 단순 리소스 요청 (GET) 처리 메서드
    default HttpResponseDto getPage(HttpRequestDto request, Logger logger) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        byte[] body = null;

        File file = new File(WebUtil.getPath(request.getUri()));

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            body = buffer;
        } catch (IOException e) {
            logger.error(e.getMessage());

            return responseDtoBuilder.response404Header().build();
        }

        return responseDtoBuilder.response200Header()
                .setHeaders("Content-Type", WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                .setHeaders("Content-Length", Integer.toString(body.length))
                .setBody(body)
                .build();
    }
}
