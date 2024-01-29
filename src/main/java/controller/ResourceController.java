package controller;

import annotation.GetMapping;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import session.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceController {
    Map<String, String> responseHeaders = new HashMap<>();
    final String CONTENT_TYPE = "Content-Type";
    final String CONTENT_LENGTH = "Content-Length";

    static Map<String, String> contentType = new HashMap<>();
    SessionManager sessionManager = new SessionManager();

    static {
        contentType.put("html", "text/html; charset=utf-8");
        contentType.put("css", "text/css; charset=utf-8");
        contentType.put("js", "text/javascript; charset=utf-8");
        contentType.put("png", "image/png");
        contentType.put("jpg", "image/jpg");
        contentType.put("ico", "image/x-icon");
    }
    @GetMapping(url = "/resources")
    public HttpResponse getResources(HttpRequest request) {
        String url;
        if (request.getUrl().endsWith(".html")) {
            url = "src/main/resources/templates" + request.getUrl();
        } else {
            url = "src/main/resources/static" + request.getUrl();
        }
        File file = new File(url);
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] body = fis.readAllBytes();

                User loginUser = sessionManager.getUserBySessionId(request);
                if (loginUser != null && url.endsWith(".html")) {
                    String content = new String(body, "UTF-8");
                    String replacedBody = replaceWord(content, "로그인", loginUser.getName());
                    body = replacedBody.getBytes("UTF-8");
                }

                responseHeaders.put(CONTENT_TYPE, getContentType(url));
                responseHeaders.put(CONTENT_LENGTH, String.valueOf(body.length));
                return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                        .headers(responseHeaders)
                        .body(body)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            responseHeaders.put(CONTENT_TYPE, "text/html;charset=utf-8");
            return new HttpResponseBuilder().status(HttpResponseStatus.NOT_FOUND)
                    .headers(responseHeaders)
                    .build();
        }
    }

    private String getContentType(String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        String result = contentType.get(extension);
        if (result == null) {
            return "font/" + extension;
        }
        return result;
    }

    private static String replaceWord(String content, String targetWord, String replacement) {
        // 정규 표현식 패턴 생성
        String regex = "\\b" + Pattern.quote(targetWord) + "\\b";
        Pattern pattern = Pattern.compile(regex);

        // 패턴과 일치하는 단어 찾기
        Matcher matcher = pattern.matcher(content);

        // 대체된 내용으로 문자열 빌드
        StringBuffer result = new StringBuffer();
        if (matcher.find()) {
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
