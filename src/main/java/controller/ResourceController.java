package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import session.SessionManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceController implements Controller {

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
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String url;
        Map<String, String> headers = new HashMap<>();
        if (request.getUrl().endsWith(".html")) {
            url = "src/main/resources/templates" + request.getUrl();
        } else {
            url = "src/main/resources/static" + request.getUrl();
        }
        File file = new File(url);
        if (file.isFile()) {
            try{
                byte[] body = Files.readAllBytes(new File(url).toPath());

                User loginUser = sessionManager.getUserBySessionId(request);
                if (loginUser != null && url.endsWith(".html")) {
                    String content = new String(body, StandardCharsets.UTF_8);
                    String replacedBody = replaceWord(content, "로그인", loginUser.getName());
                    body = replacedBody.getBytes(StandardCharsets.UTF_8);
                }

                headers.put("Content-Type", getContentType(url));
                headers.put("Content-Length", String.valueOf(body.length));
                response.setResponse(HttpResponseStatus.OK, body, headers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
            response.setResponse(HttpResponseStatus.NOT_FOUND, "404 NOT FOUND".getBytes(), headers);
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
        while (matcher.find()) {
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
