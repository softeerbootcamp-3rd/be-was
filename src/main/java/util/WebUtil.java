package util;

import dto.HttpRequestDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dto.HttpRequestDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WebUtil {
    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    private static final Map<String, String> MIME_CONTENT_TYPE = new HashMap<>();

    private static final String DEFAULT_CONTENT_LENGTH = "0";

    static {
        MIME_CONTENT_TYPE.put("html", "text/html");
        MIME_CONTENT_TYPE.put("css", "text/css");
        MIME_CONTENT_TYPE.put("js", "text/javascript");
        MIME_CONTENT_TYPE.put("json", "application/json");
        MIME_CONTENT_TYPE.put("png", "image/png");
        MIME_CONTENT_TYPE.put("jpg", "image/jpeg");
        MIME_CONTENT_TYPE.put("jpeg", "image/jpeg");
        MIME_CONTENT_TYPE.put("gif", "image/gif");
        MIME_CONTENT_TYPE.put("svg", "image/svg+xml");
        MIME_CONTENT_TYPE.put("ttf", "font/ttf");
    }

    // HTTP Request를 파싱해서 HttpRequestParam 객체로 리턴
    public static HttpRequestDto httpRequestParse(InputStream request) {
        BufferedReader br = new BufferedReader(new InputStreamReader(request));
        HttpRequestDto parsedRequest = null;

        try {
            // Parsing Request Line
            String[] requestLines = br.readLine().split(" ");

            // Parsing Request Headers
            // TODO: Request Headers는 multiple value를 가질 수 있음
            Map<String, String> requestHeaders = new HashMap<>();
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) break;
                String[] requestHeader = line.split(": ");
                requestHeaders.put(requestHeader[0], requestHeader[1]);
            }

            // Parsing Request Body
            StringBuilder stringBuilder = new StringBuilder();
            int contentLength = Integer.parseInt(requestHeaders.getOrDefault("Content-Length", DEFAULT_CONTENT_LENGTH));
            char[] charBuffer = new char[contentLength];
            br.read(charBuffer);
            stringBuilder.append(charBuffer, 0, contentLength);
            String requestBody = stringBuilder.toString();

            // Create HttpRequestDto
            parsedRequest = new HttpRequestDtoBuilder(requestLines[0], requestLines[1], requestLines[2])
                    .setHeaders(requestHeaders)
                    .setBody(requestBody)
                    .build();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return parsedRequest;
    }

    public static String getFileExtension(String uri) {
        String[] parsedUri = uri.split("\\.");
        return parsedUri[parsedUri.length - 1];
    }

    public static String getPath(String uri) {
        String extension = getFileExtension(uri);
        if (extension.equals("html")) {
            return "./src/main/resources/templates" + uri;
        }
        return "./src/main/resources/static" + uri;
    }

    public static String getContentType(String uri) {
        String extension = getFileExtension(uri);
        return MIME_CONTENT_TYPE.getOrDefault(extension, "text/html");
    }

    public static Map<String, String> parseQueryString(String uri) {
        Map<String, String> parameters = new HashMap<>();
        try {
            String queryString = uri.split("\\?")[1];
            String[] pairs = queryString.split("&");
            for (String pair: pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return parameters;
    }
}
