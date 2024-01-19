package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import util.HttpResponseUtil;
import util.WebUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface Controller {
    public abstract HttpResponseDto handleRequest(HttpRequestDto request);

    default HttpResponseDto getPage(HttpRequestDto request, Logger logger) {
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File(WebUtil.getPath(request.getUri())).toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponseUtil.response404();
        }

        return HttpResponseUtil.response200(body, WebUtil.getContentType(request.getUri()));
    }
}
