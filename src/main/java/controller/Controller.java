package controller;

import dto.HttpRequestDto;
import org.slf4j.Logger;
import util.HttpResponseUtil;
import util.WebUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface Controller {
    void handleRequest(HttpRequestDto request, DataOutputStream dos);

    default void getPage(HttpRequestDto request, DataOutputStream dos, Logger logger) {
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File(WebUtil.getPath(request.getUri())).toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
            body = "<h1>Hello, Suji👋</h1>".getBytes();
        }

        HttpResponseUtil.response200Header(dos, body.length, WebUtil.getContentType(request.getUri()));
        HttpResponseUtil.responseBody(dos, body);
    }
}
