package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class HttpHeaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderUtil.class);

    public static String[] getHttpMethodAndUrl(BufferedReader bufferedReader) throws IOException {
        String httpRequest = bufferedReader.readLine().trim();
        String[] httpRequestTokens = httpRequest.split(" ");
        String httpMethod = httpRequestTokens[0];
        String requestUrl = httpRequestTokens[1];

        logger.debug("HTTP Method: " + httpMethod);
        logger.debug("HTTP Request URL: " + requestUrl);

        logHeaderInfo(bufferedReader);

        return httpRequestTokens;
    }

    private static void logHeaderInfo(BufferedReader bufferedReader) throws IOException {
        String requestHeaderLine = "";
        String headerContent = "";

        while (!(requestHeaderLine = bufferedReader.readLine().trim()).isEmpty()) {
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


    public static String getContentType(String requestedUrl) {
        Path source = Paths.get(requestedUrl);
        try {
            String contentType = Files.probeContentType(source);
            if (contentType == null) {
                if (requestedUrl.endsWith(".woff"))
                    contentType = "font/woff";
                else
                    throw new Exception("cannot determine content-type.");
            }
            return contentType;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "text/plain";
        }
    }
}
