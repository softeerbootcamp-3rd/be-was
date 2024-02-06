package controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.util.DateTimeUtil;
import controller.util.FileUtil;
import webserver.http.HttpStatus;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StaticResourceController {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceController.class);

    public static StaticResourceController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final StaticResourceController INSTANCE = new StaticResourceController();
    }

    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        File file = FileUtil.getFileFromUrl(request.getUrl());
        String contentType = FileUtil.getContentType(request.getUrl());

        if (!file.exists()) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return;
        }

        byte[] body = FileUtil.readFile(file);

        // 마지막 파일이 변경된 시점을 lastModified()를 통해 알 수 있다.
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime lastModified = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                        Instant.ofEpochMilli(file.lastModified())
                                .getEpochSecond()
                ),
                ZoneId.systemDefault()
        );

        response.addHeaderProperty("Content-Type", contentType);

        if (contentType.equals("text/html")) {
            response.setBody(body);
            FileUtil.generateDynamicMenuBar(request, response);
            return;
        }

        response.addHeaderProperty("Cache-Control", "public, max-age=30");
        response.addHeaderProperty("Content-Length", String.valueOf(body.length));
        response.addHeaderProperty("Date", DateTimeUtil.getGMTDateString(nowDate));
        response.addHeaderProperty("Last-Modified", DateTimeUtil.getGMTDateString(lastModified));

        // 만약 이전의 요청으로 마지막으로 언제 수정됬는지 브라우저가 알면 요청헤더로 다음과 같은 값이 추가된다.
        String modifiedSince = request.getHeaderProperty("If-Modified-Since");

        if (!modifiedSince.isBlank()) {
            if (DateTimeUtil.parseGMTDateString(modifiedSince)
                    .isEqual(lastModified)) {
                // 수정한 날짜가 동일하다면 파일이 수정되지 않았다는 의미이므로
                // Empty Body와 함께 304 Not Modified 로 응답함
                response.setStatusCode(HttpStatus.NOT_MODIFIED);
                return;
            }
        }

        response.setStatusCode(HttpStatus.OK);
        response.setBody(body);
    }
}
