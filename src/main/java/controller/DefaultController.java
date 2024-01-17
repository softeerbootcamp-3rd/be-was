package controller;

import dto.HttpRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtil;

import java.io.DataOutputStream;

public class DefaultController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Override
    public void handleRequest(HttpRequestDto request, DataOutputStream dos) {
        // root 인덱스로 접근 시 /index.html 반환
        if (request.getUri().equals("/")) {
            HttpResponseUtil.response302Header(dos, "/index.html");
        }
        getPage(request, dos, logger);
    }
}
