package utils;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException, NullPointerException {
        String request = bufferedReader.readLine().trim();
        String[] httpRequestTokens = request.split(" ");

        String method = httpRequestTokens[0];
        String url = httpRequestTokens[1];
        Map<String, String> queryString = new TreeMap<>();

        String[] url_split = url.split("\\?");

        url = url_split[0];

        if (url_split.length > 1) {
            String[] queryStringArray = url_split[1].split("&");

            for (String queryKeyValue: queryStringArray) {
                String[] query_split = queryKeyValue.split("=");
                if (query_split.length != 2 || query_split[1].isBlank())
                    throw new IOException("Incorrect Query String");

                queryString.put(query_split[0], query_split[1]);
            }
        }

        logger.debug("HTTP Method: " + method);
        logger.debug("HTTP Request URL: " + url);

        logRequestHeaderInfo(bufferedReader);

        return new HttpRequest(method, url, queryString);
    }

    private static void logRequestHeaderInfo(BufferedReader bufferedReader) throws IOException {
        String requestHeaderLine = "";
        String headerContent = "";

        while ((requestHeaderLine = bufferedReader.readLine()) != null && !requestHeaderLine.isEmpty()) {
            requestHeaderLine = requestHeaderLine.trim();
            if (requestHeaderLine.startsWith("Host: ")) {
                headerContent = requestHeaderLine.substring("Host: ".length());
                logger.debug("요청하는 호스트의 이름과 포트번호: " + headerContent);
            }
            if (requestHeaderLine.startsWith("Connection: ")) {
                headerContent = requestHeaderLine.substring("Connection: ".length());
                logger.debug("현재 연결을 유지한 채로 HTTP 통신을 주고 받을 것인지(close, keep-alive): " + headerContent);
            }
            if (requestHeaderLine.startsWith("Referer: ")) {
                headerContent = requestHeaderLine.substring("Referer: ".length());
                logger.debug("직전에 머물었던 URL: " + headerContent);
            }
            if (requestHeaderLine.startsWith("User-Agent: ")) {
                headerContent = requestHeaderLine.substring("User-Agent: ".length());
                logger.debug("클라이언트 웹 브라우저 명칭 및 버전 정보: " + headerContent);
            }
            if (requestHeaderLine.startsWith("Accept: ")) {
                headerContent = requestHeaderLine.substring("Accept: ".length());
                logger.debug("클라이언트가 원하는 미디어 타입 및 우선순위: " + headerContent);
            }
            if (requestHeaderLine.startsWith("Accept-Encoding: ")) {
                headerContent = requestHeaderLine.substring("Accept-Encoding: ".length());
                logger.debug("클라이언트가 원하는 문자 인코딩 방식: " + headerContent);
            }
            if (requestHeaderLine.startsWith("Accept-Language: ")) {
                headerContent = requestHeaderLine.substring("Accept-Language: ".length());
                logger.debug("클라이언트가 원하는 언어: " + headerContent);
            }
        }
    }

    public static String getContentTypeFromUrl(String requestedUrl) throws IOException {
        Path source = Paths.get(requestedUrl);
            String contentType = Files.probeContentType(source);
            if (contentType == null) {
                if (requestedUrl.endsWith(".woff"))
                    contentType = "font/woff";
                else
                    throw new IOException("cannot determine content-type.");
            }
            return contentType;
    }
}
